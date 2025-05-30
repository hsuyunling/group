import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Contact extends JFrame {
    private final int FRAME_WIDTH = 650;
    private final int FRAME_HEIGHT = 750;
    private final int FIELD_WIDTH = 20;

    private JPanel panel, namePanel, emailPanel, phonePanel, numberPanel, passWordPanel, smallPanel;
    private JLabel labelName, labelEmail, labelPhone, labelNumber, labelPassWord;
    private JTextField textName, textEmail, textPhone, textNumber, textPassWord;
    private JButton buttonFin;

    private String name, email, phone, number, passWord;

    // constructor
    public Contact() {
        createPanel();

        setTitle("Contact");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void createPanel() {
        smallPanel = new JPanel(new GridLayout(11, 1, -2, 5));
        panel = new JPanel(new BorderLayout());
        // panel = new JPanel(new BoxLayout(panel,1));
        // panel = new JPanel(new GridLayout(8,1, -2,5));

        // name
        textName = new JTextField(FIELD_WIDTH);
        namePanel = new JPanel();
        labelName = new JLabel("Name：");
        namePanel.add(labelName);
        namePanel.add(textName);

        // email
        textEmail = new JTextField(FIELD_WIDTH);
        emailPanel = new JPanel();
        labelEmail = new JLabel("Email：");
        emailPanel.add(labelEmail);
        emailPanel.add(textEmail);

        // phone
        textPhone = new JTextField(FIELD_WIDTH);
        phonePanel = new JPanel();
        labelPhone = new JLabel("電話號碼：");
        phonePanel.add(labelPhone);
        phonePanel.add(textPhone);

        // 學號
        textNumber = new JTextField(FIELD_WIDTH);
        numberPanel = new JPanel();
        labelNumber = new JLabel("學號：");
        numberPanel.add(labelNumber);
        numberPanel.add(textNumber);

        // 密碼
        textPassWord = new JTextField(FIELD_WIDTH);
        passWordPanel = new JPanel();
        labelPassWord = new JLabel("密碼：");
        passWordPanel.add(labelPassWord);
        passWordPanel.add(textPassWord);

        buttonFin = new JButton("完成");
        buttonFin.setPreferredSize(new Dimension(10, 50));

        // createButton
        buttonFin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // 拿資料
                name = textName.getText();
                email = textEmail.getText();
                phone = textPhone.getText();
                number = textNumber.getText();
                passWord = textPassWord.getText();

                new Insert(name, email, phone, number, passWord);

                JOptionPane.showMessageDialog(null, "儲存成功", "嘻嘻", JOptionPane.PLAIN_MESSAGE);
            }
        });
        buttonFin.setPreferredSize(new Dimension(200, 40));
        JPanel p1 = new JPanel();
        p1.add(buttonFin);
        panel.add(p1, BorderLayout.SOUTH);

        // 把panel加到frame
        JPanel p = new JPanel();
        p.setSize(600, 1000);
        smallPanel.add(p);
        smallPanel.add(p);
        smallPanel.add(p);
        smallPanel.add(p);
        smallPanel.add(p);
        smallPanel.add(namePanel);
        smallPanel.add(emailPanel);
        smallPanel.add(phonePanel);
        smallPanel.add(numberPanel);
        smallPanel.add(passWordPanel);
        smallPanel.add(p1);
        panel.add(smallPanel, BorderLayout.CENTER);
        add(panel);
    }

}
