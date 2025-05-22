package org.groupapp;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class FollowingPanel extends JPanel {
    public FollowingPanel() {
        setLayout(new BorderLayout());

        String userId = ActivityDetailFrame.getCurrentUserId();
        if (userId == null) {
            add(new JLabel("尚未登入"), BorderLayout.CENTER);
            return;
        }

        JTabbedPane tabbedPane = new JTabbedPane();

        // 已報名活動
        JPanel joinedPanel = new JPanel();
        joinedPanel.setLayout(new BoxLayout(joinedPanel, BoxLayout.Y_AXIS));
        List<Activity> joinedActivities = DBUtil.getRegisteredActivities(userId);
        if (joinedActivities.isEmpty()) {
            joinedPanel.add(new JLabel("你尚未報名任何活動"));
        } else {
            for (Activity act : joinedActivities) {
                joinedPanel.add(createActCard(act));
                joinedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        // 已收藏活動
        JPanel favoritePanel = new JPanel();
        favoritePanel.setLayout(new BoxLayout(favoritePanel, BoxLayout.Y_AXIS));
        Set<Integer> favIds = DBUtil.getFavoriteActivityIds(userId);
        List<Activity> all = DBUtil.getAllActivities();
        boolean hasFav = false;
        for (Activity act : all) {
            if (favIds.contains(act.getId())) {
                favoritePanel.add(createActCard(act));
                favoritePanel.add(Box.createRigidArea(new Dimension(0, 10)));
                hasFav = true;
            }
        }
        if (!hasFav) {
            favoritePanel.add(new JLabel("你尚未收藏任何活動"));
        }

        tabbedPane.add("已報名", new JScrollPane(joinedPanel));
        tabbedPane.add("已收藏", new JScrollPane(favoritePanel));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createActCard(Activity act) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(550, 90));
        panel.setMaximumSize(new Dimension(550, 90));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel label = new JLabel(String.format(
            "<html><b>%s</b><br>時間：%s %s｜地點：%s</html>",
            act.getName(), act.getDate(), act.getTime(), act.getPlace()
        ));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
