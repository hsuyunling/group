package org.groupapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ActivityDetailFrame extends JFrame {
    private boolean isFavorited;
    private HomePage parent;
    Color normalColor = new Color(246, 209, 86);
    Color pressedColor = new Color(195, 170, 87);

    public ActivityDetailFrame(int activityId, String userId, HomePage parent) {
        this.parent = parent;
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
        JLabel title = new JLabel(act.getName());
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 6));
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.setBackground(new Color(246, 220, 135));
        topPanel.add(title, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        // 中間：資訊內容
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        infoArea.setText(
                "主辦人：" + (act.getHostName() == null ? "未知" : act.getHostName()) + "\n" +
                        "時間：" + act.getDate() + " " + act.getTime() + "\n" +
                        "地點：" + act.getPlace() + "\n" +
                        "報名截止：" + act.getDueDate() + " " + act.getDueTime() + "\n\n" +
                        "簡介：" + (act.getIntro() == null ? "無提供" : act.getIntro()));
        infoArea.setBorder(BorderFactory.createEmptyBorder(5, 7, 0, 7));
        add(new JScrollPane(infoArea), BorderLayout.CENTER);

        // 下方按鈕區
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        JButton btnFav = new JButton();
        JButton btnJoin = new JButton("加入");

        setBtnStyle(btnFav);
        setBtnStyle(btnJoin);

        Set<Integer> favoriteIds = DBUtil.getFavoriteActivityIds(userId);
        isFavorited = favoriteIds.contains(act.getId());
        btnFav.setText(isFavorited ? "取消收藏" : "收藏");

        btnFav.addActionListener(e -> {
            boolean changed = false;
            if (isFavorited) {
                boolean removed = DBUtil.removeFavorite(userId, act.getId());
                if (removed) {
                    btnFav.setText("收藏");
                    isFavorited = false;
                    changed = true;
                    JOptionPane.showMessageDialog(this, "已取消收藏");
                } else {
                    JOptionPane.showMessageDialog(this, "取消收藏失敗");
                }
            } else {
                boolean added = DBUtil.addFavorite(userId, act.getId());
                if (added) {
                    btnFav.setText("取消收藏");
                    isFavorited = true;
                    changed = true;
                    JOptionPane.showMessageDialog(this, "已加入收藏");
                } else {
                    JOptionPane.showMessageDialog(this, "收藏失敗或已收藏過");
                }
            }

            // ★ 若狀態改變就刷新主畫面
            if (changed && parent != null) {
                parent.refreshActivityList();
            }
        });

        btnJoin.addActionListener(e -> {
            boolean success = DBUtil.registerUserToActivityIfNotExists(userId, act.getId());
            JOptionPane.showMessageDialog(this,
                    success ? "報名成功" : "報名失敗或已報名過");
        });

        bottomPanel.add(btnFav);
        bottomPanel.add(Box.createVerticalStrut(2));
        bottomPanel.add(btnJoin);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public static String getCurrentUserId() {
        Preferences prefs = Preferences.userRoot().node("org.groupapp");
        return prefs.get("userId", null);
    }

    public void setBtnStyle(JButton thisBtn) {


        thisBtn.setOpaque(true);
        thisBtn.setBorderPainted(false);
        thisBtn.setContentAreaFilled(true);
        thisBtn.setFocusPainted(false);
        thisBtn.setPreferredSize(new Dimension(223, 40));
        thisBtn.setMaximumSize(new Dimension(223, 40));
        thisBtn.setBackground(normalColor);
        thisBtn.setFont(new Font("微軟正黑體", Font.PLAIN, 16));

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
