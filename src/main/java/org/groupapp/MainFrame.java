package org.groupapp;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private HomePage homePage;

    public MainFrame() {
        setTitle("Group 系統");
        setSize(650, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 加入登入頁面
        JPanel loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, "login");

        // 加入主頁面（但尚未顯示）
        homePage = new HomePage();
        mainPanel.add(homePage, "home");

        add(mainPanel);
        cardLayout.show(mainPanel, "login"); // 預設顯示登入頁
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JTextField idField = new JTextField();
        JPasswordField pwdField = new JPasswordField();
        JButton loginBtn = new JButton("登入");

        panel.add(new JLabel("學號："));
        panel.add(idField);
        panel.add(new JLabel("密碼："));
        panel.add(pwdField);
        panel.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String pwd = new String(pwdField.getPassword()).trim();

            if (DBUtil.validateUser(id, pwd)) {
                Preferences prefs = Preferences.userRoot().node("org.groupapp");
                prefs.put("userId", id);

                JOptionPane.showMessageDialog(this, "登入成功！");
                cardLayout.show(mainPanel, "home");
            } else {
                JOptionPane.showMessageDialog(this, "學號或密碼錯誤");
            }
        });

        return panel;
    }
}
