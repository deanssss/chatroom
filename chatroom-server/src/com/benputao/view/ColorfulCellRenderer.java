package com.benputao.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ColorfulCellRenderer extends JLabel implements ListCellRenderer<Object> {
	private static final long serialVersionUID = 1L;
	private Color[] colors = new Color[]{new Color(0xB0E0E6), Color.WHITE};

	 public ColorfulCellRenderer() {
		 setOpaque(true);
	 }

	 @Override
	 public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		 		setText(value.toString());
		 		Color background;
		 		Color foreground;
		 		Color light_yellow=new Color(0xFFA54F);
		 		//��ǰRenderer�Ƿ�����קĿ��
		 		JList.DropLocation dropLocation = list.getDropLocation();
		 		if (dropLocation != null && !dropLocation.isInsert()
		 								 && dropLocation.getIndex() == index) {
		 			background = light_yellow;
		 			foreground = Color.WHITE;
		 			//��ǰRenderer�Ƿ�ѡ��
		 		} else if (isSelected) {
		 			background = light_yellow;
		 			foreground = Color.WHITE;
		 		} else {
		 			background = colors[index%2];
		 			foreground = Color.BLACK;
		 		}
		 		setBackground(background);
		 		setForeground(foreground);
		 		return this;
	 	}
	}
