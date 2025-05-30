package org.groupapp;

import java.net.URI;
import java.net.URL;
import java.awt.*;
import javax.swing.*;

public class PersonalPanel extends JPanel {

    private JPanel panel, namePanel, emailPanel, phonePanel, IDPanel, genderPanel, radioPanel, imagePanel;
    private JLabel labelName, labelEmail, labelPhone, labelID, labelGender, imageLabel;
    private JRadioButton femaleButton, maleButton, nonButton;
    private JButton editBtn;
    private Image defaltImg, hippoImg, boyImg, girlImg, penguinImg;

    User user;
    DBUtil db = new DBUtil();
    String imageURL = "";

    // Consturctor
    public PersonalPanel(User user) {
        this.user = user;
        setLayout(new GridLayout(2, 1));
        image();
        createPanel();
    }

    // 個人資訊文字的地方
    public void createPanel() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));
        namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        IDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        labelName = new JLabel("姓名：" + user.getName());
        namePanel.add(labelName);
        labelEmail = new JLabel("email：" + user.getEmail());
        emailPanel.add(labelEmail);
        labelPhone = new JLabel("手機號碼：" + user.getPhone());
        phonePanel.add(labelPhone);
        labelID = new JLabel("ID：" + user.getId());
        IDPanel.add(labelID);

        panel.add(namePanel);
        panel.add(emailPanel);
        panel.add(phonePanel);
        panel.add(IDPanel);
        panel.add(genderPanel);
        if (user.getGender() == null) {
            labelGender = new JLabel("性別：");
            genderPanel.add(labelGender);
            createGenderButton();
        } else {
            labelGender = new JLabel("性別：" + user.getGender());
            genderPanel.add(labelGender);
        }
        add(panel, BorderLayout.CENTER);
    }

    // 性別選擇按鈕
    private void createGenderButton() {

        maleButton = new JRadioButton("男");
        femaleButton = new JRadioButton("女");
        nonButton = new JRadioButton("不透露");

        ButtonGroup g = new ButtonGroup();
        radioPanel = new JPanel();
        g.add(maleButton);
        g.add(femaleButton);
        g.add(nonButton);

        radioPanel.add(maleButton);
        radioPanel.add(femaleButton);
        radioPanel.add(nonButton);

        JButton confirm = new JButton("確認");
        confirm.addActionListener(e -> {
            String selectGender = null;
            if (maleButton.isSelected()) {
                selectGender = "男";
            } else if (femaleButton.isSelected()) {
                selectGender = "女";
            } else if (nonButton.isSelected()) {
                selectGender = "不透露";
            }

            if (selectGender != null) {
                if (db.execute(selectGender, user.getName())) { // 確認資料庫執行成功
                    user.setGender(selectGender);
                    removeAll();
                    revalidate();
                    repaint();
                    image();
                    createPanel();
                }

            }

        });

        genderPanel.add(radioPanel);
        genderPanel.add(confirm);
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
        } else if (user.getGender().equals("不透漏")) {
            imageLabel.setIcon(hippoIcon);
        } else {
            imageLabel.setIcon(penguinIcon);

        }

        imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        add(imagePanel, BorderLayout.NORTH);
    }

}
