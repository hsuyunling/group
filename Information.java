import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.util.*;

public class Information extends JFrame{
	private static final int FRAME_WIDTH = 650;
	private static final int FRAME_HEIGHT = 750;
	
	public Information() {
		
//		set frame
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
//		建立buttons
		JButton shareBtn = new JButton("share");
		JButton contactBtn = new JButton();
		JButton keepBtn = new JButton("keep");
		JButton joinBtn = new JButton("join");
		JButton backBtn = new JButton("back");
		
		add(shareBtn);
		add(contactBtn);
		add(keepBtn);
		add(joinBtn);
		add(backBtn);
		
//		設定樣式
		JLabel actName = new JLabel("揪團吃飯");
		JLabel info = new JLabel("資訊欄");
		add(actName);
		add(info);
		
		//first line
		backBtn.setBounds(15,0, 50,50);
		actName.setBounds(80, 0, 300, 50);
		shareBtn.setBounds(450,0, 80,50);
		contactBtn.setBounds(540, 0, 80, 50);
		
		//second line
		info.setBounds(20, 60, 610, 570);
		info.setBorder(BorderFactory.createLineBorder(Color.black, 1));

		//third line
		keepBtn.setBounds(20, 650, 80,50);
		joinBtn.setBounds(110, 650, 80,50);

//		add event listeners
		contactBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new Contact();

			}
		});
		
		keepBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		
		joinBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new Join();

			}
		});
		
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new HomePage();

			}
		});

	}
}
