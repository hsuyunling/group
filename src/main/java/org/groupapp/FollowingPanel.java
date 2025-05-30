package org.groupapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 18)); // 改字型

        UIManager.put("TabbedPane.selected", normalColor);          // 選取中的 tab 背景
        UIManager.put("TabbedPane.contentAreaColor", Color.white);   // 內容區背景
        UIManager.put("TabbedPane.background", pressedColor);   // 一般 tab 背景
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));// 上、左、下、右各留 10 像素空間

        
        SwingUtilities.updateComponentTreeUI(tabbedPane); // 讓改變生效


    }

    public void reload() {
        tabbedPane.removeAll(); // 清除所有分頁

        // ---------- 已報名 ----------
        JPanel joinedPanel = new JPanel();
        joinedPanel.setLayout(new BoxLayout(joinedPanel, BoxLayout.Y_AXIS));
        joinedPanel.setBackground(Color.white);
        joinedPanel.add(Box.createVerticalStrut(10));
        List<Activity> joinedActivities = DBUtil.getRegisteredActivities(userId);
        if (joinedActivities.isEmpty()) {
            joinedPanel.add(new JLabel("你尚未報名任何活動"));
        } else {
            for (Activity act : joinedActivities) {
                joinedPanel.add(createActCard(act, true, userId, this::reload));
                joinedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        // ---------- 已收藏 ----------
        JPanel favoritePanel = new JPanel();
        favoritePanel.setLayout(new BoxLayout(favoritePanel, BoxLayout.Y_AXIS));
        favoritePanel.setBackground(Color.white);
        favoritePanel.add(Box.createVerticalStrut(10));
        Set<Integer> favIds = DBUtil.getFavoriteActivityIds(userId);
        List<Activity> all = DBUtil.getAllActivities();
        boolean hasFav = false;
        for (Activity act : all) {
            if (favIds.contains(act.getId())) {
                favoritePanel.add(createActCard(act, false, userId, this::reload));
                favoritePanel.add(Box.createRigidArea(new Dimension(0, 10)));
                hasFav = true;
            }
        }
        if (!hasFav) {
            favoritePanel.add(new JLabel("你尚未收藏任何活動"));
        }

        tabbedPane.add("    已報名    ", new JScrollPane(joinedPanel));
        tabbedPane.add("    已收藏    ", new JScrollPane(favoritePanel));
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
                    joinBtn.setEnabled(false); // 🔒 不能再按
                    if (onChange != null)
                        onChange.run(); // 🔁 讓頁面能更新（例如從收藏頁移除）
                } else {
                    JOptionPane.showMessageDialog(panel, "報名失敗或已報名");
                }
            });

            panel.add(joinBtn, BorderLayout.EAST);
        }

        return panel;
    }

}


