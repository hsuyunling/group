import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class HomePage extends JFrame{
    private static final int FRAME_WIDTH = 650;
    private static final int FRAME_HEIGHT = 750;
    private final JButton addActivity = new JButton("+");
    JPanel addNew = new Activity(); 

    public HomePage(){
        setLayout(new BorderLayout());
        setTitle("Group");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        add(addActivity, BorderLayout.SOUTH);
        setBtnActionListener();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public final void setBtnActionListener(){
        addActivity.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e){
                // 當按鈕被點擊時，將 Activity 加入到中央區域
                add(addNew, BorderLayout.CENTER);
                
                // 確保畫面更新，重新顯示新加入的面板
                revalidate(); // 重新佈局
                repaint();    // 重繪畫面
            }
        });
    }
}
