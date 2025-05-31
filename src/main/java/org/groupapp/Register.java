package org.groupapp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Register extends JPanel {
    private final int FIELD_WIDTH = 20;

    private JPanel namePanel, emailPanel, phonePanel, numberPanel, passWordPanel, smallPanel;
    private JLabel labelName, labelEmail, labelPhone, labelNumber, labelPassWord;
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
        setLayout(new BorderLayout());
        smallPanel = new JPanel(new GridLayout(11, 1, -2, 5));
        smallPanel.setBorder(BorderFactory.createEmptyBorder(100, 140, 0, 140));

        // name
        textName = new JTextField(FIELD_WIDTH);
        namePanel = new JPanel(new BorderLayout());
        labelName = new JLabel("姓名：");
        namePanel.add(labelName, BorderLayout.WEST);
        namePanel.add(textName, BorderLayout.EAST);

        // email
        textEmail = new JTextField(FIELD_WIDTH);
        emailPanel = new JPanel(new BorderLayout());
        labelEmail = new JLabel("Email：");
        emailPanel.add(labelEmail, BorderLayout.WEST);
        emailPanel.add(textEmail, BorderLayout.EAST);

        // phone
        textPhone = new JTextField(FIELD_WIDTH);
        phonePanel = new JPanel(new BorderLayout());
        labelPhone = new JLabel("電話號碼：");
        phonePanel.add(labelPhone, BorderLayout.WEST);
        phonePanel.add(textPhone, BorderLayout.EAST);

        // 學號
        textNumber = new JTextField(FIELD_WIDTH);
        numberPanel = new JPanel(new BorderLayout());
        labelNumber = new JLabel("學號：");
        numberPanel.add(labelNumber, BorderLayout.WEST);
        numberPanel.add(textNumber, BorderLayout.EAST);

        // 密碼
        textPassWord = new JTextField(FIELD_WIDTH);
        passWordPanel = new JPanel(new BorderLayout());
        labelPassWord = new JLabel("密碼：");
        passWordPanel.add(labelPassWord, BorderLayout.WEST);
        passWordPanel.add(textPassWord, BorderLayout.EAST);

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
        Image imgBack = new ImageIcon(getClass().getResource("/back.png"))
        .getImage().getScaledInstance(18, 31, Image.SCALE_SMOOTH);
        backbtn = new JButton();
        Icon backIcon = new ImageIcon(imgBack);
        backbtn.setIcon(backIcon);
        backbtn.putClientProperty( "JButton.buttonType", "toolBarButton" );
        backbtn.setOpaque(true);
        backbtn.setBorderPainted(false);
        backbtn.setContentAreaFilled(true);
        backbtn.setFocusPainted(false);

        backbtn.addActionListener(e -> {
          cardLayout.show(mainPanel, "login");
        });

        buttonFin.setPreferredSize(new Dimension(100, 40));
        backbtn.setPreferredSize(new Dimension(50, 30));
        JPanel p1 = new JPanel();
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(217,217,217));
        topPanel.add(Box.createRigidArea(new Dimension(10, 40)));        
        p1.add(buttonFin);
        topPanel.add(backbtn);
        add(p1, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);


        smallPanel.add(namePanel);
        smallPanel.add(emailPanel);
        smallPanel.add(phonePanel);
        smallPanel.add(numberPanel);
        smallPanel.add(passWordPanel);
        smallPanel.add(p1);
        add(smallPanel, BorderLayout.CENTER);
    }

}
