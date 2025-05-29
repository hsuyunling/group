/*package org.groupapp;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.ArrayList;

public class KeepPage extends JFrame {

	private static final int FRAME_WIDTH = 650;
	private static final int FRAME_HEIGHT = 750;
	String actName;
	String place;
	int price;
	int maxParticipants;
	int currentParticipants;
	Date startDate;
	Date dueDate;
	
	public KeepPage() {
    	
	   	// set frame
		// add example
		setTitle("收藏頁面");
	        setSize(FRAME_WIDTH, FRAME_HEIGHT);
	        setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setVisible(true);
		
	        JPanel activityPanel = new JPanel();
	        activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));
	
	        JScrollPane scrollPane = new JScrollPane(activityPanel);
	        add(scrollPane, BorderLayout.CENTER);
	}
}*/
package org.groupapp;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class KeepPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private String userId;

    // 三個分類面板
    private JPanel eventsContent, expiredContent, doneContent;
    private JLabel eventsArrow, expiredArrow, doneArrow;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public KeepPanel() {
        setLayout(new BorderLayout());
        userId = ActivityDetailFrame.getCurrentUserId();
        if (userId == null) {
            add(new JLabel("尚未登入"), BorderLayout.CENTER);
            return;
        }

        tabbedPane = new JTabbedPane();
        reload();
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void reload() {
        tabbedPane.removeAll();

        // ---------- 已報名 ----------
        JPanel joinedPanel = new JPanel();
        joinedPanel.setLayout(new BoxLayout(joinedPanel, BoxLayout.Y_AXIS));
        List<Activity> joinedActivities = DBUtil.getRegisteredActivities(userId);
        if (joinedActivities.isEmpty()) {
            joinedPanel.add(new JLabel("你尚未報名任何活動"));
        } else {
            for (Activity act : joinedActivities) {
                joinedPanel.add(createActCard(act, true, userId, this::reload));
                joinedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        tabbedPane.add("已報名", new JScrollPane(joinedPanel));

        // ---------- 已收藏 (分類 & 折疊) ----------
        JPanel favTab = new JPanel();
        favTab.setLayout(new BoxLayout(favTab, BoxLayout.Y_AXIS));
        favTab.setBorder(new EmptyBorder(10,10,10,10));

        eventsContent  = new JPanel(); eventsContent .setLayout(new BoxLayout(eventsContent,  BoxLayout.Y_AXIS));
        expiredContent = new JPanel(); expiredContent.setLayout(new BoxLayout(expiredContent, BoxLayout.Y_AXIS));
        doneContent    = new JPanel(); doneContent   .setLayout(new BoxLayout(doneContent,    BoxLayout.Y_AXIS));

        buildSection(favTab, "Events",  eventsContent);
        buildSection(favTab, "Expired", expiredContent);
        buildSection(favTab, "Done",    doneContent);

        fillFavorites(userId);

        tabbedPane.add("已收藏", new JScrollPane(favTab));
    }

    /** 建立可折疊區塊 header + content */
    private void buildSection(JPanel parent, String title, JPanel content) {
        JPanel header = new JPanel(new BorderLayout());
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        header.setBorder(new EmptyBorder(5,0,5,0));
        header.setOpaque(true);

        // Expired & Done 灰底
        if ("Expired".equals(title) || "Done".equals(title)) {
            header.setBackground(Color.LIGHT_GRAY);
            content.setBackground(Color.LIGHT_GRAY);
            content.setOpaque(true);
        }

        JLabel arrow = new JLabel("\u25BC"); // ▼
        arrow.setFont(new Font("Arial", Font.BOLD, 14));
        arrow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));

        header.add(arrow, BorderLayout.WEST);
        header.add(lbl,   BorderLayout.CENTER);
        parent.add(header);
        parent.add(content);

        arrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean shown = content.isVisible();
                content.setVisible(!shown);
                arrow.setText(shown ? "\u25B2" : "\u25BC"); // ▲ or ▼
                parent.revalidate();
            }
        });

        switch (title) {
            case "Events":  eventsArrow  = arrow; break;
            case "Expired": expiredArrow = arrow; break;
            case "Done":    doneArrow    = arrow; break;
        }
    }

    /** 填入收藏資料並分類 */
    private void fillFavorites(String userId) {
        Set<Integer> favIds = DBUtil.getFavoriteActivityIds(userId);
        LocalDate today = LocalDate.now();
        for (Integer aid : favIds) {
            Activity act = DBUtil.getActivityById(aid);
            if (act == null) continue;

            LocalDate due   = parseDate(act.getDueDate());
            LocalDate event = parseDate(act.getDate());
            JPanel card = createActCard(act, false, userId, this::reload);

            if (due != null && !due.isBefore(today)) {
                eventsContent.add(card);
            } else if (due != null && due.isBefore(today)
                    && event != null && !event.isBefore(today)) {
                expiredContent.add(card);
            } else if (event != null && event.isBefore(today)) {
                doneContent.add(card);
            }
            // 間隔
            eventsContent.add(Box.createRigidArea(new Dimension(0,5)));
        }
    }

    /** 安全解析 yyyy-MM-dd */
    private LocalDate parseDate(String s) {
        try {
            return LocalDate.parse(s, DATE_FMT);
        } catch (Exception e) {
            return null;
        }
    }

    /** 複用原本的卡片建立 */
    private JPanel createActCard(Activity act, boolean showCancelBtn, String userId, Runnable onChange) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(550, 90));
        panel.setMaximumSize(new Dimension(550, 90));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel label = new JLabel(String.format(
                "<html><b>%s</b><br>時間：%s %s｜地點：%s</html>",
                act.getName(), act.getDate(), act.getTime(), act.getPlace()));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(label, BorderLayout.CENTER);

        if (showCancelBtn) {
            JButton cancelBtn = new JButton("取消報名");
            cancelBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(panel, "確定要取消報名？", "確認", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean removed = DBUtil.cancelRegistration(userId, act.getId());
                    if (removed) {
                        JOptionPane.showMessageDialog(panel, "已取消報名");
                        onChange.run();
                    } else {
                        JOptionPane.showMessageDialog(panel, "取消失敗");
                    }
                }
            });
            panel.add(cancelBtn, BorderLayout.EAST);
        } else {
            JButton joinBtn = new JButton(DBUtil.isUserRegistered(userId, act.getId()) ? "已報名" : "報名");
            joinBtn.setEnabled(!DBUtil.isUserRegistered(userId, act.getId()));
            joinBtn.addActionListener(e -> {
                boolean success = DBUtil.registerUserToActivityIfNotExists(userId, act.getId());
                if (success) {
                    JOptionPane.showMessageDialog(panel, "報名成功");
                    joinBtn.setText("已報名");
                    joinBtn.setEnabled(false);
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

