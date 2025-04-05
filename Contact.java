import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.util.*;

public class Contact extends JFrame{
	private static final int FRAME_WIDTH = 650;
	private static final int FRAME_HEIGHT = 750;
	
	public Contact() {
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
//		components
		JLabel profilePic = new JLabel(new ImageIcon("pic.JPG"));
		JLabel contactMe = new JLabel("Contact Me");
		JLabel LineIDLabel = new JLabel("Line ID: ");
		JLabel LineID = new JLabel("123456789");
		JLabel igLabel = new JLabel("IG: ");
		JLabel ig = new JLabel("celinehuang1212");
		JLabel gmailLabel = new JLabel("Gmail: ");
		JLabel gmail = new JLabel("113306072@g.nccu.edu.tw");
		JLabel phoneNumLabel = new JLabel("Phone Number: ");
		JLabel phoneNum = new JLabel("0912345678");
		
		JLabel[] labels = {profilePic, contactMe, LineIDLabel, LineID, igLabel, ig, gmailLabel, gmail, phoneNumLabel, phoneNum};
		for(JLabel label: labels) {
			add(label);
		}
		
		JButton backBtn = new JButton("back");
		add(backBtn);

//		settings
		backBtn.setBounds(15,0, 50,50);
		
		profilePic.setBounds(70, 100, 40, 40);
		profilePic.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		
		contactMe.setBounds(140, 70, 100, 40);
		
		LineIDLabel.setBounds(140, 110, 70, 20);
		LineID.setBounds(230, 110, 610, 20);
		
		igLabel.setBounds(140, 130, 40, 20);
		ig.setBounds(180, 130, 610, 20);
		
		gmailLabel.setBounds(140, 150, 50, 20);
		gmail.setBounds(190, 150, 610, 20);
		
		phoneNumLabel.setBounds(140, 170, 100, 20);
		phoneNum.setBounds(240, 170, 610, 20);
		
//		add event listeners
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new HomePage();

			}
		});
		
	}
}
