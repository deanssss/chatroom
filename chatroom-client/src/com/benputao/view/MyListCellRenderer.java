package com.benputao.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class MyListCellRenderer implements ListCellRenderer<MyListData> {
	@Override
	public Component getListCellRendererComponent(JList<? extends MyListData> list, MyListData value, int index,
			boolean isSelected, boolean cellHasFocus) {
		Component listitem=new MyListItem(value.getImgpath(),value.getName()+"("+value.getId()+")",value.getDiscribe(),value.getTime());
		if (index%2==0) {
			listitem.setBackground(new Color(0xffffcc));
		}
		if (isSelected) {
			listitem.setBackground(new Color(0x66ccff));
		}
		return listitem;
	}

}
