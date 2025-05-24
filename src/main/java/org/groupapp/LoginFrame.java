package org.groupapp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.prefs.Preferences;

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


public class LoginFrame extends JFrame {
    JPanel topP, middleP, bottomP;

    public LoginFrame() {
        setTitle("登入系統");
        setSize(400, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- 主容器 ---
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // --- 標題 ---
        JLabel titleLabel = new JLabel("登入 Group 系統");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel);

        // --- 學號欄位 ---
        JTextField idField = new JTextField();
        styleInput(idField);
        mainPanel.add(new JLabel("學號"));
        mainPanel.add(idField);

        // --- 密碼欄位 ---
        JPasswordField pwdField = new JPasswordField();
        styleInput(pwdField);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(new JLabel("密碼"));
        mainPanel.add(pwdField);

        // --- 登入按鈕 ---
        JButton loginBtn = new JButton("登入");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setBackground(new Color(100, 149, 237));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 16));
        loginBtn.setMaximumSize(new Dimension(200, 40));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(loginBtn);

        // --- 登入邏輯 ---
        Runnable tryLogin = () -> {
            String id = idField.getText().trim();
            String pwd = new String(pwdField.getPassword()).trim();

            if (DBUtil.validateUser(id, pwd)) {
                Preferences prefs = Preferences.userRoot().node("org.groupapp");
                prefs.put("userId", id);
                JOptionPane.showMessageDialog(this, "登入成功！");
                dispose();
                new MainFrame().setVisible(true); // ← 建議切到 MainFrame
            } else {
                JOptionPane.showMessageDialog(this, "學號或密碼錯誤", "登入失敗", JOptionPane.ERROR_MESSAGE);
            }
        };

        // Enter 鍵觸發
        pwdField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    tryLogin.run();
                }
            }
        });

        loginBtn.addActionListener(e -> tryLogin.run());

        add(mainPanel);
    }

    private void styleInput(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setMargin(new Insets(5, 8, 5, 8));
    }

    public void setLayout(){
        
    }
}
