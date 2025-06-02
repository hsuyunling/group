package org.groupapp;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import javax.swing.*;

public class PersonalPanel extends JPanel {

    private CardLayout cardLayout = new CardLayout(); // 用於切換 info/edit 畫面
    private JPanel cardPanel = new JPanel(cardLayout);
    private JPanel infoPanel, editPanel;

    // 以下為 infoPanel 內部元件（layout 分支樣式）
    private JPanel panel, namePanel, emailPanel, phonePanel, IDPanel, genderPanel, radioPanel, imagePanel, topPanel, midPanel, bottomPanel;
    private JLabel labelName, labelEmail, labelPhone, labelID, labelGender, imageLabel;
    private JRadioButton femaleButton, maleButton, nonButton;
    private String imageURL;

    private final User user;

    // Constructor
    public PersonalPanel(User user) {
        if (user == null) {
            throw new IllegalArgumentException("用戶對象不能為空");
        }
        this.user = user;

        setLayout(new BorderLayout());

        // 建立 info 畫面與 edit 畫面
        infoPanel = createInfoPanel(); // layout 分支寫法
        editPanel = new EditPersonalPanel(user, this); // 來自 main 分支

        // 設定卡片切換容器
        cardPanel.add(infoPanel, "info");
        cardPanel.add(editPanel, "edit");

        add(cardPanel, BorderLayout.CENTER);
    }

    // 建立 info 畫面（原 layout 分支設計）
    private JPanel createInfoPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));

        // 大頭照部分
        imagePanel = new JPanel();
        imageLabel = createImageLabel();
        imagePanel.add(imageLabel);
        container.add(imagePanel, BorderLayout.NORTH);

        // 個人資料卡片
        panel = new RoundedPanel(15);
        panel.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        IDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        topPanel = new JPanel();
        labelName = new JLabel(user.getName());
        labelName.setFont(new Font("微軟正黑體", Font.BOLD, 25));
        labelName.setAlignmentX(CENTER_ALIGNMENT);
        topPanel.add(labelName);

        // 個人資訊欄位
        labelEmail = new JLabel("email：" + user.getEmail());
        labelPhone = new JLabel("手機號碼：" + user.getPhone());
        labelID = new JLabel("ID：" + user.getId());

        emailPanel.add(labelEmail);
        phonePanel.add(labelPhone);
        IDPanel.add(labelID);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(emailPanel);
        bottomPanel.add(phonePanel);
        bottomPanel.add(IDPanel);

        if (user.getGender() == null) {
            labelGender = new JLabel("性別：");
            genderPanel.add(labelGender);
            createGenderButton();
        } else {
            labelGender = new JLabel("性別：" + user.getGender());
            genderPanel.add(labelGender);
        }
        bottomPanel.add(genderPanel);

        panel.add(topPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(bottomPanel);
        container.add(panel, BorderLayout.CENTER);

        return container;
    }

    // 建立大頭貼圖片元件
    private JLabel createImageLabel() {
        try {
            if (user.getGender() == null) {
                imageURL = "https://thumb.ac-illust.com/83/83424bf45d0570a09649ac394b40e118_w.jpeg";
            } else if (user.getGender().equals("男")) {
                imageURL = "https://drive.google.com/uc?export=view&id=1INQQcd4E1C3foBaHji2EPNvZZTsliIAP";
            } else if (user.getGender().equals("女")) {
                imageURL = "https://drive.google.com/uc?export=view&id=1kLjMKDJvSdkwUaI59Ccy_6TYaOjdR2CM";
            } else if (user.getGender().equals("不透漏")) {
                imageURL = "https://images.icon-icons.com/3037/PNG/512/agender_gender_genderless_no_gender_genderqueer_icon_189179.png";
            } else {
                imageURL = "https://media.istockphoto.com/id/584764738/zh/%E5%90%91%E9%87%8F/realistic-full-moon.jpg?s=1024x1024&w=is&k=20&c=G0ftKdRiSC11e6xWBtEV6idQeaIOE_r7hBi14fIuCJA=";
            }

            URI uri = new URI(imageURL);
            URL url = uri.toURL();
            ImageIcon imageIcon = new ImageIcon(url);
            Image image = imageIcon.getImage();
            Image newimg = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(newimg);

            JLabel label = new JLabel(imageIcon);
            label.setBorder(BorderFactory.createLineBorder(Color.black));
            return label;
        } catch (Exception e) {
            e.printStackTrace();
            return new JLabel("圖片載入失敗");
        }
    }

    // 建立性別選項按鈕（若性別尚未設定）
    private void createGenderButton() {
        femaleButton = new JRadioButton("女");
        maleButton = new JRadioButton("男");
        nonButton = new JRadioButton("不透漏");

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(femaleButton);
        genderGroup.add(maleButton);
        genderGroup.add(nonButton);

        radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(femaleButton);
        radioPanel.add(maleButton);
        radioPanel.add(nonButton);
        genderPanel.add(radioPanel);
    }

    // 顯示 info 畫面（重新生成以反映變動）
    public void showInfo() {
        cardPanel.remove(infoPanel);
        infoPanel = createInfoPanel();
        cardPanel.add(infoPanel, "info");
        cardLayout.show(cardPanel, "info");
    }

    // 顯示 edit 畫面
    public void showEdit() {
        cardLayout.show(cardPanel, "edit");
    }
}
