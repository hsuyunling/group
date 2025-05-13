package activity;

import javax.swing.*;

public class ActIntro extends JPanel {
    private JLabel introLabel;
    private JTextArea introArea;
    private JScrollPane scrollPane;

    public ActIntro(){
        createActIntroPanel();
        createLayout();
    }

    	//使用者隨意輸入活動相關資訊（textArea）
	public void createActIntroPanel(){
		introLabel = new JLabel("活動簡介");
		introArea = new JTextArea();
		scrollPane = new JScrollPane(introArea); // 加捲軸
	}

    public void createLayout(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        introArea.setLineWrap(true); // 自動換行
        introArea.setWrapStyleWord(true); // 單詞邊界換行

        add(introLabel);
        add(scrollPane);

    }
    
}
