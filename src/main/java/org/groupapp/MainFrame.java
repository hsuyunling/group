package org.groupapp;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel ,p0 , p1, p2, p3;
    private HomePage homePage;
    Color color = UIManager.getColor ( "Panel.background" );

    public MainFrame() {
        setTitle("Group");
        setSize(650, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 加入 登入/註冊 頁面
        JPanel loginPanel = createLoginPanel();
        JPanel registerPanel = new Register();
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");

        // 加入主頁面（但尚未顯示）
        /*
         * homePage = new HomePage(user);
         * mainPanel.add(homePage, "home");
         */

        add(mainPanel);
        cardLayout.show(mainPanel, "login"); // 預設顯示登入頁
    }

    // 登入頁面，含註冊按鈕
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        JPanel container = new RoundedPanel(40);
        p0 = new JPanel();
        p1 = new JPanel();
        p2 = new JPanel();
        p3 = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        p0.setLayout(new BoxLayout(p0, BoxLayout.Y_AXIS));
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
        p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));

        container.add(p0);
        container.add(p1);
        container.add(Box.createVerticalStrut(10));
        container.add(p2);
        container.add(Box.createVerticalStrut(10));
        container.add(p3);
        panel.add(container);

        panel.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));
        container.setBorder(BorderFactory.createEmptyBorder(20, 100, 30, 100));

        panel.setBackground(new Color(246, 209, 86));

        
        // 標題
        JLabel title = new JLabel("登入 Group");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Microsoft JhengHei", Font.BOLD, 22)); //  支援中文字型
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 30, 0));
        p0.add(title);

        // 學號欄位
        JTextField idField = new JTextField();
        JLabel idLabel = new JLabel("學號：");
        idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        idField.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 16)); // 
        idLabel.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 16)); // 
        p1.add(idLabel);
        p1.add(idField);

        // 密碼欄位
        JPasswordField pwdField = new JPasswordField();
        JLabel pwdLabel = new JLabel("密碼：");
        pwdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pwdLabel.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 16)); // 
        pwdField.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 16)); // 
        p2.add(pwdLabel);
        p2.add(pwdField);


        // 登入按鈕
        JButton loginBtn = new JButton("登入");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(100, 40));
        loginBtn.setFont(new Font("Microsoft JhengHei", Font.BOLD, 16)); // 
        loginBtn.setBackground(new Color(246, 209, 86));
        loginBtn.setForeground(Color.black);
        loginBtn.setFocusPainted(false);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.setContentAreaFilled(true);

        JButton registerBtn = new JButton("註冊");
        registerBtn.setFocusPainted(false);
        registerBtn.setOpaque(true);
        registerBtn.setBackground(color);
        registerBtn.setBorderPainted(false);
        registerBtn.setPreferredSize(new Dimension(60, 40));

        p3.add(Box.createRigidArea(new Dimension(70, 0))); // 開頭空格
        p3.add(loginBtn);
        // p3.add(Box.createRigidArea(new Dimension(5, 0)));  // login 和 register 之間空格
        p3.add(registerBtn);


        // 邏輯處理(登入)
        loginBtn.addActionListener(e -> {
            DBUtil c = new DBUtil();
            String id = idField.getText().trim();
            String pwd = new String(pwdField.getPassword()).trim();
            User user = c.select(id, pwd);
            String result;
            if (c.getSuccess()) {
                result = "登入成功！";
                JOptionPane.showMessageDialog(null, result, "結果", JOptionPane.PLAIN_MESSAGE);

                homePage = new HomePage(user); // 傳入 user
                mainPanel.add(homePage, "home"); // 加進 CardLayout
                cardLayout.show(mainPanel, "home"); // 切換畫面
            } else {
                result = "帳號或密碼錯誤！";
                JOptionPane.showMessageDialog(null, result, "結果", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // 邏輯處理(註冊)
        registerBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "register"); // 切換畫面
        });

        return panel;
    }

}
