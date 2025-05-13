package activity;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Activity extends JPanel{
    private CardLayout cardLayout = new CardLayout();

    private JPanel editPanel = new JPanel();
    private JPanel completePanel = new JPanel();

    private JTextField actName, date,  time, place, price, limitNumofPeople, dueDate, dueTime;
    private JButton add, next, back;
	private JPanel basicInformation, actIntro, actSettings;
    private JLabel timeLabel, placeLabel, priceLabel, limitLabel, introLabel, dueLabel, payLabel, contactLabel;
	private JRadioButton online, free, cash, card;
    private JTextArea introArea;

    private JTextField name = new JTextField("your name", 10);
    private JButton confirm = new JButton("done");
    private JLabel nameLabel = new JLabel(); // 用於完成模式顯示
    private JLabel completeTimeLabel = new JLabel(); // 用於完成模式顯示

    private String organizerName, dateAndTime, completeActname;

    public Activity() {
        setLayout(cardLayout); // 套用 CardLayout 到 Activity（自己就是 JPanel）

        createEditPanel();
        createCompletePanel();

        // 加入兩種狀態到 CardLayout 中
        add(editPanel, "edit");
        add(completePanel, "complete");

        // 預設顯示編輯模式
        cardLayout.show(this, "edit");
    }

    // 建立編輯模式的畫面
    public final void createEditPanel() {
        createBasicInformationPanel();
        createActIntroPanel();
        createActSettingsPanel();
        createLayout();

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 把輸入值存起來
                completeActname = actName.getText(); 
                dateAndTime = String.format("日期： %s\n時間： %s", date.getText(), time.getText());
                // 更新完成模式的 label
                nameLabel.setText(completeActname);
                completeTimeLabel.setText(dateAndTime);
                
                // 切到完成模式
                cardLayout.show(Activity.this, "complete"); 
            }
        });
    }

    // 建立Activity
    // 分成三個panel 上：活動基本資訊 中：活動簡介 下：活動報名設定
	public void createBasicInformationPanel(){

		//使用者輸入活動資訊(活動名稱 活動時間 活動日期 活動地點 活動費用 人數限制)
		actName = new JTextField("活動名稱");
		timeLabel = new JLabel("Time: ");
		date = new JTextField("YYYY-MM-DD", 10);
		time = new JTextField("HH:MM", 5);

		//information label
		placeLabel = new JLabel("Place: ");
		place = new JTextField(10);

		//當online被點選 表示活動在線上 因此不用輸入place
		online = new JRadioButton("online");
		if(online.isSelected()){
			place.setEnabled(false);
		}else{
			place.setEnabled(true);
		}

		//當free被點選 表示免費活動 因此不用輸入price
		priceLabel = new JLabel("Price: ");
		price = new JTextField(4);
		free = new JRadioButton("free");
		if(free.isSelected()){
			free.setEnabled(false);
		}else{
			free.setEnabled(true);
		}

		limitLabel = new JLabel("Limit: ");
		limitNumofPeople = new JTextField(4);
	}

	//使用者隨意輸入活動相關資訊（textArea）
	public void createActIntroPanel(){
		actIntro = new JPanel();
		introLabel = new JLabel("活動簡介");
		introArea = new JTextArea(100,100);
		JScrollPane scrollPane = new JScrollPane(introArea); // 加捲軸

	}

	//活動報名設定（報名截止時間 付款方式 輸入主揪聯絡方式）
	public void createActSettingsPanel(){
		dueLabel = new JLabel("截止日期： ");
		dueDate = new JTextField("YYYY-MM-DD", 10);
		dueTime = new JTextField("HH:MM", 5);

		payLabel = new JLabel("付款方式： ");
		cash = new JRadioButton("cash");
		card = new JRadioButton("card");
		ButtonGroup btnG = new ButtonGroup();
		btnG.add(cash);
		btnG.add(card);

		contactLabel = new JLabel("聯絡方式： ");
		add = new JButton("+");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				addContact();
			}
		});

	}
			//增加聯絡方式
	public void addContact(){
		JTextField contactType = new JTextField("social media",5);
		JTextField contactID = new JTextField("id", 20);
		actSettings.add(contactType);
		actSettings.add(contactID);
	}

	//layout
	public void createLayout(){
		//edit panel
		editPanel.setLayout(new CardLayout());
		editPanel.setSize(650, 700);


		// basic information layout
        basicInformation = new JPanel();
		basicInformation.setLayout(new BoxLayout(basicInformation, BoxLayout.Y_AXIS));
		// basicInformation.setSize(650, 700);
        basicInformation.setVisible(true);
		basicInformation.add(actName);

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		p1.setSize(650, 50);
		p1.add(timeLabel);
		p1.add(date);
		p1.add(time);
		basicInformation.add(p1);

		JPanel p2 = new JPanel();
		p2.add(placeLabel);
		p2.add(place);
		p2.add(online);
		basicInformation.add(p2);

		JPanel p3 = new JPanel();
		p3.add(priceLabel);
		p3.add(price);
		p3.add(free);
		basicInformation.add(p3);

		JPanel p4 = new JPanel();
		p4.add(limitLabel);
		p4.add(limitNumofPeople);
		basicInformation.add(p4);
        editPanel.add(basicInformation);

		// intro layout
        actIntro = new JPanel();
		actIntro.setLayout(new BoxLayout(actIntro, BoxLayout.Y_AXIS));
        actIntro.setVisible(true);

		JPanel p5 = new JPanel();
		p5.add(introLabel);
		p5.add(introArea);
		actIntro.add(p5);
        editPanel.add(actIntro);

		// setting layout


        editPanel.add(confirm);
	}


	// 建立完成模式的畫面
	public final void createCompletePanel() {
		completePanel.add(nameLabel);
		completePanel.add(timeLabel);
	}
}
