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
import javax.swing.JTextField;

public class HomePage extends JPanel {

    private static final int FRAME_WIDTH = 650;
    private static final int FRAME_HEIGHT = 750;
    CardLayout cardLayout = new CardLayout();
    private JPanel southPanel, northPanel, centerPanel;
    private JButton home, following, addActivity, personalInfo, searchBtn;
    JPanel actListPanel, followingPanel, addNew, personalPanel;
    JTextField searchBar;
    Font font = new Font("Arial", Font.PLAIN, 20);
    ArrayList<JButton> btns = new ArrayList<>();
    ArrayList<JButton> topbtns = new ArrayList<>();


    // 圖片
    Image imageHome, imageFollowing, imageAddNew, imageInfo;

    public HomePage() {
        setLayout(new BorderLayout());
        

        imageHome = new ImageIcon(getClass().getResource("/images/home.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageFollowing = new ImageIcon(getClass().getResource("/images/activity.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageAddNew = new ImageIcon(getClass().getResource("/images/add.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageInfo = new ImageIcon(getClass().getResource("/images/personalInfo.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

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
        northPanel = new JPanel();
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BorderLayout());
        homePanel.add(northPanel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAll = new JButton("全部");
        JButton btnAct = new JButton("活動");
        JButton btnGroup = new JButton("揪團");
        filterPanel.add(btnAll);
        filterPanel.add(btnAct);
        filterPanel.add(btnGroup);
        topbtns.add(btnAll);
        topbtns.add(btnAct);
        topbtns.add(btnGroup);

        filterPanel.setBackground(Color.WHITE);
        northPanel.setBackground(Color.WHITE);

        northPanel.add(filterPanel);
        centerPanel = new JPanel();
        centerPanel.setLayout(cardLayout);

        actListPanel = new JPanel();
        actListPanel.setLayout(new BoxLayout(actListPanel, BoxLayout.Y_AXIS));
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

        personalPanel = new JPanel();

        centerPanel.add(homePanel, "home");
        centerPanel.add(addNew, "addNew"); //連接EditPanel
        centerPanel.add(followingPanel, "following");
        centerPanel.add(personalPanel, "my");

        homePanel.add(actListPanel, BorderLayout.CENTER);
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
    private JPanel createActCard(Activity act, boolean isFavorited) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(600, 100));
        panel.setMaximumSize(new Dimension(600, 100));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setBackground(new Color(250, 250, 250));

        String title = String.format(
                "<html><b>%s%s</b><br>時間：%s %s<br>地點：%s</html>",
                isFavorited ? "★ " : "",
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
        Color normalColor = new Color(246, 209, 86);
        Color pressedColor = new Color(195, 170, 87);
        Font f = new Font("Calibri", Font.PLAIN, 18);

// ---------------底下四個按鈕-------------------
        for (JButton btn : btns) {
            final JButton thisBtn = btn;
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

// ---------------主頁面上面三個按鈕-------------------
        for (JButton btn : topbtns) {
            final JButton thisBtn = btn;
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
        }
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
