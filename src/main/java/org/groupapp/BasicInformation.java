package org.groupapp;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BasicInformation extends JPanel {

    JPanel p0, p1, p2, p3, p4, ps1;

    private JTextField actName, place, price, limitNumofPeople;
    private JLabel timeLabel, placeLabel, priceLabel, limitLabel;
    private JRadioButton online, free;
    private JDateChooser dateChooser, dueDateChooser;
    private JSpinner timeSpinner, dueTimeSpinner;

    Color normalColor = new Color(246, 209, 86);
    Color pressedColor = new Color(195, 170, 87);

    public BasicInformation() {
        createLayout();
       
    }

    public void createLayout() {
        p0 = new JPanel();
        p1 = new JPanel();
        p2 = new JPanel();
        p3 = new JPanel();
        p4 = new JPanel();
        ps1 = new JPanel();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setVisible(true);

        // 活動名稱
        actName = new JTextField("活動名稱", 20);
        p0.add(actName);

        // 日期 + 時間
        timeLabel = new JLabel("時間/ 日期: ");
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date());

        p1.add(dateChooser);
        p1.add(timeSpinner);

        // 地點
        placeLabel = new JLabel("地點: ");
        place = new JTextField(10);
        online = new JRadioButton("線上");
        online.addActionListener(e -> place.setEnabled(!online.isSelected()));
        p2.add(place);
        p2.add(online);

        // 價格
        priceLabel = new JLabel("價錢: ");
        price = new JTextField(10);
        free = new JRadioButton("免費");
        free.addActionListener(e -> {
            if (free.isSelected()) {
                price.setText("0");
                price.setEnabled(false);
            } else {
                price.setEnabled(true);
            }
        });
        p3.add(price);
        p3.add(free);

        // 人數限制
        limitLabel = new JLabel("人數限制: ");
        limitNumofPeople = new JTextField(10);
        p4.add(limitNumofPeople);

        // 截止日期 + 時間
        JLabel dueLabel = new JLabel("截止日期： ");
        dueDateChooser = new JDateChooser();
        dueDateChooser.setDateFormatString("yyyy-MM-dd");

        dueTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dueEditor = new JSpinner.DateEditor(dueTimeSpinner, "HH:mm");
        dueTimeSpinner.setEditor(dueEditor);
        dueTimeSpinner.setValue(new Date());

        ps1.add(dueDateChooser);
        ps1.add(dueTimeSpinner);

        // 加入元件
        add(p0);
        addRow(timeLabel, p1);
        addRow(placeLabel, p2);
        addRow(priceLabel, p3);
        addRow(limitLabel, p4);
        addRow(dueLabel, ps1);
        add(Box.createVerticalStrut(100));
    }

    private void addRow(JLabel label, JPanel panel) {
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(Box.createHorizontalStrut(20));
        add(label);
        add(Box.createHorizontalStrut(20));
        add(panel);
        add(Box.createVerticalStrut(10));
    }

    // ----------------- Getter + 驗證 -----------------

    public String getName() {
        String nameValue = actName.getText().trim();
        if (nameValue.isEmpty()) throw new IllegalArgumentException("活動名稱不能為空");
        return nameValue;
    }

    public String getDate() {
        Date d = dateChooser.getDate();
        if (d == null) throw new IllegalArgumentException("請選擇活動日期");
        return new SimpleDateFormat("yyyy-MM-dd").format(d);
    }

    public String getTime() {
        return new SimpleDateFormat("HH:mm").format((Date) timeSpinner.getValue());
    }

    public String getPlace() {
        if (!online.isSelected() && place.getText().trim().isEmpty())
            throw new IllegalArgumentException("地點為必填欄位");
        return place.getText().trim();
    }

    public String getPrice() {
        String value = price.getText().trim();
        if (value.isEmpty()) throw new IllegalArgumentException("價錢不能為空");
        if (!value.matches("\\d+")) throw new IllegalArgumentException("價錢必須為數字");
        return value;
    }

    public int getLimitPeople() {
        String value = limitNumofPeople.getText().trim();
        if (!value.matches("\\d+")) throw new IllegalArgumentException("人數限制必須為數字");
        return Integer.parseInt(value);
    }

    public String getDueDate() {
        Date d = dueDateChooser.getDate();
        if (d == null) throw new IllegalArgumentException("請選擇截止日期");
        return new SimpleDateFormat("yyyy-MM-dd").format(d);
    }

    public String getDueTime() {
        return new SimpleDateFormat("HH:mm").format((Date) dueTimeSpinner.getValue());
    }
}
