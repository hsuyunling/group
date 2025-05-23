package org.groupapp;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class BasicInformation extends JPanel {

    // --------basic information----------
    private JTextField actName, date, time, place, price, limitNumofPeople;
    private JLabel timeLabel, placeLabel, priceLabel, limitLabel;
    private JRadioButton online, free;

    // --------settings----------
    private JTextField dueDate, dueTime, paymentAccount, contactType, contactID;
    private JButton addContactBtn, deleteContactBtn;
    private JLabel dueLabel, payLabel, contactLabel;
    private JCheckBox cash, card;
    private int contactCount = 0;

    public BasicInformation() {
        createBasicInformationPanel();
        createLayout();
    }

    public void createBasicInformationPanel() {

        // 使用者輸入活動資訊(活動名稱 活動時間 活動日期 活動地點 活動費用 人數限制)
        actName = new JTextField("活動名稱", 20);
        timeLabel = new JLabel("Time: ");
        date = new JTextField("YYYY-MM-DD", 10);
        time = new JTextField("HH:MM", 5);

        // information label
        placeLabel = new JLabel("Place: ");
        place = new JTextField(10);

        // 當online被點選 表示活動在線上 因此不用輸入place(還沒成功)
        online = new JRadioButton("online");
        if (online.isSelected()) {
            place.setEnabled(false);
        } else {
            place.setEnabled(true);
        }

        // 當free被點選 表示免費活動 因此不用輸入price(還沒成功)
        priceLabel = new JLabel("Price: ");
        price = new JTextField(4);
        free = new JRadioButton("free");
        if (free.isSelected()) {
            free.setEnabled(false);
        } else {
            free.setEnabled(true);
        }

        limitLabel = new JLabel("Limit: ");
        limitNumofPeople = new JTextField(4);


        // --------settings----------
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

    // layout
    public void createLayout() {
        // basic information layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // basicInformation.setSize(650, 700);
        setVisible(true);

        JPanel p0 = new JPanel();
        p0.setLayout(new FlowLayout(FlowLayout.LEFT));
        p0.add(actName);

        actName.setPreferredSize(new Dimension(0, 20));

        JPanel container = new JPanel();
        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        p1.setSize(650, 50);
        p1.add(timeLabel);
        p1.add(date);
        p1.add(time);

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        p2.add(placeLabel);
        p2.add(place);
        p2.add(online);

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout(FlowLayout.LEFT));
        p3.add(priceLabel);
        p3.add(price);
        p3.add(free);

        JPanel p4 = new JPanel();
        p4.setLayout(new FlowLayout(FlowLayout.LEFT));
        p4.add(limitLabel);
        p4.add(limitNumofPeople);

        add(p0);
        add(p1);
        add(p2);
        add(p3);
        add(p4);
        add(Box.createVerticalGlue());

// ---------------settings-------------------
        JPanel ps1 = new JPanel();
        ps1.setLayout(new FlowLayout(FlowLayout.LEFT));
        ps1.add(dueLabel);
        ps1.add(dueDate);
        ps1.add(dueTime);

        JPanel ps2 = new JPanel();
        ps2.setLayout(new FlowLayout(FlowLayout.LEFT));
        ps2.add(payLabel);
        ps2.add(cash);
        ps2.add(card);
        ps2.add(paymentAccount);

        // 一開始不顯示
        paymentAccount.setVisible(false);

        // 用 ItemListener 監聽 checkbox 的勾選狀態
        card.addItemListener(e -> {
            if (card.isSelected()) {
                paymentAccount.setVisible(true);
                ps2.revalidate();
                ps2.repaint();
            } else {
                paymentAccount.setVisible(false);
                ps2.revalidate();
                ps2.repaint();
            }
        });

        JPanel ps3 = new JPanel();
        ps3.setLayout(new FlowLayout(FlowLayout.LEFT));
        ps3.add(contactLabel);
        ps3.add(addContactBtn);

        add(ps1);
        add(ps2);
        add(ps3);

    }

    // 加在 class 裡最後面

    public String getName() {
        return actName.getText();
    }

    public String getDate() {
        return date.getText();
    }

    public String getTime() {
        return time.getText();
    }

    public String getPlace() {
        return place.getText();
    }

    public String getPrice() {
        return price.getText();
    }

    public int getLimitPeople() {
        try {
            return Integer.parseInt(limitNumofPeople.getText());
        } catch (NumberFormatException e) {
            return 0; // 或回傳預設值
        }
    }

}
