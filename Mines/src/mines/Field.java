package mines;

import java.awt.*;

/**
 * 網格類
 */
public class Field {
	public static final int STYLE_COVERED = 1;	//Field覆蓋時的樣式
	public static final int STYLE_OPENED = 2;	//Field打開時的樣式
	public static final int STYLE_MARKED = 3;	//Field被標記時的樣式
	public static final int FIELD_SIZE = 25;	//一個格子的大小

	private int mineValue;	//Field的附近地雷值
	private int x;	//Field的橫坐標
	private int y;	//Field的縱坐標
	private int style;	//Field的樣式

	/**
	 * Field類構造方法
	 * @param x 橫坐標
	 * @param y 縱坐標
	 */
	public Field(int x, int y) {
		this.x = x;
		this.y = y;
		style = STYLE_COVERED;	//初始化樣式為覆蓋
		mineValue = 0;	//初始化該Field不為地雷，用mineValue值為0來表示
	}

	/**
	 * 獲取該Field的橫坐標
	 * @return 橫坐標
	 */
	public int getX() {
		return x;
	}

	/**
	 * 獲取該Field的縱坐標
	 * @return 縱坐標
	 */
	public int getY() {
		return y;
	}

	/**
	 * 設置某個Field的樣式
	 * @param style 樣式
	 */
	public void setStyle(int style) {
		this.style = style;
	}

	/**
	 * 通過把mineValue值設置為-100表示該Field為地雷
	 */
	public void setMine() {
		mineValue = -100;
	}

	/**
	 * 返回該Field的mineValue
	 * @return mineValue
	 */
	public int getMineValue() {
		return mineValue;
	}

	/**
	 * 設置該Field的地雷值
	 * @param value 地雷值
	 */
	public void setMineValue(int value) {
		mineValue = value;
	}

	/**
	 * 判斷該Field是否地雷
	 * @return true，是；否則，false
	 */
	public boolean isMine() {
		return mineValue == -100;
	}

	/**
	 * 判斷該Field是否被標記
	 * @return true，被標記；false，沒被標記
	 */
	public boolean isMarked() {
		return style == STYLE_MARKED;
	}

	/**
	 * 判斷該Field是否覆蓋
	 * @return true，覆蓋；false，沒覆蓋
	 */
	public boolean isCovered() {
		return style == STYLE_COVERED;
	}

	/**
	 * 判斷該Field是否已打開
	 * @return true，已打開；false，沒打開
	 */
	public boolean isOpened() {
		return style == STYLE_OPENED;
	}

	/**
	 * 繪圖方法
	 * @param g Graphics g
	 */
	public void paintField(Graphics g) {
		int yCoordinate = x * FIELD_SIZE + 1;
		int xCoordinate = y * FIELD_SIZE + 1;
		
		if(isCovered()) {//樣式為覆蓋時候所做的繪圖
			g.setColor(Color.CYAN);
			g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
		}
		
		if(isOpened()) {//樣式為打開時所做的繪圖
			if(mineValue > 0) {//當該Field的附近地雷值大於0時
				g.setColor(Color.WHITE);
				g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
				g.setColor(Color.BLACK);
				g.drawString(mineValue + "", y * FIELD_SIZE + FIELD_SIZE / 2, x * FIELD_SIZE + FIELD_SIZE / 2);
				
			} else if(mineValue == -100) {//當該Field是地雷時
				g.setColor(Color.RED);
				g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
				
			} else if(mineValue == 0) {//當該Field的附近地雷值為0
				g.setColor(Color.WHITE);
				g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
				
			}
		}

		if(isMarked()) {//樣式為標記時所做的繪圖
			g.setColor(Color.CYAN);
			g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
			g.setColor(Color.MAGENTA);
			g.drawString("？", y * FIELD_SIZE + FIELD_SIZE / 2, x * FIELD_SIZE + FIELD_SIZE / 2);
		}
	}
}