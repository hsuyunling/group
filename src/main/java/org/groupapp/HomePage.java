package org.groupapp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ButtonModel;
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
    Font font = new Font("Arial", Font.PLAIN, 20);
    ArrayList<JButton> btns = new ArrayList<>();

    // -------------constructor-------------
    public HomePage() {
        setLayout(new BorderLayout());
        setTitle("Group");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        createNorthPanel();   // 頁面最上方
        createCenterPanel();  // 頁面中間
        createSouthPanel();   // 頁面最下方

        cardLayout.show(centerPanel, "home");

        // set the south btn
        setBtnActionListener(home, "home");
        setBtnActionListener(following, "following");
        setBtnActionListener(addActivity, "addNew");
        setBtnActionListener(personalInfo, "my");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // -------------頁面最上方-------------
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

    // -------------頁面中間--------------
    public void createCenterPanel() {
        centerPanel = new JPanel();
        addNew = new EditPanel();
        actListPanel = new JPanel();
        followingPanel = new JPanel();
        personal
