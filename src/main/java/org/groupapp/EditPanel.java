package org.groupapp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.List;
import javax.swing.*;

public class EditPanel extends JPanel {

    private CardLayout cardLayout = new CardLayout();
    private BorderLayout borderLayout = new BorderLayout();
    private JPanel cardLayoutPanel, basicInformation, actIntro, settings;
    private String currentCard;
    private java.util.List<String> cardNames = List.of("BasicInformation", "ActIntro", "Settings");
    private int currentIndex = 0;

    private JButton cancel, saveDraft, back, next, confirm;

    public EditPanel() {
        createEditPanel();
        createLayout();
    }

    // 建立編輯模式的畫面
    public final void createEditPanel() {

        cancel = new JButton("cancel");
        saveDraft = new JButton("save as a draft");
        back = new JButton("back");
        next = new JButton("next");
        confirm = new JButton("confirm");
    
        confirm.addActionListener(e -> {
            try (java.sql.Connection conn = DBUtil.getConnection()) {
                String sql = "INSERT INTO activity (name, date, time, place, price, limit_people, due_date, due_time, intro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
    
                // 向子元件索取資料
                BasicInformation info = (BasicInformation) basicInformation;
                ActIntro intro = (ActIntro) actIntro;
                Settings set = (Settings) settings;
    
                stmt.setString(1, info.getName());
                stmt.setString(2, info.getDate());
                stmt.setString(3, info.getTime());
                stmt.setString(4, info.getPlace());
                stmt.setString(5, info.getPrice());
                stmt.setInt(6, info.getLimitPeople());
                stmt.setString(7, set.getDueDate());
                stmt.setString(8, set.getDueTime());
                stmt.setString(9, intro.getIntro());
    
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "活動成功新增到資料庫！");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "資料庫新增失敗：" + ex.getMessage());
            }
        });
    }
    

    public JPanel createCardLayoutPanel() {
        cardLayoutPanel = new JPanel();
        basicInformation = new BasicInformation();
        actIntro = new ActIntro();
        settings = new Settings();

        cardLayoutPanel.setLayout(cardLayout);

        // createBasicInformationPanel();
        cardLayoutPanel.add(basicInformation, "BasicInformation");

        // createActIntroPanel();
        cardLayoutPanel.add(actIntro, "ActIntro");

        // createActSettingsPanel();
        cardLayoutPanel.add(settings, "Settings");

        cardLayout.show(cardLayoutPanel, "basicInformation");

        return cardLayoutPanel;
    }

    public void createLayout() {
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northPanel.add(cancel);
        northPanel.add(Box.createHorizontalGlue());
        northPanel.add(saveDraft);
        add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        southPanel.add(back);
        back.setVisible(false);
        southPanel.add(next);
        southPanel.add(confirm);
        confirm.setVisible(false);
        add(southPanel, BorderLayout.SOUTH);

        add(createCardLayoutPanel(), BorderLayout.CENTER);

        next.addActionListener(e -> {
            if (currentIndex < cardNames.size() - 1) {
                currentIndex++;
                cardLayout.show(cardLayoutPanel, cardNames.get(currentIndex));
                updateButtonVisibility();
            }
        });

        back.addActionListener(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                cardLayout.show(cardLayoutPanel, cardNames.get(currentIndex));
                updateButtonVisibility();
            }
        });
    }

    private void updateButtonVisibility() {
        back.setVisible(currentIndex != 0);
        next.setVisible(currentIndex != cardNames.size() - 1);
        confirm.setVisible(currentIndex == cardNames.size() - 1);
    }

}
