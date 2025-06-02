package org.groupapp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;

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
import javax.swing.SwingWorker;
import javax.swing.UIManager;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel, p0, p1, p2, p3;
    private HomePage homePage;
    Color color = UIManager.getColor("Panel.background");

    public MainFrame() {
        setTitle("Group");
        setSize(650, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 創建一個臨時的面板，用於顯示載入中狀態
        JPanel loadingPanel = new JPanel(new BorderLayout());
        loadingPanel.add(new JLabel("請先登入", JLabel.CENTER), BorderLayout.CENTER);
        mainPanel.add(loadingPanel, "home");

        JPanel loginPanel = createLoginPanel();
        JPanel registerPanel = new Register(cardLayout, mainPanel);
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }

    public JPanel createLoginPanel() {
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

        JLabel title = new JLabel("登入 Group");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Microsoft JhengHei", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 30, 0));
        p0.add(title);

        JTextField idField = new JTextField();
        JLabel idLabel = new JLabel("學號：");
        idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        idField.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 16));
        idLabel.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 16));
        p1.add(idLabel);
        p1.add(idField);

        JPasswordField pwdField = new JPasswordField();
        JLabel pwdLabel = new JLabel("密碼：");
        pwdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pwdLabel.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 16));
        pwdField.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 16));
        p2.add(pwdLabel);
        p2.add(pwdField);

        JButton loginBtn = new JButton("登入");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(100, 40));
        loginBtn.setFont(new Font("Microsoft JhengHei", Font.BOLD, 16));
        loginBtn.setBackground(new Color(246, 209, 86));
        loginBtn.setForeground(Color.black);
        loginBtn.setFocusPainted(false);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.setContentAreaFilled(true);

        JButton registerBtn = new JButton("註冊");
        registerBtn.setFocusPainted(false);
        registerBtn.setOpaque(true);
        registerBtn.setContentAreaFilled(true);
        registerBtn.setBackground(color);
        registerBtn.setBorderPainted(false);
        registerBtn.setPreferredSize(new Dimension(60, 40));
        registerBtn.setMaximumSize(new Dimension(60, 40));

        
        p3.add(Box.createRigidArea(new Dimension(70, 0))); // 開頭空格

  
        p3.add(loginBtn);
        p3.add(registerBtn);

        loginBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String pwd = new String(pwdField.getPassword()).trim();

            SwingWorker<User, Void> loginWorker = new SwingWorker<User, Void>() {
                @Override
                protected User doInBackground() throws Exception {
                    return DBUtil.authenticateUser(id, pwd);
                }

                @Override
                protected void done() {
                    try {
                        User user = get();

                        if (user != null) {
                            JOptionPane.showMessageDialog(
                                    MainFrame.this,
                                    "登入成功！",
                                    "結果",
                                    JOptionPane.PLAIN_MESSAGE);

                            // 登入成功後才創建 HomePage
                            if (homePage == null) {
                                homePage = new HomePage(user);
                                mainPanel.remove(mainPanel.getComponent(0)); // 移除臨時面板
                                mainPanel.add(homePage, "home");
                            } else {
                                homePage.setUser(user);
                            }
                            cardLayout.show(mainPanel, "home");

                        } else {
                            JOptionPane.showMessageDialog(
                                    MainFrame.this,
                                    "帳號或密碼錯誤！",
                                    "結果",
                                    JOptionPane.PLAIN_MESSAGE);
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                MainFrame.this,
                                "登入時發生錯誤：" + ex.getMessage(),
                                "錯誤",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            };

            loginWorker.execute();
        });

        registerBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "register");
        });

        return panel;
    }
}
