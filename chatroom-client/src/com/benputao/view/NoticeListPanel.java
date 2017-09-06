package com.benputao.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

public class NoticeListPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public NoticeListPanel() {
		setBounds(0, 96, 317, 490);
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 317, 490);
		add(scrollPane);
		
		JPopupMenu popu=new JPopupMenu();
		popu.add(new JMenuItem("进入聊天"));
		popu.add(new JMenuItem("查看消息"));
		popu.add(new JMenuItem("删除消息"));
		
		DefaultListModel<MyListData>model=new DefaultListModel<>();
		Date date=new Date();
		SimpleDateFormat format=new SimpleDateFormat("hh:mm:ss");
		String time=format.format(date);
		for(int i=0;i<10;i++){
			model.addElement(new MyListData("name"+i,"10000"+i,"discribe"+i,null,time));
		}
		JList<MyListData> list_noticelist = new JList<MyListData>(model);
		list_noticelist.setFixedCellHeight(50);
		list_noticelist.setCellRenderer(new MyListCellRenderer());
		list_noticelist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index=list_noticelist.locationToIndex(e.getPoint());
				list_noticelist.setSelectedIndex(index);
				if(e.isMetaDown()){
					popu.show(list_noticelist, e.getX(), e.getY());
				}
				if(e.getClickCount()==2){
					System.out.println(model.get(index).getId());
				}
			}
		});
		scrollPane.setViewportView(list_noticelist);
	}
}
