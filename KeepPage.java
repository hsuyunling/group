
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.ArrayList;

public class KeepPage extends JFrame {
	private static final int FRAME_WIDTH = 650;
	private static final int FRAME_HEIGHT = 750;
	
    public KeepPage() {
    	
    	// set frame
        setTitle("收藏頁面");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLayout(new BorderLayout());
		
        JPanel activityPanel = new JPanel();
        activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(activityPanel);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
