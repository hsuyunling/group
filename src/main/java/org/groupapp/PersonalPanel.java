package org.groupapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.net.URI;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class PersonalPanel extends JPanel {

    private JPanel panel, namePanel, emailPanel, phonePanel, IDPanel, genderPanel, radioPanel, imagePanel, topPanel, midPanel, bottomPanel;
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
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        image();

        panel = new RoundedPanel(15);
        panel.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        IDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

// Panel -> top/ middle/ bottom

        // topPanel -> name
        labelName = new JLabel(user.getName());
        labelName.setFont(new Font("微軟正黑體", Font.BOLD, 25));
        labelName.setAlignmentX(CENTER_ALIGNMENT);
        topPanel.add(labelName);

        // midPanel -> label "個人資訊"/ edit btn
        // bottomPanel -> emailLabel tf/ phoneLabel tf/ IDLabel genderLabel tf tf





        labelEmail = new JLabel("email：" + user.getEmail());
        emailPanel.add(labelEmail);
        labelPhone = new JLabel("手機號碼：" + user.getPhone());
        phonePanel.add(labelPhone);
        labelID = new JLabel("ID：" + user.getId());
        IDPanel.add(labelID);

        bottomPanel.add(emailPanel);
        bottomPanel.add(phonePanel);
        bottomPanel.add(IDPanel);
        bottomPanel.add(genderPanel);
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
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.black));
            imagePanel = new JPanel();
            imagePanel.add(Box.createGlue());
            imagePanel.add(imageLabel);
            imagePanel.add(Box.createGlue());

            add(imagePanel, BorderLayout.NORTH);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
