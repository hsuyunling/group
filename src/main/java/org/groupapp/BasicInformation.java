package org.groupapp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class BasicInformation extends JPanel {

    JPanel p0, p1, p2, p3, p4, ps1, ps2;

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

    List<JLabel> labels = Arrays.asList(timeLabel, placeLabel, priceLabel, limitLabel, dueLabel, payLabel, contactLabel);
    List<JPanel> panels = new ArrayList<>();

    // --------colors----------
    Color normalColor = new Color(246, 209, 86);
    Color pressedColor = new Color(195, 170, 87);

    public BasicInformation() {
        // setBorder(BorderFactory.createEmptyBorder(20,20,0,20));
        createBasicInformationPanel();
        createLayout();
        setLayout();
        
    }

    public void setLabelFont(){
        Font f = new Font("Arial", Font.PLAIN, 16);
        for(JLabel label: labels){
            label.setFont(f);
            label.setMaximumSize(new Dimension(400, 20));
        }
        

    }

    public void setTfStyle(){
        List<JTextField> tfs = Arrays.asList(actName, date, time, place, price, limitNumofPeople, dueDate, dueTime);
        for(JTextField tf: tfs){
            tf.setMaximumSize(new Dimension(150, 30));
            tf.setPreferredSize(new Dimension(150,30));
        }
        time.setMaximumSize(new Dimension(80, 30));
        time.setPreferredSize(new Dimension(80,30));
        dueTime.setMaximumSize(new Dimension(80, 30));
        dueTime.setPreferredSize(new Dimension(80,30));
        actName.setMaximumSize(new Dimension(300, 50));
        actName.setPreferredSize(new Dimension(300,50));
    }

    public void setLayout(){
        add(p0);
        for (int i = 0; i <= 6; i++) {
            JLabel label = labels.get(i);
            JPanel panel = panels.get(i+1);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(Box.createHorizontalStrut(20));
            add(label);
            add(Box.createVerticalStrut(10));
            add(Box.createHorizontalStrut(20));
            add(panel);
            add(Box.createVerticalStrut(10));
        }
    }

    public void createBasicInformationPanel() {

        // 使用者輸入活動資訊(活動名稱 活動時間 活動日期 活動地點 活動費用 人數限制)
        actName = new JTextField("活動名稱", 20);
        timeLabel = new JLabel("時間/ 日期: ");
        date = new JTextField("YYYY-MM-DD", 10);
        time = new JTextField("HH:MM", 5);

        // information label
        placeLabel = new JLabel("地點: ");
        place = new JTextField(10);

        // 當online被點選 表示活動在線上 因此不用輸入place(還沒成功)
        online = new JRadioButton("線上");
        if (online.isSelected()) {
            place.setEnabled(false);
        } else {
            place.setEnabled(true);
        }

        // 當free被點選 表示免費活動 因此不用輸入price(還沒成功)
        priceLabel = new JLabel("價錢: ");
        price = new JTextField(10);
        free = new JRadioButton("免費");
        if (free.isSelected()) {
            free.setEnabled(false);
        } else {
            free.setEnabled(true);
        }

        limitLabel = new JLabel("人數限制: ");
        limitNumofPeople = new JTextField(10);


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
        labels = Arrays.asList(timeLabel, placeLabel, priceLabel, limitLabel, dueLabel, payLabel, contactLabel);

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
        p0 = new JPanel();
        p1 = new JPanel();
        p2 = new JPanel();
        p3 = new JPanel();
        p4 = new JPanel();
        ps1 = new JPanel();
        ps2 = new JPanel();
        JPanel blank = new JPanel();
        panels = Arrays.asList(p0, p1, p2, p3, p4, ps1, ps2, blank);

        // basic information layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // basicInformation.setSize(650, 700);
        setVisible(true);

        p0.add(actName);

        p1.add(date);
        p1.add(time);

        p2.add(place);
        p2.add(online);

        p3.add(price);
        p3.add(free);

        p4.add(limitNumofPeople);

        ps1.add(dueDate);
        ps1.add(dueTime);

        ps2.add(cash);
        ps2.add(card);


        // add(Box.createVerticalGlue());

// ---------------settings-------------------


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

        // add(ps3);

        setLabelFont();
        setTfStyle();

    }

    // public void setBtnStyle(){
        
    //         for (JTextField tf : tf) {
    //         final JTextField thisBtn = btn;
    //         thisBtn.setOpaque(true);
    //         thisBtn.setBorderPainted(false);
    //         thisBtn.setContentAreaFilled(true);
    //         thisBtn.setFocusPainted(false);

    //         thisBtn.setBackground(normalColor);

    //         thisBtn.getModel().addChangeListener(e -> {
    //             ButtonModel model = thisBtn.getModel();
    //             if (model.isPressed()) {
    //                 thisBtn.setBackground(pressedColor);
    //             } else {
    //                 thisBtn.setBackground(normalColor);
    //             }
    //         });
    //     }
    // }

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
