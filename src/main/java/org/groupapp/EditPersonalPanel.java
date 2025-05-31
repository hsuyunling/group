package org.groupapp;

import java.awt.*;
import javax.swing.*;

public class EditPersonalPanel extends JPanel {
    private final int FIELD_WIDTH = 20;
    private JPanel panel, namePanel, emailPanel, phonePanel, genderPanel, radioPanel, btnPanel;
    private JLabel labelName, labelEmail, labelPhone, labelGender;
    private JTextField textName, textEmail, textPhone;
    private JRadioButton femaleButton, maleButton, nonButton;
    private JButton confirm, back;
    private String selectedGender;
    private User user;
    private DBUtil db = new DBUtil();
    private PersonalPanel parent;

    public EditPersonalPanel(User user, PersonalPanel parent) {
        this.user = user;
        this.parent = parent;
        createPanel();
    }

    // 個人資訊文字的地方
    private void createPanel() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));
        namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // IDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        labelName = new JLabel("姓名：");
        textName = new JTextField(FIELD_WIDTH);
        textName.setText(user.getName());
        namePanel.add(labelName);
        namePanel.add(textName);

        labelEmail = new JLabel("email：");
        textEmail = new JTextField(FIELD_WIDTH);
        textEmail.setText(user.getEmail());
        emailPanel.add(labelEmail);
        emailPanel.add(textEmail);

        labelPhone = new JLabel("手機號碼：");
        textPhone = new JTextField(FIELD_WIDTH);
        textPhone.setText(user.getPhone());
        phonePanel.add(labelPhone);
        phonePanel.add(textPhone);

        /*
         * labelID = new JLabel("ID：");
         * textNumber = new JTextField(FIELD_WIDTH);
         * textNumber.setText(user.getId());
         * IDPanel.add(labelID);
         * IDPanel.add(textNumber);
         */

        labelGender = new JLabel("性別：");
        genderPanel.add(labelGender);
        createGenderButton();

        panel.add(namePanel);
        panel.add(emailPanel);
        panel.add(phonePanel);
        // panel.add(IDPanel);
        panel.add(genderPanel);
        createBtn();

        add(panel, BorderLayout.CENTER);
    }

    // 選擇性別按鈕
    private void createGenderButton() {
        maleButton = new JRadioButton("男");
        femaleButton = new JRadioButton("女");
        nonButton = new JRadioButton("不透露");

        ButtonGroup g = new ButtonGroup();
        radioPanel = new JPanel();
        g.add(maleButton);
        g.add(femaleButton);
        g.add(nonButton);

        // 預設性別
        String gender = user.getGender();
        if ("男".equals(gender)) {
            maleButton.setSelected(true);
        } else if ("女".equals(gender)) {
            femaleButton.setSelected(true);
        } else if ("不透露".equals(gender)) {
            nonButton.setSelected(true);
        }

        radioPanel.add(maleButton);
        radioPanel.add(femaleButton);
        radioPanel.add(nonButton);
        genderPanel.add(radioPanel);
    }

    // 編輯完成、 返回 按鈕
    public void createBtn() {

        confirm = new JButton("確認");
        confirm.addActionListener(e -> {

            selectedGender = null;
            if (maleButton.isSelected())
                selectedGender = "男";
            else if (femaleButton.isSelected())
                selectedGender = "女";
            else if (nonButton.isSelected())
                selectedGender = "不透露";

            // 修改個人資料(user中)
            if (isModify()) {
                user.setName(textName.getText());
                user.setEmail(textEmail.getText());
                user.setPhone(textPhone.getText());
                user.setGender(selectedGender);

                if (db.execute(selectedGender, user.getName()) && db.updateUser(user)) { // 確認資料庫執行成功
                    JOptionPane.showMessageDialog(null, "更改成功", "提示", JOptionPane.PLAIN_MESSAGE);

                } else {
                    JOptionPane.showMessageDialog(null, "失敗", "提示", JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "未有資料更動", "提示", JOptionPane.PLAIN_MESSAGE);
            }
            parent.showInfo();
        });

        back = new JButton("返回");
        back.addActionListener(e -> {
            parent.showInfo();
        });

        confirm.setPreferredSize(new Dimension(100, 40));
        back.setPreferredSize(new Dimension(100, 40));
        btnPanel = new JPanel();
        btnPanel.add(confirm);
        btnPanel.add(back);
        panel.add(btnPanel);
    }

    // 檢查資料更改
    public boolean isModify() {
        if (!textName.getText().equals(user.getName()))
            return true;
        if (!textEmail.getText().equals(user.getEmail()))
            return true;
        if (!textPhone.getText().equals(user.getPhone()))
            return true;

        if (!selectedGender.equals(user.getGender())) {
            return true;
        } else {
            return false;
        }
    }

}
