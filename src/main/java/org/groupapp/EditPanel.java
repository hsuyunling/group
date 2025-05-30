package org.groupapp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class EditPanel extends JPanel {

    private CardLayout cardLayout = new CardLayout();
    private BorderLayout borderLayout = new BorderLayout();
    private JPanel cardLayoutPanel, basicInformation, actIntro, settings;
    private String currentCard;
    private java.util.List<String> cardNames = List.of("BasicInformation", "ActIntro");
    private int currentIndex = 0;

    private JButton back, next, confirm;
    ArrayList<JButton> btns = new ArrayList<>();

    public EditPanel() {
        createEditPanel();
        createLayout();
    }

    // ------------------建立編輯模式的畫面------------------
    public final void createEditPanel() {

        back = new JButton("back");
        next = new JButton("next");
        confirm = new JButton("confirm");

        confirm.addActionListener(e -> {
            try {
                BasicInformation info = (BasicInformation) basicInformation;
                ActIntro intro = (ActIntro) actIntro;

                Activity act = new Activity();
                act.setName(info.getName());
                act.setDate(info.getDate());
                act.setTime(info.getTime());
                act.setPlace(info.getPlace()); // 如果這行丟例外 → 就會被 catch 到
                act.setPrice(info.getPrice());
                act.setLimitPeople(info.getLimitPeople());
                act.setDueDate(info.getDueDate());
                act.setDueTime(info.getDueTime());
                act.setIntro(intro.getIntro());

                boolean success = DBUtil.addActivity(act);
                if (success) {
                    JOptionPane.showMessageDialog(this, "活動成功新增到資料庫！");
                } else {
                    JOptionPane.showMessageDialog(this, "資料庫新增失敗，請確認登入狀態或欄位資料。");
                }

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "輸入錯誤", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace(); // 保留 debug
                JOptionPane.showMessageDialog(this, "發生錯誤：" + ex.getMessage(), "系統錯誤", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    // ------------------建立切換畫面------------------
    public JPanel createCardLayoutPanel() {
        cardLayoutPanel = new JPanel();
        basicInformation = new BasicInformation();
        actIntro = new ActIntro();

        cardLayoutPanel.setLayout(cardLayout);

        // createBasicInformationPanel();
        cardLayoutPanel.add(basicInformation, "BasicInformation");

        // createActIntroPanel();
        cardLayoutPanel.add(actIntro, "ActIntro");

        cardLayout.show(cardLayoutPanel, "basicInformation");

        return cardLayoutPanel;
    }

    public void createLayout() {
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northPanel.add(Box.createHorizontalGlue());
        add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        southPanel.add(back);
        back.setVisible(false);
        southPanel.add(next);
        southPanel.add(confirm);
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

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

        setBtnStyle();

    }

    private void updateButtonVisibility() {
        back.setVisible(currentIndex != 0);
        next.setVisible(currentIndex != cardNames.size() - 1);
        confirm.setVisible(currentIndex == cardNames.size() - 1);
    }

    public void setBtnStyle() {
        Color normalColor = new Color(246, 209, 86);
        Color pressedColor = new Color(195, 170, 87);
        btns.add(next);
        btns.add(back);
        btns.add(confirm);
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
        }
    }
}
