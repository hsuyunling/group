import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.util.*;

public class HomePage extends JFrame{
	private static final int FRAME_WIDTH = 650;
	private static final int FRAME_HEIGHT = 750;
	
	public HomePage() {
		setTitle("Group");
		setLayout(null);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
//		建立activity panel
		JPanel panel = new JPanel();
		add(panel);
//		panel template
		JLabel actName = new JLabel("揪團吃飯！");
		JLabel timeLabel = new JLabel("Time: ");
		JLabel placeLabel = new JLabel("Place: ");
		JLabel priceLabel = new JLabel("Price: ");
		JLabel organizer = new JLabel();
		
		JLabel time = new JLabel("星期天 6:00 pm");
		JLabel place = new JLabel("首思義");
		JLabel price = new JLabel("個人餐點費用（150-300）");
		
		JLabel[] labels = {actName, timeLabel, placeLabel, priceLabel, organizer, time, place, price};
		for(JLabel label: labels) {
			panel.add(label);
		}
		//first line
		actName.setBounds(20, 0, 300, 50);
		organizer.setBounds(540, 0, 80, 50);

		//second line 
		timeLabel.setBounds(20, 50, 100, 50);
		time.setBounds(100, 50, 300, 50);

		//third line
		placeLabel.setBounds(20, 100, 100, 50);
		place.setBounds(100, 100, 300, 50);

		//forth line
		priceLabel.setBounds(20, 150, 100, 50);
		price.setBounds(100, 150, 300, 50);


//		建立buttons
		JButton shareBtn = new JButton("share");
		JButton contactBtn = new JButton("contact me");
		JButton moreBtn = new JButton("more information");
//		set eventListeners
		moreBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new Information();

			}
		});
		
		contactBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new Contact();

			}
		});

//設定位置 細節

		panel.add(shareBtn);
		panel.add(contactBtn);
		panel.add(moreBtn);
		shareBtn.setBounds(450,0, 80,50);
		contactBtn.setBounds(495,150, 85,50);
		moreBtn.setBounds(350,150, 130,50);

		panel.setLayout(null);
		setBackground(Color.white);
		panel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		panel.setBounds(0, 0, 650, 200);
	}
	public static void main(String[] args) {
		new HomePage();


	}
}
