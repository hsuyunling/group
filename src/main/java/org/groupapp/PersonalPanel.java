package org.groupapp;

import java.awt.*;
import javax.swing.*;

public class PersonalPanel extends JPanel {

    private CardLayout cardLayout = new CardLayout(); // 建立 CardLoyout
    private JPanel cardPanel = new JPanel(cardLayout); // cardLayout容器
    private JPanel infoPanel, editPanel;
    private User user;

    // Consturctor
    public PersonalPanel(User user) {
        this.user = user;
        setLayout(new GridLayout(2, 1));
        infoPanel = new PersonInfo(user, this);
        editPanel = new EditPersonalPanel(user, this);

        cardPanel.add(infoPanel, "info");
        cardPanel.add(editPanel, "edit");

        add(cardPanel, BorderLayout.CENTER);
    }

    public void showInfo() {
        remove(infoPanel);
        infoPanel = new PersonInfo(user, this);
        cardPanel.add(infoPanel, "info");
        cardLayout.show(cardPanel, "info");
    }

    public void showEdit() {
        cardLayout.show(cardPanel, "edit");
    }

}
