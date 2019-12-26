package mines;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * ���p�D�ج[��
 */
public class MinesFrame extends JFrame{
	
	private MinesFields ms;		//��ܱ��p�C�����O
	private JButton btnStart;	//��ܡu���s�}�l�v���s
	
	/**
	 * �c�y��k
	 */
	public MinesFrame(){
		setSize(500, 500);	//�]�m����j�p
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);	//�]�m�����̹�����
		setTitle("Mine");		//�]�m���D��Mine
		setResizable(false);		//�����\�����Y��
		setLayout(new FlowLayout());	//�]�m�G���޲z��

		ms = new MinesFields();		//�s�س�������H
		btnStart = new JButton("���s�}�l");	//�s�ث��s

		add(btnStart);	//����s�K�[��ج[��
		add(ms);		//�ⱽ�p�����K�[��ج[��

		/* ���U���Шƥ� */
		addMouseListener(ms.getInnerInstance());
		ms.addMouseListener(ms.getInnerInstance());

		/* �ϥΰΦW���������覡��ť���s�ƥ�A���C�����s�}�l */
		btnStart.addActionListener(new Restart());

		setVisible(true);
	}
	
	/**
	 * �������A�Ω��{�I�����s���s�}�l�C�����\��
	 */
	private class Restart implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			remove(ms);		//���������O
			ms = new MinesFields();	//�A�s��
			add(ms);	//�K�[�s�ت�MinesFields��H

			/* ���U���Шƥ� */
			addMouseListener(ms.getInnerInstance());
			ms.addMouseListener(ms.getInnerInstance());

			setVisible(true);
		}
	}

	public static void main(String[] args) {
		new MinesFrame();
	}

}