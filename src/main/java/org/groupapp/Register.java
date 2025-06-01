package org.groupapp;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;

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

    // constructor
    public Register(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        createPanel();
    }

    public void createPanel() {
        smallPanel = new JPanel(new GridLayout(11, 1, -2, 5));
        panel = new JPanel(new BorderLayout());

        // 學號
        textNumber = new JTextField(FIELD_WIDTH);
        numberPanel = new JPanel();
        labelNumber = new JLabel("學號：");
        numberPanel.add(labelNumber);
        numberPanel.add(textNumber);

        // name
        textName = new JTextField(FIELD_WIDTH);
        namePanel = new JPanel();
        labelName = new JLabel("姓名：");
        namePanel.add(labelName);
        namePanel.add(textName);

        // email
        textEmail = new JTextField(FIELD_WIDTH);
        emailPanel = new JPanel();
        labelEmail = new JLabel("Email：");
        emailPanel.add(labelEmail);
        emailPanel.add(textEmail);

        // phone
        textPhone = new JTextField(FIELD_WIDTH);
        phonePanel = new JPanel();
        labelPhone = new JLabel("電話號碼：");
        phonePanel.add(labelPhone);
        phonePanel.add(textPhone);

        // 密碼
        textPassWord = new JTextField(FIELD_WIDTH);
        passWordPanel = new JPanel();
        labelPassWord = new JLabel("密碼：");
        passWordPanel.add(labelPassWord);
        passWordPanel.add(textPassWord);

        // 確認密碼
        textSecondPwd = new JTextField(FIELD_WIDTH);
        secondPwdPanel = new JPanel();
        labelSecondPwd = new JLabel("再次輸入密碼：");
        secondPwdPanel.add(labelSecondPwd);
        secondPwdPanel.add(textSecondPwd);

        // ButtonFin 完成編輯，跳到登入頁
        buttonFin = new JButton("完成");
        buttonFin.addActionListener(e -> {
            // 拿資料
            name = textName.getText().trim();
            email = textEmail.getText().trim();
            phone = textPhone.getText().trim();
            number = textNumber.getText().trim();
            passWord = textPassWord.getText().trim();
            pwdSecond = textSecondPwd.getText().trim();
            if (checkInput()) {
                u.execute(name, email, phone, number, passWord);
                JOptionPane.showMessageDialog(null, "儲存成功", "嘻嘻", JOptionPane.PLAIN_MESSAGE);
                cardLayout.show(mainPanel, "login");
            }

        });

        // backbtn 返回登入頁面
        backbtn = new JButton("返回");
        backbtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "login");
        });

        buttonFin.setPreferredSize(new Dimension(100, 40));
        backbtn.setPreferredSize(new Dimension(100, 40));
        JPanel pBtn = new JPanel();
        pBtn.add(buttonFin);
        pBtn.add(backbtn);
        // panel.add(pBtn, BorderLayout.SOUTH);

        // 學號不可更改提醒
        hintLabel = new JLabel("請仔細檢查學號，後續無法更改");

        smallPanel.add(numberPanel);
        smallPanel.add(namePanel);
        smallPanel.add(emailPanel);
        smallPanel.add(phonePanel);
        smallPanel.add(passWordPanel);
        smallPanel.add(secondPwdPanel);
        smallPanel.add(pBtn);
        smallPanel.add(hintLabel);

        panel.add(smallPanel, BorderLayout.CENTER);
        add(panel);
    }

    // 確認是否為有效輸入
    public boolean checkInput() {
        // 檢查學號是否已註冊
        if (u.isNumberExists(number)) {
            JOptionPane.showMessageDialog(null, "該學號已註冊！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // 避免空值
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || number.isEmpty() || passWord.isEmpty()) {
            JOptionPane.showMessageDialog(null, "請填寫所有欄位！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // 兩次密碼要輸入一樣的
        if (passWord != pwdSecond) {
            JOptionPane.showMessageDialog(null, "兩次密碼輸入不同！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // 學號(Id)一定有9位數
        if (!number.matches("^\\d{9}$")) {
            JOptionPane.showMessageDialog(null, "請輸入正確的學號！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // 正確電話號碼，10碼、09開頭
        if (!phone.matches("^09\\d{8}$")) {
            JOptionPane.showMessageDialog(null, "請輸入正確的電話號碼！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // 正確email
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            JOptionPane.showMessageDialog(null, "請輸入有效的email！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

}
