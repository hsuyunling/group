package activity;

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
                repaint();    // 重繪畫面

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
    //增加聯絡方式

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

        JPanel p5 = new JPanel();
        p5.setLayout(new FlowLayout(FlowLayout.LEFT));
        p5.add(dueLabel);
        p5.add(dueDate);
        p5.add(dueTime);

        JPanel p6 = new JPanel();
        p6.setLayout(new FlowLayout(FlowLayout.LEFT));
        p6.add(payLabel);
        p6.add(cash);
        p6.add(card);
        p6.add(paymentAccount);

        // 一開始不顯示
        paymentAccount.setVisible(false);

        // 用 ItemListener 監聽 checkbox 的勾選狀態
        card.addItemListener(e -> {
            if (card.isSelected()) {
                paymentAccount.setVisible(true);
                p6.revalidate();
                p6.repaint();
            } else {
                paymentAccount.setVisible(false);
                p6.revalidate();
                p6.repaint();
            }
        });

        JPanel p7 = new JPanel();
        p7.setLayout(new FlowLayout(FlowLayout.LEFT));
        p7.add(contactLabel);
        p7.add(addContactBtn);

        add(p5);
        add(p6);
        add(p7);

    }

}
