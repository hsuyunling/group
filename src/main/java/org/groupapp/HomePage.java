package org.groupapp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class HomePage extends JPanel {

    private static final int FRAME_WIDTH = 650;
    private static final int FRAME_HEIGHT = 750;
    CardLayout cardLayout = new CardLayout();
    private JPanel southPanel, northPanel, centerPanel;
    private JButton home, following, addActivity, personalInfo, searchBtn;
    JPanel actListPanel, followingPanel, addNew, personalPanel;
    JTextField searchField;
    Font font = new Font("Arial", Font.PLAIN, 18);
    ArrayList<JButton> btns = new ArrayList<>();
    User user = new User();

    Color normalColor = new Color(246, 209, 86);
    Color pressedColor = new Color(195, 170, 87);

    // 圖片
    Image imageHome, imageFollowing, imageAddNew, imageInfo, imageSearch;

    public HomePage(User user) {
        this.user = user;
        setLayout(new BorderLayout());

        imageHome = new ImageIcon(getClass().getResource("/home.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageFollowing = new ImageIcon(getClass().getResource("/following.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageAddNew = new ImageIcon(getClass().getResource("/add.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageInfo = new ImageIcon(getClass().getResource("/person.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageSearch = new ImageIcon(getClass().getResource("/search.png"))
                .getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);

        createCenterPanel();
        createSouthPanel();

        cardLayout.show(centerPanel, "home");

        setBtnActionListener(home, "home");
        setBtnActionListener(following, "following");
        setBtnActionListener(addActivity, "addNew");
        setBtnActionListener(personalInfo, "my");
    }

    // ---------------切換主頁面-------------------
    public void setBtnActionListener(JButton btn, String cardName) {
        btn.addActionListener(e -> {
            if ("following".equals(cardName)) {
                centerPanel.remove(followingPanel); // 先移除舊的
                followingPanel = new FollowingPanel(); // 建立新的
                centerPanel.add(followingPanel, "following"); // 加入新的
            }
            cardLayout.show(centerPanel, cardName); // 切換畫面
        });
    }

    // ---------------主頁面-------------------
    public void createCenterPanel() {
        northPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BorderLayout());
        homePanel.add(northPanel, BorderLayout.NORTH);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.setMaximumSize(new Dimension(200, 35));
        searchField.putClientProperty("JComponent.roundRect", true);
        searchField.putClientProperty("JTextField.placeholderText", "搜尋揪團...");

        searchBtn = new JButton();
        Icon searchIcon = new ImageIcon(imageSearch);
        searchBtn.setIcon(searchIcon);

        northPanel.add(searchField);
        northPanel.add(searchBtn);
        northPanel.add(Box.createRigidArea(new Dimension(10,0)));


        northPanel.setBackground(normalColor);

        centerPanel = new JPanel();
        centerPanel.setLayout(cardLayout);

        actListPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(actListPanel);
        actListPanel.setLayout(new BoxLayout(actListPanel, BoxLayout.Y_AXIS));
        actListPanel.add(Box.createVerticalStrut(10));
        actListPanel.setBackground(Color.WHITE);

        List<Activity> activities = DBUtil.getAllActivities();
        String userId = ActivityDetailFrame.getCurrentUserId();
        Set<Integer> favoriteIds = (userId != null) ? DBUtil.getFavoriteActivityIds(userId) : new HashSet<>();

        if (activities.isEmpty()) {
            actListPanel.add(new JLabel("目前沒有活動"));
        } else {
            for (Activity act : activities) {
                boolean isFavorited = favoriteIds.contains(act.getId());
                actListPanel.add(createActCard(act, isFavorited));
                actListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        addNew = new EditPanel();
        followingPanel = new FollowingPanel();

        personalPanel = new PersonalPanel(user);

        centerPanel.add(homePanel, "home");
        centerPanel.add(addNew, "addNew"); // 連接EditPanel
        centerPanel.add(followingPanel, "following");
        centerPanel.add(personalPanel, "my");

        homePanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel);
    }

    // ---------------底下的四個按鈕-------------------
    public void createSouthPanel() {
        southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 4));

        Icon homeIcon = new ImageIcon(imageHome);
        Icon addIcon = new ImageIcon(imageAddNew);
        Icon followingIcon = new ImageIcon(imageFollowing);
        Icon infoIcon = new ImageIcon(imageInfo);

        home = new JButton();
        home.setIcon(homeIcon);
        home.setToolTipText("Home");

        addActivity = new JButton();
        addActivity.setIcon(addIcon);
        addActivity.setToolTipText("Add");

        following = new JButton();
        following.setIcon(followingIcon);
        following.setToolTipText("Following");

        personalInfo = new JButton();
        personalInfo.setIcon(infoIcon);
        personalInfo.setToolTipText("My");

        btns.add(home);
        btns.add(addActivity);
        btns.add(following);
        btns.add(personalInfo);

        setBtnStyle();

        southPanel.setPreferredSize(new Dimension(650, 60));
        add(southPanel, BorderLayout.SOUTH);
    }


// ---------------中間的活動-------------------
    private RoundedPanel createActCard(Activity act, boolean isFavorited) {
        RoundedPanel panel = new RoundedPanel(15);
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(600, 120));
        panel.setMaximumSize(new Dimension(600, 120));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
        panel.setBackground(new Color(246, 220, 135));

        String title = String.format(
                "<html><div><b>%s%s</b><br><br>時間：%s %s<br>地點：%s<div></html>",
                isFavorited ? "★ " : "  ",
                act.getName(), act.getDate(), act.getTime(), act.getPlace());

        JLabel label = new JLabel(title);
        label.setFont(font);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(label, BorderLayout.CENTER);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String userId = ActivityDetailFrame.getCurrentUserId();
                if (userId == null) {
                    JOptionPane.showMessageDialog(null, "尚未登入，請先登入");
                    return;
                }
                new ActivityDetailFrame(act.getId(), userId, HomePage.this).setVisible(true);
            }
        });

        return panel;
    }

    // ---------------按鈕形式-------------------
    public void setBtnStyle() {

        Font f = new Font("Calibri", Font.PLAIN, 18);

        // ---------------底下四個按鈕-------------------
        for (JButton btn : btns) {
            final JButton thisBtn = btn;
            thisBtn.putClientProperty( "JButton.buttonType", "square" );
            thisBtn.setOpaque(true);
            thisBtn.setBorderPainted(false);
            thisBtn.setContentAreaFilled(true);
            thisBtn.setFocusPainted(false);

            thisBtn.setBackground(normalColor);

            thisBtn.getModel().addChangeListener(e -> {
                ButtonModel model = thisBtn.getModel();
                if (model.isPressed()) {
                    thisBtn.setBackground(pressedColor);
                } else {
                    thisBtn.setBackground(normalColor);
                }
            });
            southPanel.add(thisBtn);
        }

        
        searchBtn.putClientProperty( "JButton.buttonType", "toolBarButton" );
        searchBtn.setOpaque(true);
        searchBtn.setBorderPainted(false);
        searchBtn.setContentAreaFilled(true);
        searchBtn.setFocusPainted(false);

    }

    public void refreshActivityList() {
        actListPanel.removeAll();

        List<Activity> activities = DBUtil.getAllActivities();
        String userId = ActivityDetailFrame.getCurrentUserId();
        Set<Integer> favoriteIds = (userId != null) ? DBUtil.getFavoriteActivityIds(userId) : new HashSet<>();

        for (Activity act : activities) {
            boolean isFavorited = favoriteIds.contains(act.getId());
            actListPanel.add(createActCard(act, isFavorited));
            actListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        actListPanel.revalidate();
        actListPanel.repaint();
    }

}
