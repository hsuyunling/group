package org.groupapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.Border;

public class FollowingPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private String userId;
    Color normalColor = new Color(246, 209, 86);
    Color pressedColor = new Color(195, 170, 87);

    public FollowingPanel() {
        setLayout(new BorderLayout());
        userId = ActivityDetailFrame.getCurrentUserId();

        if (userId == null) {
            add(new JLabel("尚未登入"), BorderLayout.CENTER);
            return;
        }

        tabbedPane = new JTabbedPane();
        reload(); // 初始載入
        add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 18));

        UIManager.put("TabbedPane.contentAreaColor", Color.white);
        UIManager.put("TabbedPane.background", normalColor);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        SwingUtilities.updateComponentTreeUI(tabbedPane);
    }

    public void reload() {
        tabbedPane.removeAll();

        JPanel joinedPanel = new JPanel();
        joinedPanel.add(new JLabel("載入中..."));
        tabbedPane.add("    已報名    ", new JScrollPane(joinedPanel));

        JPanel favoritePanel = new JPanel();
        favoritePanel.add(new JLabel("載入中..."));
        tabbedPane.add("    已收藏    ", new JScrollPane(favoritePanel));

        new SwingWorker<List<Object>, Void>() {
            @Override
            protected List<Object> doInBackground() throws Exception {
                List<Activity> joinedActivities = DBUtil.getRegisteredActivities(userId);
                Set<Integer> favIds = DBUtil.getFavoriteActivityIds(userId);
                List<Activity> all = DBUtil.getAllActivities();
                return List.of(joinedActivities, favIds, all);
            }

            @Override
            protected void done() {
                try {
                    List<Object> result = get();
                    List<Activity> joinedActivities = (List<Activity>) result.get(0);
                    Set<Integer> favIds = (Set<Integer>) result.get(1);
                    List<Activity> allActivities = (List<Activity>) result.get(2);

                    // 已報名
                    JPanel joined = new JPanel();
                    joined.setLayout(new BoxLayout(joined, BoxLayout.Y_AXIS));
                    joined.setBackground(Color.white);
                    joined.add(Box.createVerticalStrut(10));

                    if (joinedActivities.isEmpty()) {
                        joined.add(new JLabel("你尚未報名任何活動"));
                    } else {
                        for (Activity act : joinedActivities) {
                            joined.add(createActCard(act, true, userId, FollowingPanel.this::reload));
                            joined.add(Box.createRigidArea(new Dimension(0, 10)));
                        }
                    }

                    // 已收藏
                    JPanel favorite = new JPanel();
                    favorite.setLayout(new BoxLayout(favorite, BoxLayout.Y_AXIS));
                    favorite.setBackground(Color.white);
                    favorite.add(Box.createVerticalStrut(10));

                    boolean hasFav = false;
                    for (Activity act : allActivities) {
                        if (favIds.contains(act.getId())) {
                            favorite.add(createActCard(act, false, userId, FollowingPanel.this::reload));
                            favorite.add(Box.createRigidArea(new Dimension(0, 10)));
                            hasFav = true;
                        }
                    }
                    if (!hasFav) {
                        favorite.add(new JLabel("你尚未收藏任何活動"));
                    }

                    tabbedPane.setComponentAt(0, new JScrollPane(joined));
                    tabbedPane.setComponentAt(1, new JScrollPane(favorite));

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(FollowingPanel.this, "資料載入失敗：" + ex.getMessage());
                }
            }
        }.execute();
    }

    private JPanel createActCard(Activity act, boolean showCancelBtn, String userId, Runnable onChange) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(550, 90));
        panel.setMaximumSize(new Dimension(550, 90));

        JLabel label = new JLabel(String.format(
                "<html><b>%s</b><br>時間：%s %s｜地點：%s</html>",
                act.getName(), act.getDate(), act.getTime(), act.getPlace()));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(label, BorderLayout.CENTER);

        if (showCancelBtn) {
            JButton cancelBtn = new JButton("取消報名");
            cancelBtn.setOpaque(true);
            cancelBtn.setBorderPainted(false);
            cancelBtn.setContentAreaFilled(true);
            cancelBtn.setFocusPainted(false);

            cancelBtn.setBackground(normalColor);
            cancelBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(panel, "確定要取消報名？", "確認", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean removed = DBUtil.cancelRegistration(userId, act.getId());
                    if (removed) {
                        JOptionPane.showMessageDialog(panel, "已取消報名");
                        if (onChange != null)
                            onChange.run();
                    } else {
                        JOptionPane.showMessageDialog(panel, "取消失敗");
                    }
                }
            });
            panel.add(cancelBtn, BorderLayout.EAST);
        } else {
            boolean alreadyRegistered = DBUtil.isUserRegistered(userId, act.getId());
            JButton joinBtn = new JButton(alreadyRegistered ? "已報名" : "報名");
            joinBtn.setOpaque(true);
            joinBtn.setBorderPainted(false);
            joinBtn.setContentAreaFilled(true);
            joinBtn.setFocusPainted(false);

            joinBtn.setBackground(normalColor);
            joinBtn.setEnabled(!alreadyRegistered);

            joinBtn.addActionListener(e -> {
                boolean success = DBUtil.registerUserToActivityIfNotExists(userId, act.getId());
                if (success) {
                    JOptionPane.showMessageDialog(panel, "報名成功");
                    joinBtn.setText("已報名");
                    joinBtn.setEnabled(false);
                    if (onChange != null)
                        onChange.run();
                } else {
                    JOptionPane.showMessageDialog(panel, "報名失敗或已報名");
                }
            });

            panel.add(joinBtn, BorderLayout.EAST);
        }

        return panel;
    }
}
