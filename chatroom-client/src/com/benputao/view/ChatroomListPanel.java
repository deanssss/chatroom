package com.benputao.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.benputao.listen.Client;
import com.benputao.model.Chatroom;
import com.benputao.utils.ChatMessageManger;
import com.benputao.view.Frame.GroupChatFrame;

public class ChatroomListPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextField txtf_search;
	private JPopupMenu popu;
	private DefaultListModel<MyListData>model;
	private JList<MyListData> list_chatroomlist;
	
	private Client client;
	
	public ChatroomListPanel(Client client) {
		this.client=client;
		setBounds(0, 96, 317, 490);
		setLayout(null);
		
		txtf_search = new JTextField();
		txtf_search.setBounds(0, 0, 317, 29);
		add(txtf_search);
		txtf_search.setColumns(10);
		txtf_search.setText("搜索聊天室");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 27, 317, 463);
		add(scrollPane);
		
		popu=new JPopupMenu();
		addPopuMenuItem(popu, "进入聊天");
		addPopuMenuItem(popu, "退出聊天室");
		addPopuMenuItem(popu, "加入聊天室");
		addPopuMenuItem(popu, "创建聊天室");
		
		model=new DefaultListModel<>();
		list_chatroomlist = new JList<MyListData>(model);
		list_chatroomlist.setFixedCellHeight(50);
		list_chatroomlist.setCellRenderer(new MyListCellRenderer());
		list_chatroomlist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index=list_chatroomlist.locationToIndex(e.getPoint());
				list_chatroomlist.setSelectedIndex(index);
				if(e.isMetaDown()){
					popu.show(list_chatroomlist, e.getX(), e.getY());
				}
				if(e.getClickCount()==2){
					System.out.println(model.get(index).getId());
				}
			}
		});
		scrollPane.setViewportView(list_chatroomlist);
	}
	
	public void refreshChatroomsList(List<Chatroom>chatrooms){
		model = new DefaultListModel<>();
		for (Chatroom chatroom : chatrooms) {
			MyListData data=new MyListData(chatroom.getName(), chatroom.getId(), "闲聊灌水斗图休闲", null, null);
			model.addElement(data);
		}
		list_chatroomlist.setModel(model);
		list_chatroomlist.invalidate();
		//初始化chatmessagemanger
		if(ChatMessageManger.groupIsInit==false){
			ChatMessageManger.initGroup(chatrooms);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int index=list_chatroomlist.getSelectedIndex();
		switch (e.getActionCommand()) {
			case "0":	//打开聊天窗口
				MyListData data=model.getElementAt(index);
				Chatroom room=new Chatroom(data.getId(),data.getName(),null);
				GroupChatFrame frame=new GroupChatFrame(client, room);
				ChatMessageManger.registGroupChatFrame(room.getId(), frame);
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						ChatMessageManger.unRegistGroupChatFrame(room.getId());
					}
				});
				frame.setVisible(true);
				break;
			case "1":	
				
				break;
			case "2":	//加入聊天室
				EnterDialog dialog=new EnterDialog("加入聊天室");
				JPanel panel=new AddFenRoDialogPanel(dialog, client, AddFenRoDialogPanel.ROOM);
				dialog.setContentPanel(panel);
				dialog.setVisible(true);
				break;
			case "3":
				
				break;
			default:
				break;
		}
	}
	
	/**
	 * 向弹出菜单中添加菜单项与监听事件
	 * @param menu
	 * @param str
	 */
	private void addPopuMenuItem(JPopupMenu menu,String str){
		JMenuItem item=new JMenuItem(str);
		item.setActionCommand(""+menu.getComponentCount());
		item.addActionListener(this);
		menu.add(item);
	}
}
