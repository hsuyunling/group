import java.awt.*;
import javax.swing.*;


public class Activity extends JPanel{
	private String actName, time, place;
	private int price;
	private JLabel timeLabel, placeLabel, priceLabel;
	private JButton share, mainGrouper, contactMe, more;

	
	public Activity(String actName, String time, String place, int price){
		setSize(200, 650);
		this.actName = actName;
		this.time = time;
		this.place = place;
		this.price = price;
		
	}

	
	
	
}
