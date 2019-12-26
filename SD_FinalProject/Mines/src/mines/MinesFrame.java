package mines;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * 掃雷主框架類
 */
public class MinesFrame extends JFrame{
	
	private MinesFields ms;		//表示掃雷遊戲面板
	private JButton btnStart;	//表示「重新開始」按鈕
	
	/**
	 * 構造方法
	 */
	public MinesFrame(){
		setSize(500, 500);	//設置窗體大小
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);	//設置窗體於屏幕中央
		setTitle("Mine");		//設置標題為Mine
		setResizable(false);		//不允許窗體縮放
		setLayout(new FlowLayout());	//設置佈局管理器

		ms = new MinesFields();		//新建場景類對象
		btnStart = new JButton("重新開始");	//新建按鈕

		add(btnStart);	//把按鈕添加到框架中
		add(ms);		//把掃雷場景添加到框架中

		/* 註冊鼠標事件 */
		addMouseListener(ms.getInnerInstance());
		ms.addMouseListener(ms.getInnerInstance());

		/* 使用匿名內部類的方式監聽按鈕事件，讓遊戲重新開始 */
		btnStart.addActionListener(new Restart());

		setVisible(true);
	}
	
	/**
	 * 內部類，用於實現點擊按鈕重新開始遊戲的功能
	 */
	private class Restart implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			remove(ms);		//先移除面板
			ms = new MinesFields();	//再新建
			add(ms);	//添加新建的MinesFields對象

			/* 註冊鼠標事件 */
			addMouseListener(ms.getInnerInstance());
			ms.addMouseListener(ms.getInnerInstance());

			setVisible(true);
		}
	}

	public static void main(String[] args) {
		new MinesFrame();
	}

}