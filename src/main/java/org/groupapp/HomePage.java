
package org.groupapp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HomePage extends JFrame {

    private static final int FRAME_WIDTH = 650;
    private static final int FRAME_HEIGHT = 750;
    CardLayout cardLayout = new CardLayout();
    private JPanel southPanel, northPanel, centerPanel;
    private JButton home, following, addActivity, personalInfo, searchBtn;
    JPanel actListPanel, followingPanel, addNew, personalPanel;
    JTextField searchBar;

    public HomePage() {
        setLayout(new BorderLayout());
        setTitle("Group");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        createNorthPanel();// 頁面最上方
        createCenterPanel();// 頁面中間
        createSouthPanel();// 頁面最下方

        cardLayout.show(centerPanel, "home");

        // set the south btn
        setBtnActionListener(home, "home");
        setBtnActionListener(following, "following");
        setBtnActionListener(addActivity, "addNew");
        setBtnActionListener(personalInfo, "my");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // 頁面最上方
    public void createNorthPanel() {
        northPanel = new JPanel();
        searchBar = new JTextField(10);
        searchBtn = new JButton("查詢");

        searchBtn.addActionListener(e -> searchAction());

        northPanel.add(searchBar);
        northPanel.add(searchBtn);

        northPanel.setBackground(Color.gray);

        add(northPanel, BorderLayout.NORTH);
    }

    // 頁面中間
    public void createCenterPanel() {
        centerPanel = new JPanel();
        addNew = new EditPanel();
        actListPanel = new JPanel();
        followingPanel = new JPanel();
        personalPanel = new JPanel();

        centerPanel.setLayout(cardLayout);
        centerPanel.add(actListPanel, "home");
        centerPanel.add(followingPanel, "following");
        centerPanel.add(addNew, "addNew");
        centerPanel.add(personalPanel, "my");

        add(centerPanel, BorderLayout.CENTER);

    }

    // 頁面最下方
    public void createSouthPanel() {
        southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 4));
        home = new JButton("home");
        following = new JButton("following");
        addActivity = new JButton("+");
        personalInfo = new JButton("My");

        southPanel.add(home);
        southPanel.add(following);
        southPanel.add(addActivity);
        southPanel.add(personalInfo);

        southPanel.setBackground(Color.GRAY);
        southPanel.setPreferredSize(new Dimension(650, 60));

        add(southPanel, BorderLayout.SOUTH);

    }

    public void setBtnActionListener(JButton btn, String s) {
        btn.addActionListener(e -> {
            cardLayout.show(centerPanel, s);
        });

    }

    public void searchAction() {
        String keyword = searchBar.getText().trim();
        actListPanel.removeAll();

        java.util.List<String> results = DBUtil.searchActivitiesByName(keyword);

        if (results.isEmpty()) {
            actListPanel.add(new JLabel("沒有找到相關活動"));
        } else {
            for (String line : results) {
                actListPanel.add(new JLabel(line));
            }
        }

        searchBar.setText("");

        actListPanel.revalidate();
        actListPanel.repaint();
        cardLayout.show(centerPanel, "home");
    }

}
