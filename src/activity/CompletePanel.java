package activity;

import java.awt.CardLayout;
import javax.swing.*;

public class CompletePanel {
    private CardLayout cardLayout = new CardLayout();

    private JPanel editPanel = new JPanel();
    private JPanel completePanel = new JPanel();

    private JTextField actName, date,  time, place, price, limitNumofPeople, dueDate, dueTime;
    private JButton add, next, back;
	private JPanel basicInformation, actIntro, actSettings;
    private JLabel timeLabel, placeLabel, priceLabel, limitLabel, introLabel, dueLabel, payLabel, contactLabel;
	private JRadioButton online, free, cash, card;
    private JTextArea introArea;

    private JTextField name = new JTextField("your name", 10);
    private JButton confirm = new JButton("done");
    private JLabel nameLabel = new JLabel(); // 用於完成模式顯示
    private JLabel completeTimeLabel = new JLabel(); // 用於完成模式顯示

    private String organizerName, dateAndTime, completeActname;
}
