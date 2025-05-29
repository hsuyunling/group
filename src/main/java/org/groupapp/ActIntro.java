package org.groupapp;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ActIntro extends JPanel {
    private JLabel introLabel;
    private JTextArea introArea;
    private JScrollPane scrollPane;
    private Dimension size;

    public ActIntro(){
        setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        createActIntroPanel();
        createLayout();
    }

    public String getIntro() { return introArea.getText(); }


    	//使用者隨意輸入活動相關資訊（textArea）
	public void createActIntroPanel(){
        size = new Dimension(650, 600);
		introLabel = new JLabel("活動簡介");
        introLabel.setFont(new Font("Arial", Font.BOLD, 17));
		introArea = new JTextArea();
        introArea.setFont(new Font("Arial", Font.PLAIN, 17));
		scrollPane = new JScrollPane(introArea); // 加捲軸
        scrollPane.setPreferredSize(size);
        scrollPane.setMaximumSize(size);
	}

    public void createLayout(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        introArea.setLineWrap(true); // 自動換行
        introArea.setWrapStyleWord(true); // 單詞邊界換行

        add(introLabel);
        introLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);

    }
    
}
