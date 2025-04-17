package org.groupapp;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Settings extends JPanel {

    private JTextField dueDate, dueTime, paymentAccount, contactType, contactID;
    private JButton addContactBtn, deleteContactBtn;
    private JLabel dueLabel, payLabel, contactLabel;
    private JCheckBox cash, card;
    private int contactCount = 0;

    private JTextField name = new JTextField("your name", 10);
    private JButton confirm = new JButton("done");
    private JLabel nameLabel = new JLabel(); // 用於完成模式顯示
    private JLabel completeTimeLabel = new JLabel(); // 用於完成模式顯示

    public Settings() {
        createActSettingsPanel();
        createLayout();
    }

    // 加在 class 裡最後面

    public String getDueDate() {
        return dueDate.getText();
    }

    public String getDueTime() {
        return dueTime.getText();
    }

    public void createActSettingsPanel() {
        dueLabel = new JLabel("截止日期： ");
        dueDate = new JTextField("YYYY-MM-DD", 10);
        dueTime = new JTextField("HH:MM", 5);

        payLabel = new JLabel("付款方式： ");
        cash = new JCheckBox("cash");
        card = new JCheckBox("card");
        paymentAccount = new JTextField("轉帳帳號", 14);

        contactLabel = new JLabel("聯絡方式： ");
        addContactBtn = new JButton("+");
        deleteContactBtn = new JButton("-");
        deleteContactBtn.setVisible(false);
        addContactBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addContact();
                contactCount++;
                add(deleteContactBtn);
                deleteContactBtn.setVisible(true);
                // 確保畫面更新，重新顯示新加入的面板
                revalidate(); // 重新佈局
                repaint(); // 重繪畫面

            }
        });

        deleteContactBtn.addActionListener(e -> {
            if (contactCount > 1) {
                deleteContact();
                contactCount--;
            } else if (contactCount > 0) {
                deleteContact();
                contactCount--;
                deleteContactBtn.setVisible(false);
            }
        });

    }
    // 增加聯絡方式

    public void addContact() {
        JPanel contactJPanel = new JPanel();

        contactType = new JTextField("social media", 5);
        contactID = new JTextField("id", 10);

        contactJPanel.add(contactType);
        contactJPanel.add(contactID);

        add(contactJPanel);

    }

    public void deleteContact() {
        remove(contactType);
        remove(contactID);
    }

    public void createLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        p1.add(dueLabel);
        p1.add(dueDate);
        p1.add(dueTime);

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        p2.add(payLabel);
        p2.add(cash);
        p2.add(card);
        p2.add(paymentAccount);

        // 一開始不顯示
        paymentAccount.setVisible(false);

        // 用 ItemListener 監聽 checkbox 的勾選狀態
        card.addItemListener(e -> {
            if (card.isSelected()) {
                paymentAccount.setVisible(true);
                p2.revalidate();
                p2.repaint();
            } else {
                paymentAccount.setVisible(false);
                p2.revalidate();
                p2.repaint();
            }
        });

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout(FlowLayout.LEFT));
        p3.add(contactLabel);
        p3.add(addContactBtn);

        add(p1);
        add(p2);
        add(p3);

    }

}
