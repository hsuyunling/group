package org.groupapp;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class BasicInformation extends JPanel {

    private JTextField actName, date, time, place, price, limitNumofPeople;
    private JLabel timeLabel, placeLabel, priceLabel, limitLabel;
    private JRadioButton online, free;

    public BasicInformation() {
        createBasicInformationPanel();
        createLayout();
    }

    public void createBasicInformationPanel() {

        //使用者輸入活動資訊(活動名稱 活動時間 活動日期 活動地點 活動費用 人數限制)
        actName = new JTextField("活動名稱", 20);
        timeLabel = new JLabel("Time: ");
        date = new JTextField("YYYY-MM-DD", 10);
        time = new JTextField("HH:MM", 5);

        //information label
        placeLabel = new JLabel("Place: ");
        place = new JTextField(10);

        //當online被點選 表示活動在線上 因此不用輸入place(還沒成功)
        online = new JRadioButton("online");
        if (online.isSelected()) {
            place.setEnabled(false);
        } else {
            place.setEnabled(true);
        }

        //當free被點選 表示免費活動 因此不用輸入price(還沒成功)
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

    }
}
