package activity;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BasicInformation extends JPanel {

    private JTextField actName, date, time, place, price, limitNumofPeople;
    private JLabel timeLabel, placeLabel, priceLabel, limitLabel;
    private JRadioButton online, free;

    private JTextField dueDate, dueTime, paymentAccount, contactType, contactID;
    private JButton addContactBtn, deleteContactBtn;
    private JLabel dueLabel, payLabel, contactLabel;
    private JCheckBox cash, card;
    private int contactCount = 0;
    Font font = new Font("微軟正黑體",Font.PLAIN, 20);

    public BasicInformation() {
        createBasicInformationPanel();
        createActSettingsPanel();
        createLayout();
    }

    public void createBasicInformationPanel() {

        //使用者輸入活動資訊(活動名稱 活動時間 活動日期 活動地點 活動費用 人數限制)
        actName = new JTextField("活動名稱", 20);
        timeLabel = new JLabel("時間: ");
        date = new JTextField("YYYY-MM-DD", 10);
        time = new JTextField("HH:MM", 5);

        //information label
        placeLabel = new JLabel("地點: ");
        place = new JTextField(10);


        //當online被點選 表示活動在線上 因此不用輸入place(還沒成功)
        online = new JRadioButton("線上");
        if (online.isSelected()) {
            place.setEnabled(false);
        } else {
            place.setEnabled(true);
        }

        //當free被點選 表示免費活動 因此不用輸入price(還沒成功)
        priceLabel = new JLabel("價錢: ");
        price = new JTextField(4);

        free = new JRadioButton("免費");
        if (free.isSelected()) {
            free.setEnabled(false);
        } else {
            free.setEnabled(true);
        }

        limitLabel = new JLabel("人數限制: ");
        limitNumofPeople = new JTextField(4);
    }

    public void createActSettingsPanel() {
        dueLabel = new JLabel("截止日期： ");
        dueDate = new JTextField("YYYY-MM-DD", 10);
        dueTime = new JTextField("HH:MM", 5);

        payLabel = new JLabel("付款方式： ");
        cash = new JCheckBox("現金");
        card = new JCheckBox("刷卡");
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

    //layout
    public void createLayout() {
        // basic information layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // basicInformation.setSize(650, 700);
        setVisible(true);

        JPanel p0 = new JPanel();
        p0.setLayout(new FlowLayout(FlowLayout.LEFT));
        p0.add(actName);

		actName.setPreferredSize(new Dimension(0, 20));

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

        add(p0);
        add(p1);
        add(p2);
        add(p3);
        add(p4);
        add(p5);
        add(p6);
        add(p7);
		add(Box.createVerticalGlue());

    }

    public void setFont(){
    // private JTextField actName, date, time, place, price, limitNumofPeople;
    // private JLabel timeLabel, placeLabel, priceLabel, limitLabel;
    // private JRadioButton online, free;

    // private JTextField dueDate, dueTime, paymentAccount, contactType, contactID;
    // private JButton addContactBtn, deleteContactBtn;
    // private JLabel dueLabel, payLabel, contactLabel;
    // private JCheckBox cash, card;
    // private int contactCount = 0;

    }

}
