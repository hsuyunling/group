package org.groupapp;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

import java.util.ArrayList;

import java.awt.*;
import java.util.List;
import javax.swing.*;

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

    // 圖片
    Image imageHome, imageFollowing, imageAddNew, imageInfo;

    public HomePage() {
        setLayout(new BorderLayout());
        setTitle("Group");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);

        imageHome = new ImageIcon(getClass().getResource("/images/home.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageFollowing = new ImageIcon(getClass().getResource("/images/activity.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageAddNew = new ImageIcon(getClass().getResource("/images/add.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageInfo = new ImageIcon(getClass().getResource("/images/personalInfo.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        createNorthPanel();
        createCenterPanel();
        createSouthPanel();

        cardLayout.show(centerPanel, "home");

        setBtnActionListener(home, "home");
        setBtnActionListener(following, "following");
        setBtnActionListener(addActivity, "addNew");
        setBtnActionListener(personalInfo, "my");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setBtnActionListener(JButton btn, String cardName) {
        btn.addActionListener(e -> cardLayout.show(centerPanel, cardName));
    }

    public void createNorthPanel() {
        northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton menuBtn = new JButton("≡");
        JLabel logoLabel = new JLabel("LOGO");
        logoPanel.add(menuBtn);
        logoPanel.add(logoLabel);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAll = new JButton("全部");
        JButton btnAct = new JButton("活動");
        JButton btnGroup = new JButton("揪團");
        filterPanel.add(btnAll);
        filterPanel.add(btnAct);
        filterPanel.add(btnGroup);

        northPanel.add(logoPanel, BorderLayout.WEST);
        northPanel.add(filterPanel, BorderLayout.SOUTH);
        northPanel.setBackground(new Color(230, 230, 230));

        add(northPanel, BorderLayout.NORTH);
    }

    public void createCenterPanel() {
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
    followingPanel = new JPanel();
    personalPanel = new JPanel();

    centerPanel.add(actListPanel, "home");
    centerPanel.add(addNew, "addNew");
    centerPanel.add(followingPanel, "following");
    centerPanel.add(personalPanel, "my");

    add(centerPanel, BorderLayout.CENTER);
}

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

   private JPanel createActCard(Activity act, boolean isFavorited) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setPreferredSize(new Dimension(600, 100));
    panel.setMaximumSize(new Dimension(600, 100));
    panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    panel.setBackground(new Color(250, 250, 250));

    String title = String.format(
        "<html><b>%s%s</b><br>時間：%s %s<br>地點：%s</html>",
        isFavorited ? "★ " : "",
        act.getName(), act.getDate(), act.getTime(), act.getPlace()
    );

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
            new ActivityDetailFrame(act.getId(), userId).setVisible(true);
        }
    });

    return panel;
}


    public void setBtnStyle() {
        Color normalColor = new Color(246, 209, 86);
        Color pressedColor = new Color(195, 170, 87);
        Font f = new Font("Calibri", Font.PLAIN, 18);

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
    }
}
