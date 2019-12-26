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
 * ���p������
 */
public class MinesFields extends JPanel {
	public static final int ROWS = 16;	//��ӳ��������
	public static final int COLUMNS = 16;	//��ӳ������C��
	public static final int MINES_NUM = 40;		//�a�p���ƥ�
	
	private Map<String, Field> fields;		//��ܹp�줤�Ҧ�Field
	private List<Field> notMineFields;		//��ܩҦ����O�a�p��Field�����X
	private GameRunScript mouseListener;	//��ܹ��Шƥ󪺺�ť��
	
	/**
	 * MinesFields���c�y��k
	 */
	public MinesFields() {
		setPreferredSize(new Dimension(ROWS * Field.FIELD_SIZE, COLUMNS * Field.FIELD_SIZE));	//�]�m���p���O�j�p
		
		fields = new HashMap<String, Field>();	//��hash���ܩҦ���Field
		notMineFields = new ArrayList<Field>();
		mouseListener = new GameRunScript();
		Random random = new Random();
		
		int mineX, mineY;	//�O���ͦ��a�p������
		List<Field> aroundList;		//����������Field������Field�����X
		
		/* �ͦ��p�줤���C��Field */
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				fields.put(x + "," + y, new Field(x, y));	//�Ы�Field��H�A�ç��H�K�[��fields��
			}
		}
		
		/* �H���ͦ�40�Ӧa�p�ó]�m����a�p�� */
		for(int i = 0; i < MINES_NUM; i ++) {
			/* �p�G�ͦ����a�p���Э��_�A���s�]�m�a�p������ */
			do {
				mineX = random.nextInt(COLUMNS);
				mineY = random.nextInt(ROWS);
			} while(isMine(mineX, mineY));
			
			setMine(mineX, mineY);	//�ھڬ�������ȧ��Field�]�m���a�p
			aroundList = getAround(mineX, mineY);		//����Ӧa�p���񪺩Ҧ�Field
			
			/* �M��aroundList�A�⤣�O�a�p��Field���a�p�ȥ[1 */
			for(Field field: aroundList) {
				if(!field.isMine()) {
					field.setMineValue(field.getMineValue() + 1);
				}
			}
			
		}
		
		/* �⤣�O�a�p��Field�K�[��notMineFields�� */
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				if(!getField(x, y).isMine()) {
					notMineFields.add(getField(x, y));
				}
			}
		}
	}
	
	/**
	 * ��������������
	 * @return �����������
	 */
	public GameRunScript getInnerInstance() {
		return mouseListener;
	}
	
	/**
	 * �������A�Ω��{�ι��Цb�C���ɭ��W�ާ@���\��
	 */
	 private class GameRunScript extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			Point p = e.getPoint();		//������I����m�������I
			int y = p.x / Field.FIELD_SIZE;		//��������I�����
			int x = p.y / Field.FIELD_SIZE;		//��������I���a����

			/* �p�G�������Х��� */
			if(e.getButton() == MouseEvent.BUTTON1) {
				open(x, y);
			} 
			/* �p�G�������Х��� */
			if(e.getClickCount() == 2) {
				openAround(x, y);
			}
			/* �p�G�������Хk��,�аO�Ӱ�Ψ����Ӱ쪺�аO */
			if(e.getButton() == MouseEvent.BUTTON3) {
				mark(x, y);
			}
				
		} 
	}
	
	/**
	 * ���Ҧ����O�a�p��Field�A�P�_�O�_�������w�N�}
	 * @return true�A�������w���}�Ffalse�A�����Υ������S���}
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
	 * ��^�@��Field��H
	 * @param x ���
	 * @param y �a����
	 * @return Field��H
	 */
	public Field getField(int x, int y) {
		return fields.get(x + "," + y);
	}
	
	/**
	 * ��^���O�a�p���Ҧ�Field
	 * @param x �YField�����
	 * @param y �YField���a����
	 * @return ���O�a�p���Ҧ�Field�A�s��b���X��
	 */
	public List<Field> getAround(int x, int y) { 
		List<Field> aroundList = new ArrayList<Field>();
		
		for(int m = -1; m <= 1; m ++) {
			for(int n = -1; n <= 1; n ++) {
				/* �p�G�ҿ襤����H���a�p�����A���X�����`�� */
				if(m == 0 && n == 0) {
					continue;
				}
				
				/* ���P�_�O�_������񪺦a�p�����ЬO�_�V�ɡA�Y�_�A�⤣�O�a�p���Ҧ�Field��iaroundList�̭� */
				if(x + m < COLUMNS && x + m >= 0 && y + n >= 0 && y + n < ROWS) {
					aroundList.add(getField(x + m, y + n));
				}
				
			}
		}
		
		return aroundList;
		
	}
	
	/**
	 * ���Field���a�p�Ȭ�0�ɡA���k���}�����Field
	 * @param x ���
	 * @param y �a����
	 */
	public void open(int x, int y) {
		if(isCovered(x, y)) {
			/* �p�G�O��Field�O�a�p���ܡA���}����Field */
			if(isMine(x, y)) {
				explode();
				JOptionPane.showMessageDialog(null, "�A��F");
				return;
			}			
			setOpened(x, y);	//���Field�]�m�����}���A
			repaint();
			
			if(isAllOpened()) {//�p�G�����w�Q���}
				JOptionPane.showMessageDialog(null, "���ߧA�A�AĹ�F�I�I�I");
			}	
			
			/* ���field���a�p�Ȭ�0�~���}����Field */
			if(getMineValue(x, y) == 0) {
				List<Field> aroundList = getAround(x, y);
				
				/* ���k�եΥ��}�YField���񪺩Ҧ�Field */
				for(Field field: aroundList) {
					open(field.getX(), field.getY());
				}
			}
			
		}
	}
	
	/**
	 * ��b��Field���񪺨�L��@�аO��A�Ω�B�z�u���}����Field�v����k
	 * @param x ���
	 * @param y �a����
	 */
	public void openAround(int x, int y) {
		if(isOpened(x, y) && getMineValue(x, y) > 0) {//�p�G��Field�w���}�åB�����񪺦a�p�Ȥj��0
			
			List<Field> aroundList = getAround(x, y);
			int mineNum = 0;
			
			for(Field field: aroundList) {
				/* �p�G��Field�w�Q�аO�AmineNum�[1 */
				if(field.isMarked()) {
					mineNum ++;
				}
			}
			
			/* ���Field���񪺦a�p�ȩMmineNum�۵��ɤ~����u�����ާ@  */
			if(getMineValue(x, y) == mineNum) {
				for(Field field: aroundList) {	
					if(field.isMarked() && !field.isMine()) {
						explode();	//�p�G��Field�Q�аO�B��Field���O�a�p�A���W���}�Ҧ�Field
						JOptionPane.showMessageDialog(null, "�A��F");
					} else if(!field.isMarked() && !field.isMine()) {
						open(field.getX(), field.getY());//�����field���O�a�p�ɽե�open��k
					}
				}
				
			}
		}
		
	}
	
	/**
	 * ��Y��Field�]�m�����}���A
	 * @param x ���
	 * @param y �a����
	 */
	public void setOpened(int x, int y) {
		getField(x, y).setStyle(Field.STYLE_OPENED);
	}
	
	/**
	 * �ھڼаO���A�]�m�䬰�аO�Ψ����аO
	 * @param x ���
	 * @param y �a����
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
	 * ��Y��Field�]�m���Q�аO
	 * @param x ���
	 * @param y �a����
	 */
	public void setMarked(int x, int y) {
		getField(x, y).setStyle(Field.STYLE_MARKED);
	}
	
	/**
	 * �P�_�@��Field�O�_�Q�аO
	 * @param x ���
	 * @param y �a����
	 * @return �Y�Q�аO�Atrue�F�_�h�Afalse
	 */
	public boolean isMarked(int x, int y) {
		return getField(x, y).isMarked();
	}
	
	/**
	 * �P�_�Y��Field�O�_�a�p
	 * @param x ���
	 * @param y �a����
	 * @return true�A�O�a�p�Ffalse�A���O�a�p
	 */
	public boolean isMine(int x, int y) {
		return getField(x, y).isMine();
	}
		
	/**
	 * �q�L��mineValue�ȳ]�m��-100��ܸ�Field���a�p
	 * @param x ���
	 * @param y �a����
	 */
	public void setMine(int x, int y) {
		getField(x, y).setMine();
	}
	
	/**
	 * �P�_��Field�O�_�w���}
	 * @param x
	 * @param y
	 * @return true�A�w�Q���}�Ffalse�A�S���}
	 */
	public boolean isOpened(int x, int y) {
		return getField(x, y).isOpened();
	}
	
	/**
	 * ��o���wField���a�p��
	 * @param x ���
	 * @param y �a����
	 * @return ��Field���a�p��
	 */
	public int getMineValue(int x, int y) {
		return getField(x, y).getMineValue();
	}
	
	/**
	 * �P�_�Y��Field�O�_���л\
	 * @param x ��� 
	 * @param y �a����
	 * @return �Y���л\�Atrue�F�_�h�Afalse
	 */
	public boolean isCovered(int x, int y) {
		return getField(x, y).isCovered();
	}
	
	/**
	 * ���Field���˦��]�m���л\
	 * @param x ���
	 * @param y �a����
	 */
	public void setCovered(int x, int y) {
		getField(x, y).setStyle(Field.STYLE_COVERED);
	}
	
	/**
	 * ���}����Field
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