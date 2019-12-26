package mines;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.*;

/**
 * 掃雷場景類
 */
public class MinesFields extends JPanel {
	public static final int ROWS = 16;	//整個場景的行數
	public static final int COLUMNS = 16;	//整個場景的列數
	public static final int MINES_NUM = 40;		//地雷的數目
	
	private Map<String, Field> fields;		//表示雷域中所有Field
	private List<Field> notMineFields;		//表示所有不是地雷的Field的集合
	private GameRunScript mouseListener;	//表示鼠標事件的監聽器
	
	/**
	 * MinesFields類構造方法
	 */
	public MinesFields() {
		setPreferredSize(new Dimension(ROWS * Field.FIELD_SIZE, COLUMNS * Field.FIELD_SIZE));	//設置掃雷面板大小
		
		fields = new HashMap<String, Field>();	//用hash表表示所有的Field
		notMineFields = new ArrayList<Field>();
		mouseListener = new GameRunScript();
		Random random = new Random();
		
		int mineX, mineY;	//記錄生成地雷的坐標
		List<Field> aroundList;		//表示獲取的該Field的附近的Field的集合
		
		/* 生成雷域中的每個Field */
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				fields.put(x + "," + y, new Field(x, y));	//創建Field對象，並把對象添加到fields中
			}
		}
		
		/* 隨機生成40個地雷並設置附近地雷數 */
		for(int i = 0; i < MINES_NUM; i ++) {
			/* 如果生成的地雷坐標重復，重新設置地雷的坐標 */
			do {
				mineX = random.nextInt(COLUMNS);
				mineY = random.nextInt(ROWS);
			} while(isMine(mineX, mineY));
			
			setMine(mineX, mineY);	//根據相應的鍵值把該Field設置為地雷
			aroundList = getAround(mineX, mineY);		//獲取該地雷附近的所有Field
			
			/* 遍歷aroundList，把不是地雷的Field的地雷值加1 */
			for(Field field: aroundList) {
				if(!field.isMine()) {
					field.setMineValue(field.getMineValue() + 1);
				}
			}
			
		}
		
		/* 把不是地雷的Field添加到notMineFields中 */
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				if(!getField(x, y).isMine()) {
					notMineFields.add(getField(x, y));
				}
			}
		}
	}
	
	/**
	 * 獲取內部類的實例
	 * @return 內部類的實例
	 */
	public GameRunScript getInnerInstance() {
		return mouseListener;
	}
	
	/**
	 * 內部類，用於實現用鼠標在遊戲界面上操作的功能
	 */
	 private class GameRunScript extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			Point p = e.getPoint();		//獲取所點擊位置的那個點
			int y = p.x / Field.FIELD_SIZE;		//所獲取的點的橫坐標
			int x = p.y / Field.FIELD_SIZE;		//所獲取的點的縱坐標

			/* 如果單擊鼠標左鍵 */
			if(e.getButton() == MouseEvent.BUTTON1) {
				open(x, y);
			} 
			/* 如果雙擊鼠標左鍵 */
			if(e.getClickCount() == 2) {
				openAround(x, y);
			}
			/* 如果單擊鼠標右鍵,標記該域或取消該域的標記 */
			if(e.getButton() == MouseEvent.BUTTON3) {
				mark(x, y);
			}
				
		} 
	}
	
	/**
	 * 對於所有不是地雷的Field，判斷是否全部都已代開
	 * @return true，全部都已打開；false，部分或全部都沒打開
	 */
	public boolean isAllOpened() {
		for(Field field: notMineFields) {
			if(!field.isOpened()) {
				return false;
			}
		}
		explode();
		return true;
	}
	 
	/**
	 * 返回一個Field對象
	 * @param x 橫坐標
	 * @param y 縱坐標
	 * @return Field對象
	 */
	public Field getField(int x, int y) {
		return fields.get(x + "," + y);
	}
	
	/**
	 * 返回不是地雷的所有Field
	 * @param x 某Field的橫坐標
	 * @param y 某Field的縱坐標
	 * @return 不是地雷的所有Field，存放在集合里
	 */
	public List<Field> getAround(int x, int y) { 
		List<Field> aroundList = new ArrayList<Field>();
		
		for(int m = -1; m <= 1; m ++) {
			for(int n = -1; n <= 1; n ++) {
				/* 如果所選中的對象為地雷本身，跳出本次循環 */
				if(m == 0 && n == 0) {
					continue;
				}
				
				/* 先判斷是否獲取附近的地雷的坐標是否越界，若否，把不是地雷的所有Field放進aroundList裡面 */
				if(x + m < COLUMNS && x + m >= 0 && y + n >= 0 && y + n < ROWS) {
					aroundList.add(getField(x + m, y + n));
				}
				
			}
		}
		
		return aroundList;
		
	}
	
	/**
	 * 當該Field的地雷值為0時，遞歸打開其附近的Field
	 * @param x 橫坐標
	 * @param y 縱坐標
	 */
	public void open(int x, int y) {
		if(isCovered(x, y)) {
			/* 如果是該Field是地雷的話，打開全部Field */
			if(isMine(x, y)) {
				explode();
				JOptionPane.showMessageDialog(null, "你輸了");
				return;
			}			
			setOpened(x, y);	//把該Field設置為打開狀態
			repaint();
			
			if(isAllOpened()) {//如果全部已被打開
				JOptionPane.showMessageDialog(null, "恭喜你，你贏了！！！");
			}	
			
			/* 當該field的地雷值為0才打開附近的Field */
			if(getMineValue(x, y) == 0) {
				List<Field> aroundList = getAround(x, y);
				
				/* 遞歸調用打開某Field附近的所有Field */
				for(Field field: aroundList) {
					open(field.getX(), field.getY());
				}
			}
			
		}
	}
	
	/**
	 * 當在該Field附近的其他域作標記後，用於處理「打開附近Field」的方法
	 * @param x 橫坐標
	 * @param y 縱坐標
	 */
	public void openAround(int x, int y) {
		if(isOpened(x, y) && getMineValue(x, y) > 0) {//如果該Field已打開並且它附近的地雷值大於0
			
			List<Field> aroundList = getAround(x, y);
			int mineNum = 0;
			
			for(Field field: aroundList) {
				/* 如果該Field已被標記，mineNum加1 */
				if(field.isMarked()) {
					mineNum ++;
				}
			}
			
			/* 當該Field附近的地雷值和mineNum相等時才執行真正的操作  */
			if(getMineValue(x, y) == mineNum) {
				for(Field field: aroundList) {	
					if(field.isMarked() && !field.isMine()) {
						explode();	//如果該Field被標記且該Field不是地雷，馬上打開所有Field
						JOptionPane.showMessageDialog(null, "你輸了");
					} else if(!field.isMarked() && !field.isMine()) {
						open(field.getX(), field.getY());//當附近的field不是地雷時調用open方法
					}
				}
				
			}
		}
		
	}
	
	/**
	 * 把某個Field設置為打開狀態
	 * @param x 橫坐標
	 * @param y 縱坐標
	 */
	public void setOpened(int x, int y) {
		getField(x, y).setStyle(Field.STYLE_OPENED);
	}
	
	/**
	 * 根據標記狀態設置其為標記或取消標記
	 * @param x 橫坐標
	 * @param y 縱坐標
	 */
	public void mark(int x, int y) {
		if(isCovered(x, y)) {
			setMarked(x, y);
			repaint();
		} else if(isMarked(x, y)) {
			setCovered(x, y);
			repaint();
		}
	}
	
	/**
	 * 把某個Field設置為被標記
	 * @param x 橫坐標
	 * @param y 縱坐標
	 */
	public void setMarked(int x, int y) {
		getField(x, y).setStyle(Field.STYLE_MARKED);
	}
	
	/**
	 * 判斷一個Field是否被標記
	 * @param x 橫坐標
	 * @param y 縱坐標
	 * @return 若被標記，true；否則，false
	 */
	public boolean isMarked(int x, int y) {
		return getField(x, y).isMarked();
	}
	
	/**
	 * 判斷某個Field是否地雷
	 * @param x 橫坐標
	 * @param y 縱坐標
	 * @return true，是地雷；false，不是地雷
	 */
	public boolean isMine(int x, int y) {
		return getField(x, y).isMine();
	}
		
	/**
	 * 通過把mineValue值設置為-100表示該Field為地雷
	 * @param x 橫坐標
	 * @param y 縱坐標
	 */
	public void setMine(int x, int y) {
		getField(x, y).setMine();
	}
	
	/**
	 * 判斷該Field是否已打開
	 * @param x
	 * @param y
	 * @return true，已被打開；false，沒打開
	 */
	public boolean isOpened(int x, int y) {
		return getField(x, y).isOpened();
	}
	
	/**
	 * 獲得指定Field的地雷值
	 * @param x 橫坐標
	 * @param y 縱坐標
	 * @return 該Field的地雷值
	 */
	public int getMineValue(int x, int y) {
		return getField(x, y).getMineValue();
	}
	
	/**
	 * 判斷某個Field是否為覆蓋
	 * @param x 橫坐標 
	 * @param y 縱坐標
	 * @return 若為覆蓋，true；否則，false
	 */
	public boolean isCovered(int x, int y) {
		return getField(x, y).isCovered();
	}
	
	/**
	 * 把該Field的樣式設置為覆蓋
	 * @param x 橫坐標
	 * @param y 縱坐標
	 */
	public void setCovered(int x, int y) {
		getField(x, y).setStyle(Field.STYLE_COVERED);
	}
	
	/**
	 * 打開全部Field
	 */
	public void explode() {
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				setOpened(x, y);
			}
		}
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getBounds().width, getBounds().height);
		
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				getField(x, y).paintField(g);
			}
		}
			
	}
	
}