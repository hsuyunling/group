// HomePage.java（已優化）
package org.groupapp;

// Java AWT 僅引入實際需要的類別（例如 Color, Dimension 等）
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Component;

// 只使用 java.util 中實際需要的類型，避免 List 混淆
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// 並發工具
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

// Swing
import javax.swing.*;

public class HomePage extends JPanel {

    private static final int FRAME_WIDTH = 650;
    private static final int FRAME_HEIGHT = 750;
    CardLayout cardLayout = new CardLayout();
    private JPanel southPanel, northPanel, centerPanel;
    private JButton home, following, addActivity, personalInfo;
    JPanel actListPanel, followingPanel, addNew, personalPanel;
    Font font = new Font("Microsoft JhengHei", Font.PLAIN, 18);
    ArrayList<JButton> btns = new ArrayList<>();
    User user;
    Color normalColor = new Color(246, 209, 86);
    Color pressedColor = new Color(195, 170, 87);

    private static final Map<String, Icon> iconCache = new ConcurrentHashMap<>();
    private List<Activity> cachedActivities = new ArrayList<>();
    private Set<Integer> cachedFavoriteIds = new HashSet<>();
    private long lastCacheUpdate = 0;
    private static final long CACHE_DURATION = 30000;

    public HomePage(User user) {
        if (user == null) {
            throw new IllegalArgumentException("用戶對象不能為空");
        }
        this.user = user;
        setLayout(new BorderLayout());
        initBasicUI();
        loadResourcesInBackground();
    }

    private void preloadImages() {
        loadImageToCache("/home.png", "home");
        loadImageToCache("/following.png", "following");
        loadImageToCache("/add.png", "add");
        loadImageToCache("/person.png", "person");
        loadImageToCache("/search.png", "search");
    }

    private void loadImageToCache(String resourcePath, String key) {
        if (!iconCache.containsKey(key)) {
            try {
                Image image = new ImageIcon(getClass().getResource(resourcePath))
                        .getImage()
                        .getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                iconCache.put(key, new ImageIcon(image));
            } catch (Exception e) {
                System.err.println("載入圖片失敗 " + resourcePath + ": " + e.getMessage());
            }
        }
    }

    private void initBasicUI() {
        centerPanel = new JPanel();
        centerPanel.setLayout(cardLayout);
        centerPanel.add(createLoadingPanel(), "loading");
        add(centerPanel);
        cardLayout.show(centerPanel, "loading");
        createBasicSouthPanel();
    }

    private JPanel createLoadingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(normalColor);
        
        // 創建一個專門的標籤面板
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setBackground(normalColor);
        
