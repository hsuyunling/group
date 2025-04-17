package activity;

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
        northPanel.add(cancel);
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
