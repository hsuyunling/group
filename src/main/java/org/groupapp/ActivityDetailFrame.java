package org.groupapp;

import javax.swing.*;
import java.awt.*;
import java.util.Set;
import java.util.prefs.Preferences;

public class ActivityDetailFrame extends JFrame {
    private boolean isFavorited;

    public ActivityDetailFrame(int activityId, String userId) {
        Activity act = DBUtil.getActivityById(activityId);
        if (act == null) {
            JOptionPane.showMessageDialog(null, "查無此活動");
            dispose();
            return;
        }

        setTitle("活動詳情");
        setSize(450, 380);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 上方：標題 + 分享
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("活動名稱：" + act.getName());
        title.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(title, BorderLayout.WEST);

        JPanel circlePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        circlePanel.add(new JLabel("share"));
        circlePanel.add(new JLabel("◯"));  // 模擬圓圈
        topPanel.add(circlePanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 中間：資訊內容
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        infoArea.setText(
                "主辦人：王大明\n" +
                "時間：" + act.getDate() + " " + act.getTime() + "\n" +
                "地點：" + act.getPlace() + "\n" +
                "報名截止：" + act.getDueDate() + " " + act.getDueTime() + "\n\n" +
                "簡介：" + (act.getIntro() == null ? "無提供" : act.getIntro())
        );
        add(new JScrollPane(infoArea), BorderLayout.CENTER);

        // 下方按鈕區
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        JButton btnFav = new JButton();
        JButton btnJoin = new JButton("加入");

        Set<Integer> favoriteIds = DBUtil.getFavoriteActivityIds(userId);
        isFavorited = favoriteIds.contains(act.getId());
        btnFav.setText(isFavorited ? "取消收藏" : "收藏");

        btnFav.addActionListener(e -> {
            if (isFavorited) {
                boolean removed = DBUtil.removeFavorite(userId, act.getId());
                if (removed) {
                    btnFav.setText("收藏");
                    isFavorited = false;
                    JOptionPane.showMessageDialog(this, "已取消收藏");
                } else {
                    JOptionPane.showMessageDialog(this, "取消收藏失敗");
                }
            } else {
                boolean added = DBUtil.addFavorite(userId, act.getId());
                if (added) {
                    btnFav.setText("取消收藏");
                    isFavorited = true;
                    JOptionPane.showMessageDialog(this, "已加入收藏");
                } else {
                    JOptionPane.showMessageDialog(this, "收藏失敗或已收藏過");
                }
            }
        });

        btnJoin.addActionListener(e -> {
            boolean success = DBUtil.registerUserToActivityIfNotExists(userId, act.getId());
            JOptionPane.showMessageDialog(this,
                    success ? "報名成功" : "報名失敗或已報名過");
        });

        bottomPanel.add(btnFav);
        bottomPanel.add(btnJoin);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public static String getCurrentUserId() {
        Preferences prefs = Preferences.userRoot().node("org.groupapp");
        return prefs.get("userId", null);
    }
}
