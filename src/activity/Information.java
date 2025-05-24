import javax.swing.*;
import java.io.*;
import java.awt.*;

// 寫個人資訊那一頁
public class Information extends JFrame {

    private final int FRAME_WIDTH = 650;
    private final int FRAME_HEIGHT = 750;

    private JPanel panel, namePanel, emailPanel, phonePanel, numberPanel, addressPanel, sexPanel;
    private JLabel labelName, labelEmail, labelPhone, labelNumber, labelAddress;

    // Consturctor
    public Information() {
        createPanel();

        setTitle("Contact");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void createPanel() {

        Insert i = new Insert();

        namePanel = new JPanel();
        emailPanel = new JPanel();
        phonePanel = new JPanel();
        numberPanel = new JPanel();
        addressPanel = new JPanel();
        sexPanel = new JPanel();

        labelName = new JLabel();
        labelEmail = new JLabel();
        labelPhone = new JLabel();
        labelNumber = new JLabel();
        labelAddress = new JLabel();
    }

}