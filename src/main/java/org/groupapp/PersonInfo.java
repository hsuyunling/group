package org.groupapp;

import java.awt.*;
import javax.swing.*;

public class PersonInfo extends JPanel {

    private JPanel panel, namePanel, emailPanel, phonePanel, IDPanel, genderPanel, imagePanel;
    private JLabel labelName, labelEmail, labelPhone, labelID, labelGender, imageLabel;
    private JButton editBtn;
    private Image defaltImg, hippoImg, boyImg, girlImg, penguinImg;
    private User user;
    private PersonalPanel parent;

    // constucto
    public PersonInfo(User user, PersonalPanel parent) {
        this.user = user;
        this.parent = parent;
        image();
        createPanel();
    }

    // 個人資訊文字的地方
    public void createPanel() {

        panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));
        namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        IDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        editBtn = new JButton("編輯");

        labelName = new JLabel("姓名：" + user.getName());
        namePanel.add(labelName);
        labelEmail = new JLabel("email：" + user.getEmail());
        emailPanel.add(labelEmail);
        labelPhone = new JLabel("手機號碼：" + user.getPhone());
        phonePanel.add(labelPhone);
        labelID = new JLabel("ID：" + user.getId());
        IDPanel.add(labelID);

        if (user.getGender() == null) {
            labelGender = new JLabel("性別：未知");
        } else {
            labelGender = new JLabel("性別：" + user.getGender());
        }
        genderPanel.add(labelGender);

        // 編輯按鈕
        editBtn.addActionListener(e -> {
            parent.showEdit();
        });
        JPanel btnPanel = new JPanel();
        btnPanel.add(editBtn);

        panel.add(namePanel);
        panel.add(emailPanel);
        panel.add(phonePanel);
        panel.add(IDPanel);
        panel.add(genderPanel);
        panel.add(btnPanel);
        add(panel, BorderLayout.CENTER);
    }

    // 照片
    public void image() {
        defaltImg = new ImageIcon(getClass().getResource("/penguin.png"))
                .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        hippoImg = new ImageIcon(getClass().getResource("/hippo.png"))
                .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        boyImg = new ImageIcon(getClass().getResource("/boy.png"))
                .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        girlImg = new ImageIcon(getClass().getResource("/girl.png"))
                .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        penguinImg = new ImageIcon(getClass().getResource("/penguin.png"))
                .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);

        Icon defaultIcon = new ImageIcon(defaltImg);
        Icon hippoIcon = new ImageIcon(hippoImg);
        Icon boyIcon = new ImageIcon(boyImg);
        Icon girlIcon = new ImageIcon(girlImg);
        Icon penguinIcon = new ImageIcon(penguinImg);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        if (user.getGender() == null) {
            imageLabel.setIcon(defaultIcon);
        } else if (user.getGender().equals("男")) {
            imageLabel.setIcon(boyIcon);
        } else if (user.getGender().equals("女")) {
            imageLabel.setIcon(girlIcon);
        } else if (user.getGender().equals("不透露")) {
            imageLabel.setIcon(hippoIcon);
        } else {
            imageLabel.setIcon(penguinIcon);
        }

        imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        add(imagePanel, BorderLayout.NORTH);
    }

    public void update() {

    }
}
