package org.groupapp;

import java.awt.*;
import java.util.*;
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

    // 僅宣告圖片變數
    Image imageHome, imageFollowing, imageAddNew, imageInfo;

    // -------------constructor-------------
    public HomePage() {
        setLayout(new BorderLayout());
        setTitle("Group");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);

        // 初始化圖片（從 resources/images 中載入）
        imageHome = new ImageIcon(getClass().getResource("/images/home.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageFollowing = new ImageIcon(getClass().getResource("/images/activity.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageAddNew = new ImageIcon(getClass().getResource("/images/add.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        imageInfo = new ImageIcon(getClass().getResource("/images/personalInfo.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        createNorthPanel(); // 頁面最上方
        createCenterPanel(); // 頁面中間
        createSouthPanel(); // 頁面最下方
        System.out.println("imageHome = " + imageHome);
        System.out.println("imageFollowing = " + imageFollowing);
        System.out.println("imageAddNew = " + imageAddNew);
        System.out.println("imageInfo = " + imageInfo);
        cardLayout.show(centerPanel, "home");

        // 設定下方按鈕切換功能
        setBtnActionListener(home, "home");
        setBtnActionListener(following, "following");
        setBtnActionListener(addActivity, "addNew");
        setBtnActionListener(personalInfo, "my");
        System.out.println("home.png: " + getClass().getResource("/images/home.png"));
        System.out.println("activity.png: " + getClass().getResource("/images/activity.png"));
        System.out.println("add.png: " + getClass().getResource("/images/add.png"));
        System.out.println("personalInfo.png: " + getClass().getResource("/images/personalInfo.png"));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setBtnActionListener(JButton btn, String cardName) {
        btn.addActionListener(e -> cardLayout.show(centerPanel, cardName));
    }

    // -------------頁面最上方-------------
    public void createNorthPanel() {
        northPanel = new JPanel();
        searchBar = new JTextField(10);
        searchBtn = new JButton("查詢");
        // searchBtn.addActionListener(e -> searchAction());

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
        personalPanel = new JPanel();

        centerPanel.setLayout(cardLayout);
        centerPanel.add(actListPanel, "home");
        centerPanel.add(addNew, "addNew");
        centerPanel.add(followingPanel, "following");
        centerPanel.add(personalPanel, "my");

        add(centerPanel, BorderLayout.CENTER);
    }

    // -------------頁面最下方-------------
    public void createSouthPanel() {
        southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 4));

        // 建立 icon 並確認是否成功
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

        // 放入列表統一設定樣式
        btns.add(home);
        btns.add(addActivity);
        btns.add(following);
        btns.add(personalInfo);

        setBtnStyle();

        southPanel.setPreferredSize(new Dimension(650, 60));
        add(southPanel, BorderLayout.SOUTH);
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
            // thisBtn.setForeground(Color.BLACK);
            // thisBtn.setFont(f);

            thisBtn.getModel().addChangeListener(e -> {
                ButtonModel model = thisBtn.getModel();
                if (model.isPressed()) {
                    thisBtn.setBackground(pressedColor);
                    // thisBtn.setForeground(Color.WHITE);
                } else {
                    thisBtn.setBackground(normalColor);
                    // thisBtn.setForeground(Color.BLACK);
                }
            });
            southPanel.add(thisBtn);
        }
    }

    // public void searchAction() {
    // String keyword = searchBar.getText().trim();
    // actListPanel.removeAll();
    // java.util.List<String> results = DBUtil.searchActivitiesByName(keyword);
    // if (results.isEmpty()) {
    // actListPanel.add(new JLabel("沒有找到相關活動"));
    // } else {
    // for (String line : results) {
    // actListPanel.add(new JLabel(line));
    // }
    // }
    // searchBar.setText("");
    // actListPanel.revalidate();
    // actListPanel.repaint();
    // cardLayout.show(centerPanel, "home");
    // }
}
