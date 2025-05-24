import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class First extends JFrame{
    private final int FRAME_WIDTH = 250;
    private final int FRAME_HEIGHT = 150;

    private JButton scroll, login;
    private JPanel s, l;

    //constructor
    public First(){
        setTitle("登入還註冊呢？");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLayout(new BorderLayout()); 
        createButton();
    }
       
        

    public void createButton(){
        scroll = new JButton("註冊");
        login = new JButton("登入");

        scroll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                new Contact();
            }
        });

        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                new Login();
            }
        });

        s = new JPanel();
        l = new JPanel();
        s.add(scroll);
        l.add(login);

        JPanel p = new JPanel();
        p.add(s);
        p.add(l);
        add(p,BorderLayout.CENTER);
    }


    
}
