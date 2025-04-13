
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.ArrayList;

public class KeepPage extends JFrame {

    public KeepPage() {
    	
    	// set frame
        setTitle("收藏頁面");
        setSize(650, 750);
        setLayout(new BorderLayout());
		
        JPanel activityPanel = new JPanel();
        activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(activityPanel);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}