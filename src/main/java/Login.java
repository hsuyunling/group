import java.awt.BorderLayout;
import java.awt.*;
import javax.swing.*;

public class Login extends JFrame{
    private final int FRAME_WIDTH = 450;
    private final int FRAME_HEIGHT = 250;
    private final int FIELD_WIDTH = 20;

    JPanel phonePanel, numberPanel, buttonPanel;
    JLabel pLabel, nLabel;
    JTextField pField, nField;
    JButton checkButton;

    //  constructor
    public Login(){
        setTitle("Login");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLayout(new BorderLayout());

        createButton();
        createPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void createButton(){
        checkButton = new JButton("登入");
        buttonPanel = new JPanel();
        checkButton.addActionListener(e -> new Insert());
        buttonPanel.add(checkButton);
    }

    public void createPanel(){
        pLabel= new JLabel("手機號碼：");
        nLabel = new JLabel("學號：");
        pField = new JTextField(FIELD_WIDTH);
        nField = new JTextField(FIELD_WIDTH);

        phonePanel = new JPanel();
        numberPanel = new JPanel();

        phonePanel.add(pLabel);
        phonePanel.add(pField);

        numberPanel.add(nLabel);
        numberPanel.add(nField);

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(3,1));
        p.add(numberPanel);
        p.add(phonePanel);
        p.add(buttonPanel);

        add(p, BorderLayout.CENTER);
    }

}
