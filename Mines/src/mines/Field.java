package mines;

import java.awt.*;

/**
 * ������
 */
public class Field {
	public static final int STYLE_COVERED = 1;	//Field�л\�ɪ��˦�
	public static final int STYLE_OPENED = 2;	//Field���}�ɪ��˦�
	public static final int STYLE_MARKED = 3;	//Field�Q�аO�ɪ��˦�
	public static final int FIELD_SIZE = 25;	//�@�Ӯ�l���j�p

	private int mineValue;	//Field������a�p��
	private int x;	//Field�����
	private int y;	//Field���a����
	private int style;	//Field���˦�

	/**
	 * Field���c�y��k
	 * @param x ���
	 * @param y �a����
	 */
	public Field(int x, int y) {
		this.x = x;
		this.y = y;
		style = STYLE_COVERED;	//��l�Ƽ˦����л\
		mineValue = 0;	//��l�Ƹ�Field�����a�p�A��mineValue�Ȭ�0�Ӫ��
	}

	/**
	 * �����Field�����
	 * @return ���
	 */
	public int getX() {
		return x;
	}

	/**
	 * �����Field���a����
	 * @return �a����
	 */
	public int getY() {
		return y;
	}

	/**
	 * �]�m�Y��Field���˦�
	 * @param style �˦�
	 */
	public void setStyle(int style) {
		this.style = style;
	}

	/**
	 * �q�L��mineValue�ȳ]�m��-100��ܸ�Field���a�p
	 */
	public void setMine() {
		mineValue = -100;
	}

	/**
	 * ��^��Field��mineValue
	 * @return mineValue
	 */
	public int getMineValue() {
		return mineValue;
	}

	/**
	 * �]�m��Field���a�p��
	 * @param value �a�p��
	 */
	public void setMineValue(int value) {
		mineValue = value;
	}

	/**
	 * �P�_��Field�O�_�a�p
	 * @return true�A�O�F�_�h�Afalse
	 */
	public boolean isMine() {
		return mineValue == -100;
	}

	/**
	 * �P�_��Field�O�_�Q�аO
	 * @return true�A�Q�аO�Ffalse�A�S�Q�аO
	 */
	public boolean isMarked() {
		return style == STYLE_MARKED;
	}

	/**
	 * �P�_��Field�O�_�л\
	 * @return true�A�л\�Ffalse�A�S�л\
	 */
	public boolean isCovered() {
		return style == STYLE_COVERED;
	}

	/**
	 * �P�_��Field�O�_�w���}
	 * @return true�A�w���}�Ffalse�A�S���}
	 */
	public boolean isOpened() {
		return style == STYLE_OPENED;
	}

	/**
	 * ø�Ϥ�k
	 * @param g Graphics g
	 */
	public void paintField(Graphics g) {
		int yCoordinate = x * FIELD_SIZE + 1;
		int xCoordinate = y * FIELD_SIZE + 1;
		
		if(isCovered()) {//�˦����л\�ɭԩҰ���ø��
			g.setColor(Color.CYAN);
			g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
		}
		
		if(isOpened()) {//�˦������}�ɩҰ���ø��
			if(mineValue > 0) {//���Field������a�p�Ȥj��0��
				g.setColor(Color.WHITE);
				g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
				g.setColor(Color.BLACK);
				g.drawString(mineValue + "", y * FIELD_SIZE + FIELD_SIZE / 2, x * FIELD_SIZE + FIELD_SIZE / 2);
				
			} else if(mineValue == -100) {//���Field�O�a�p��
				g.setColor(Color.RED);
				g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
				
			} else if(mineValue == 0) {//���Field������a�p�Ȭ�0
				g.setColor(Color.WHITE);
				g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
				
			}
		}

		if(isMarked()) {//�˦����аO�ɩҰ���ø��
			g.setColor(Color.CYAN);
			g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
			g.setColor(Color.MAGENTA);
			g.drawString("�H", y * FIELD_SIZE + FIELD_SIZE / 2, x * FIELD_SIZE + FIELD_SIZE / 2);
		}
	}
}