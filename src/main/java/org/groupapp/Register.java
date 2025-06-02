package org.groupapp;

import java.awt.*;
import javax.swing.*;

public class Register extends JPanel {
    private final int FIELD_WIDTH = 20;

    private JPanel panel, namePanel, emailPanel, phonePanel, numberPanel, passWordPanel, secondPwdPanel, smallPanel;
    private JLabel labelName, labelEmail, labelPhone, labelNumber, labelPassWord, labelSecondPwd, hintLabel;
    private JTextField textName, textEmail, textPhone, textNumber, textPassWord, textSecondPwd;

    private JButton buttonFin, backbtn;

    private String name, email, phone, number, passWord, pwdSecond;
    DBUtil u = new DBUtil();
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public Register(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        createPanel();
    }

    public void createPanel() {
        setLayout(new BorderLayout());

        panel = new JPanel(new BorderLayout());

        smallPanel = new JPanel(new GridLayout(8, 1, 0, 5));
        smallPanel.setBorder(BorderFactory.createEmptyBorder(70, 140, 0, 140));

        // 學號
        textNumber = new JTextField(FIELD_WIDTH);
        numberPanel = new JPanel(new BorderLayout());
        labelNumber = new JLabel("學號：");
        numberPanel.add(labelNumber, BorderLayout.WEST);
        numberPanel.add(textNumber, BorderLayout.EAST);

        // 姓名
        textName = new JTextField(FIELD_WIDTH);
        namePanel = new JPanel(new BorderLayout());
        labelName = new JLabel("姓名：");
        namePanel.add(labelName, BorderLayout.WEST);
        namePanel.add(textName, BorderLayout.EAST);

        // Email
        textEmail = new JTextField(FIELD_WIDTH);
        emailPanel = new JPanel(new BorderLayout());
        labelEmail = new JLabel("Email：");
        emailPanel.add(labelEmail, BorderLayout.WEST);
        emailPanel.add(textEmail, BorderLayout.EAST);

        // 電話號碼
        textPhone = new JTextField(FIELD_WIDTH);
        phonePanel = new JPanel(new BorderLayout());
        labelPhone = new JLabel("電話號碼：");
        phonePanel.add(labelPhone, BorderLayout.WEST);
        phonePanel.add(textPhone, BorderLayout.EAST);

        // 密碼
        textPassWord = new JTextField(FIELD_WIDTH);
        passWordPanel = new JPanel(new BorderLayout());
        labelPassWord = new JLabel("密碼：");
        passWordPanel.add(labelPassWord, BorderLayout.WEST);
        passWordPanel.add(textPassWord, BorderLayout.EAST);

        // 再次輸入密碼
        textSecondPwd = new JTextField(FIELD_WIDTH);
        secondPwdPanel = new JPanel(new BorderLayout());
        labelSecondPwd = new JLabel("再次輸入密碼：");
        secondPwdPanel.add(labelSecondPwd, BorderLayout.WEST);
        secondPwdPanel.add(textSecondPwd, BorderLayout.EAST);

        // hintLabel
        hintLabel = new JLabel("請仔細檢查學號，後續無法更改");
        hintLabel.setForeground(Color.GRAY);
        hintLabel.setHorizontalAlignment(JLabel.CENTER);

        // 完成按鈕
        buttonFin = new JButton("完成");
        buttonFin.setPreferredSize(new Dimension(100, 40));
        buttonFin.addActionListener(e -> {
            name = textName.getText().trim();
            email = textEmail.getText().trim();
            phone = textPhone.getText().trim();
            number = textNumber.getText().trim();
            passWord = textPassWord.getText().trim();
            pwdSecond = textSecondPwd.getText().trim();
            if (checkInput()) {
                u.execute(name, email, phone, number, passWord);
                JOptionPane.showMessageDialog(null, "儲存成功", "完成", JOptionPane.PLAIN_MESSAGE);
                cardLayout.show(mainPanel, "login");
            }
        });

        // 返回按鈕
        Image imgBack = new ImageIcon(getClass().getResource("/back.png"))
                .getImage().getScaledInstance(16, 29, Image.SCALE_SMOOTH);
        backbtn = new JButton();
        Icon backIcon = new ImageIcon(imgBack);
        backbtn.setIcon(backIcon);
        backbtn.setPreferredSize(new Dimension(16, 29));
        backbtn.putClientProperty("JButton.buttonType", "toolBarButton");
        backbtn.setOpaque(true);
        backbtn.setBorderPainted(false);
        backbtn.setContentAreaFilled(true);
        backbtn.setFocusPainted(false);
        backbtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        // 上方標題區與返回按鈕
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 0));
        topPanel.setBackground(new Color(217, 217, 217));

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.setBackground(new Color(217, 217, 217));
        labelPanel.add(new JLabel("註冊新帳號！"));

        JPanel backBtnPanel = new JPanel();
        backBtnPanel.setBackground(new Color(217, 217, 217));
        backBtnPanel.add(backbtn);

        topPanel.add(backBtnPanel, BorderLayout.WEST);
        topPanel.add(labelPanel, BorderLayout.CENTER);
        topPanel.add(Box.createRigidArea(new Dimension(50, 40)), BorderLayout.EAST);

        // 下方按鈕區
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 280, 0));
        bottomPanel.add(buttonFin);

        // 將所有欄位加入 smallPanel
        smallPanel.add(numberPanel);
        smallPanel.add(namePanel);
        smallPanel.add(emailPanel);
        smallPanel.add(phonePanel);
        smallPanel.add(passWordPanel);
        smallPanel.add(secondPwdPanel);
        smallPanel.add(hintLabel);

        // 小面板包進主面板，再加入本元件
        panel.add(smallPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public boolean checkInput() {
        if (u.isNumberExists(number)) {
            JOptionPane.showMessageDialog(null, "該學號已註冊！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || number.isEmpty() || passWord.isEmpty()) {
            JOptionPane.showMessageDialog(null, "請填寫所有欄位！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!passWord.equals(pwdSecond)) {
            JOptionPane.showMessageDialog(null, "兩次密碼輸入不同！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!number.matches("^\\d{9}$")) {
            JOptionPane.showMessageDialog(null, "請輸入正確的學號！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!phone.matches("^09\\d{8}$")) {
            JOptionPane.showMessageDialog(null, "請輸入正確的電話號碼！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            JOptionPane.showMessageDialog(null, "請輸入有效的email！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
