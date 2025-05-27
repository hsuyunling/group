package org.groupapp;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel, p1, p2, p3;
    private HomePage homePage;

    public MainFrame() {
        setTitle("Group");
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
    JPanel panel = new JPanel();
    JPanel container = new RoundedPanel(40);
    JPanel p0 = new JPanel();
    p1 = new JPanel();
    p2 = new JPanel();
    p3 = new JPanel();

    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    p0.setLayout(new BoxLayout(p0, BoxLayout.Y_AXIS));
    p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
    p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
    p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));

    container.add(p0);
    container.add(p1);
    container.add(Box.createVerticalStrut(10));
    container.add(p2);
    container.add(p3);
    panel.add(container);

    panel.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));
    container.setBorder(BorderFactory.createEmptyBorder(20, 100, 30, 100));
    // p0.setBorder(BorderFactory.createEmptyBorder(30, 100, 0, 100));
    // p1.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100));
    // p2.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100));
    // p3.setBorder(BorderFactory.createEmptyBorder(0, 100, 60, 100));

    panel.setBackground(new Color(246, 209, 86));

    // 標題
    JLabel title = new JLabel("登入 Group");
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    title.setFont(new Font("Arial", Font.BOLD, 22));
    title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
    p0.add(Box.createVerticalStrut(15)); // 空間

    p0.add(title);

    // 學號欄位
    JTextField idField = new JTextField();
    JLabel idLabel = new JLabel("學號：");
    idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    idField.setFont(new Font("Arial", Font.PLAIN, 16));
    idLabel.setFont(new Font("Arial", Font.PLAIN, 16));
    p1.add(idLabel);
    p1.add(idField);


    // 密碼欄位
    JPasswordField pwdField = new JPasswordField();
    JLabel pwdLabel = new JLabel("密碼：");
    pwdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    pwdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
    pwdField.setFont(new Font("Arial", Font.PLAIN, 16));
    p2.add(pwdLabel);
    // panel.add(Box.createVerticalStrut(5));
    p2.add(pwdField);

    p3.add(Box.createVerticalStrut(25)); // 空間

    // 登入按鈕
    JButton loginBtn = new JButton("登入");
    loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    loginBtn.setPreferredSize(new Dimension(120, 40));
    loginBtn.setMaximumSize(new Dimension(100, 40));
    loginBtn.setFont(new Font("Arial", Font.BOLD, 16));
    loginBtn.setBackground(new Color(246, 209, 86));
    loginBtn.setForeground(Color.black);
    loginBtn.setFocusPainted(false);
    loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    loginBtn.setOpaque(true);
    loginBtn.setBorderPainted(false);
    loginBtn.setContentAreaFilled(true);

    p3.add(loginBtn);

    // 邏輯處理
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