        JLabel loadingLabel = new JLabel("載入中...");
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 18));
        loadingLabel.setName("loadingLabel"); // 給標籤一個名字，方便之後找到它
        
        labelPanel.add(Box.createVerticalGlue());
        labelPanel.add(loadingLabel);
        labelPanel.add(Box.createVerticalGlue());
        
        panel.add(labelPanel);
        return panel;
    }

    private void loadResourcesInBackground() {
        // 找到載入標籤
        JPanel loadingPanel = (JPanel) centerPanel.getComponent(0);
        JPanel labelPanel = (JPanel) loadingPanel.getComponent(0);
        final JLabel[] loadingLabelRef = new JLabel[1];
        
        // 遍歷所有組件找到標籤
        for (Component comp : labelPanel.getComponents()) {
            if (comp instanceof JLabel && "loadingLabel".equals(comp.getName())) {
                loadingLabelRef[0] = (JLabel) comp;
                break;
            }
        }
        
        if (loadingLabelRef[0] == null) {
            System.err.println("找不到載入標籤");
            return;
        }

        loadingLabelRef[0].setText("正在載入資源...");

        // 立即載入所有圖片，包括首頁圖標
        CompletableFuture.runAsync(() -> {
            try {
                // 載入所有圖標，包括首頁和搜尋
                List<String> allImages = List.of(
                    "/home.png", "/search.png", "/following.png", "/add.png", "/person.png"
                );
                allImages.parallelStream().forEach(path -> {
                    String key = path.substring(1, path.length() - 4);
                    loadImageToCache(path, key);
                });
            } catch (Exception e) {
                System.err.println("載入圖片時發生錯誤: " + e.getMessage());
            }
        });

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // 1. 加載活動列表和收藏列表（並行執行）
                publish("正在載入活動列表...");
                CompletableFuture<List<Activity>> activitiesFuture = CompletableFuture
                        .supplyAsync(DBUtil::getAllActivities);
                
                CompletableFuture<Set<Integer>> favoritesFuture = CompletableFuture.supplyAsync(() -> {
                    String userId = ActivityDetailFrame.getCurrentUserId();
                    return (userId != null) ? DBUtil.getFavoriteActivityIds(userId) : new HashSet<>();
                });

                // 2. 等待數據加載完成
                CompletableFuture.allOf(activitiesFuture, favoritesFuture).join();
                cachedActivities = activitiesFuture.get();
                cachedFavoriteIds = favoritesFuture.get();
                lastCacheUpdate = System.currentTimeMillis();

                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                if (!chunks.isEmpty()) {
                    loadingLabelRef[0].setText(chunks.get(chunks.size() - 1));
                }
            }

            @Override
            protected void done() {
                try {
                    get();
                    // 立即創建UI，不等待其他圖片
                    SwingUtilities.invokeLater(() -> {
                        createFullUI();
                        // 使用更短的檢查間隔，檢查所有圖標
                        Timer timer = new Timer(50, e -> {
                            if (iconCache.containsKey("home") &&
                                iconCache.containsKey("search") &&
                                iconCache.containsKey("following") && 
                                iconCache.containsKey("add") && 
                                iconCache.containsKey("person")) {
                                updateSouthPanelIcons();
                                ((Timer)e.getSource()).stop();
                            }
                        });
                        timer.setRepeats(true);
                        timer.start();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    loadingLabelRef[0].setText("載入失敗：" + e.getMessage());
                    JOptionPane.showMessageDialog(HomePage.this, 
                        "載入失敗：" + e.getMessage(), 
                        "錯誤",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void createFullUI() {
        centerPanel.removeAll();
        createCenterPanel();
        updateSouthPanelIcons();
        cardLayout.show(centerPanel, "home");

        // 設置按鈕事件監聽器
        setBtnActionListener(home, "home");
        setBtnActionListener(following, "following");
        setBtnActionListener(addActivity, "addNew");
        setBtnActionListener(personalInfo, "my");

        // 使用 SwingUtilities.invokeLater 確保 UI 更新在 EDT 中進行
        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }

    public void setBtnActionListener(JButton btn, String cardName) {
        btn.addActionListener(e -> {
            if ("following".equals(cardName)) {
                centerPanel.remove(followingPanel);
                followingPanel = new FollowingPanel();
                centerPanel.add(followingPanel, "following");
            }
            cardLayout.show(centerPanel, cardName);
        });
    }

    public void createCenterPanel() {
        if (user == null) {
            throw new IllegalStateException("用戶對象未初始化");
        }

        northPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.add(northPanel, BorderLayout.NORTH);

        JTextField questionField = new JTextField();
        JButton search = new JButton();
        search.setIcon(iconCache.getOrDefault("search", null));
        northPanel.add(questionField);
        northPanel.add(search);
        northPanel.setBackground(normalColor);

        actListPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(actListPanel);
        actListPanel.setLayout(new BoxLayout(actListPanel, BoxLayout.Y_AXIS));
        actListPanel.setBackground(Color.WHITE);

        displayActivities();

        addNew = new EditPanel();
        followingPanel = new FollowingPanel();
        personalPanel = new PersonalPanel(user);

        centerPanel.add(homePanel, "home");
        centerPanel.add(addNew, "addNew");
        centerPanel.add(followingPanel, "following");
        centerPanel.add(personalPanel, "my");

        homePanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel);
    }

    private void displayActivities() {
        actListPanel.removeAll();
        if (cachedActivities.isEmpty()) {
            actListPanel.add(new JLabel("目前沒有活動"));
        } else {
            SwingWorker<Void, RoundedPanel> displayWorker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    int i = 0;
                    for (Activity act : cachedActivities) {
                        boolean isFavorited = cachedFavoriteIds.contains(act.getId());
                        RoundedPanel card = createActCard(act, isFavorited);
                        publish(card);
                        if (i++ % 5 == 0)
                            Thread.sleep(1);
                    }
                    return null;
                }

                @Override
                protected void process(List<RoundedPanel> chunks) {
                    for (RoundedPanel card : chunks) {
                        actListPanel.add(card);
                        actListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    }
                    actListPanel.revalidate();
                }

                @Override
                protected void done() {
                    actListPanel.repaint();
                }
            };
            displayWorker.execute();
        }
    }

    public void createBasicSouthPanel() {
        southPanel = new JPanel(new GridLayout(1, 4));
        home = new JButton("首頁");
        addActivity = new JButton("新增");
        following = new JButton("追蹤");
        personalInfo = new JButton("個人");
        btns.addAll(List.of(home, addActivity, following, personalInfo));
        
        // 設置按鈕字體
        Font buttonFont = new Font("Microsoft JhengHei", Font.PLAIN, 18);
        for (JButton btn : btns) {
            btn.setFont(buttonFont);
        }
        
        setBtnStyle();
        southPanel.setPreferredSize(new Dimension(650, 60));
        add(southPanel, BorderLayout.SOUTH);
    }

    private void updateSouthPanelIcons() {
        Map<JButton, String> buttonIconMap = Map.of(
                home, "home",
                addActivity, "add",
                following, "following",
                personalInfo, "person");
        for (Map.Entry<JButton, String> entry : buttonIconMap.entrySet()) {
            Icon icon = iconCache.get(entry.getValue());
            if (icon != null) {
                JButton btn = entry.getKey();
                btn.setIcon(icon);
                btn.setText("");
                btn.setToolTipText(entry.getValue());
            }
        }
    }

    private RoundedPanel createActCard(Activity act, boolean isFavorited) {
        RoundedPanel panel = new RoundedPanel(15);
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(600, 120));
        panel.setMaximumSize(new Dimension(600, 120));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
        panel.setBackground(new Color(246, 220, 135));

        String title = String.format(
                "<html><div style='font-family: Microsoft JhengHei;'><b>%s%s</b><br><br>時間：%s %s<br>地點：%s<div></html>",
                isFavorited ? "★ " : "  ", act.getName(), act.getDate(), act.getTime(), act.getPlace());

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

    public void setBtnStyle() {
        for (JButton btn : btns) {
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(true);
            btn.setFocusPainted(false);
            btn.setBackground(normalColor);
            btn.getModel().addChangeListener(e -> {
                ButtonModel model = btn.getModel();
                btn.setBackground(model.isPressed() ? pressedColor : normalColor);
            });
            southPanel.add(btn);
        }
    }

    public void refreshActivityList() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCacheUpdate < CACHE_DURATION) {
            displayActivities();
            return;
        }

        actListPanel.removeAll();
        actListPanel.add(new JLabel("重新載入中...", JLabel.CENTER));
        actListPanel.revalidate();
        actListPanel.repaint();

        SwingWorker<Void, Void> refreshWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                cachedActivities = DBUtil.getAllActivities();
                String userId = ActivityDetailFrame.getCurrentUserId();
                cachedFavoriteIds = (userId != null) ? DBUtil.getFavoriteActivityIds(userId) : new HashSet<>();
                lastCacheUpdate = System.currentTimeMillis();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    displayActivities();
                } catch (Exception e) {
                    e.printStackTrace();
                    actListPanel.removeAll();
                    actListPanel.add(new JLabel("載入活動失敗：" + e.getMessage()));
                    actListPanel.revalidate();
                    actListPanel.repaint();
                }
            }
        };
        refreshWorker.execute();
    }

    public void setUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("用戶對象不能為空");
        }
        this.user = user;
        // 如果 personalPanel 已經存在，需要更新它
        if (personalPanel != null) {
            centerPanel.remove(personalPanel);
            personalPanel = new PersonalPanel(user);
            centerPanel.add(personalPanel, "my");
        }
        refreshActivityList(); // 若登入後要重新刷新活動列表
    }

}
