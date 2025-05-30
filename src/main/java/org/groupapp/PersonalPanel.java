package org.groupapp;

import java.net.URI;
import java.net.URL;
import java.awt.*;
import javax.swing.*;

public class PersonalPanel extends JPanel {

    private JPanel panel, namePanel, emailPanel, phonePanel, IDPanel, genderPanel, radioPanel, imagePanel;
    private JLabel labelName, labelEmail, labelPhone, labelID, labelGender, imageLabel;
    private JRadioButton femaleButton, maleButton, nonButton;

    User user;
    DBUtil db = new DBUtil();
    String imageURL = "";

    // Consturctor
    public PersonalPanel(User user) {
        this.user = user;
        createPanel();
    }

    public void createPanel() {
        image();

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

    private void createGenderButton() {
        maleButton = new JRadioButton("男");
        femaleButton = new JRadioButton("女");
        nonButton = new JRadioButton("不透漏");

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
            if (maleButton.isSelected()) {
                db.execute("男");
            } else if (femaleButton.isSelected()) {
                db.execute("女");
            } else if (nonButton.isSelected()) {
                db.execute("不透漏");
            }
        });

        genderPanel.add(radioPanel);
    }

    // 照片
    public void image() {
        if (user.getGender() == null) {
            imageURL = "https://thumb.ac-illust.com/83/83424bf45d0570a09649ac394b40e118_w.jpeg";
        } else if (user.getGender().equals("男")) {
            imageURL = "https://drive.google.com/uc?export=view&id=1INQQcd4E1C3foBaHji2EPNvZZTsliIAP";
        } else if (user.getGender().equals("女")) {
            imageURL = "https://drive.google.com/uc?export=view&id=1kLjMKDJvSdkwUaI59Ccy_6TYaOjdR2CM";
        } else if (user.getGender().equals("不透漏")) {
            imageURL = "https://images.icon-icons.com/3037/PNG/512/agender_gender_genderless_no_gender_genderqueer_icon_189179.png";
        } else
            imageURL = "https://media.istockphoto.com/id/584764738/zh/%E5%90%91%E9%87%8F/realistic-full-moon.jpg?s=1024x1024&w=is&k=20&c=G0ftKdRiSC11e6xWBtEV6idQeaIOE_r7hBi14fIuCJA=";

        try {

            URI uri = new URI(imageURL);
            URL url = uri.toURL();
            ImageIcon imageIcon = new ImageIcon(url);

            // 200x200
            Image image = imageIcon.getImage();
            Image newimg = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(newimg);

            imageLabel = new JLabel(imageIcon);
            imagePanel = new JPanel(new BorderLayout());
            imagePanel.add(imageLabel, BorderLayout.CENTER);
            add(imagePanel, BorderLayout.NORTH);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
