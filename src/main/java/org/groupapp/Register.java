package org.groupapp;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;

public class Register extends JPanel {
    private final int FIELD_WIDTH = 20;

    private JPanel panel, namePanel, emailPanel, phonePanel, numberPanel, passWordPanel, smallPanel;
    private JLabel labelName, labelEmail, labelPhone, labelNumber, labelPassWord, hintLabel;
    private JTextField textName, textEmail, textPhone, textNumber, textPassWord;
    private JButton buttonFin, backbtn;

    private String name, email, phone, number, passWord;
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

        // name
        textName = new JTextField(FIELD_WIDTH);
        namePanel = new JPanel();
        labelName = new JLabel("Name：");
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

        // 學號
        textNumber = new JTextField(FIELD_WIDTH);
        numberPanel = new JPanel();
        labelNumber = new JLabel("學號：");
        numberPanel.add(labelNumber);
        numberPanel.add(textNumber);

        // 密碼
        textPassWord = new JTextField(FIELD_WIDTH);
        passWordPanel = new JPanel();
        labelPassWord = new JLabel("密碼：");
        passWordPanel.add(labelPassWord);
        passWordPanel.add(textPassWord);

        // ButtonFin 完成編輯，跳到登入頁
        buttonFin = new JButton("完成");
        buttonFin.addActionListener(e -> {
            // 拿資料
            name = textName.getText();
            email = textEmail.getText();
            phone = textPhone.getText();
            number = textNumber.getText();
            passWord = textPassWord.getText();
            u.execute(name, email, phone, number, passWord);

            JOptionPane.showMessageDialog(null, "儲存成功", "嘻嘻", JOptionPane.PLAIN_MESSAGE);
            cardLayout.show(mainPanel, "login");
            // MainFrame f = new MainFrame();
            // f.setVisible(true);
        });

        // backbtn 返回登入頁面
        backbtn = new JButton("返回");
        backbtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "login");
        });

        buttonFin.setPreferredSize(new Dimension(100, 40));
        backbtn.setPreferredSize(new Dimension(100, 40));
        JPanel p1 = new JPanel();
        p1.add(buttonFin);
        p1.add(backbtn);
        panel.add(p1, BorderLayout.SOUTH);

        // 學號不可更改提醒
        hintLabel = new JLabel("請仔細檢查學號，後續無法更改");

        // 把panel加到frame
        JPanel p = new JPanel();
        p.setSize(600, 1000);
        smallPanel.add(p);
        smallPanel.add(p);
        smallPanel.add(p);
        smallPanel.add(p);
        smallPanel.add(p);
        smallPanel.add(namePanel);
        smallPanel.add(emailPanel);
        smallPanel.add(phonePanel);
        smallPanel.add(numberPanel);
        smallPanel.add(passWordPanel);
        smallPanel.add(p1);
        smallPanel.add(hintLabel);

        panel.add(smallPanel, BorderLayout.CENTER);
        add(panel);
    }

}
