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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.benputao.listen.Client;
import com.benputao.model.User;
import com.benputao.utils.ChatMessageManger;
import com.benputao.view.Frame.PrivateChatFrame;

public class FriendListPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextField txtf_search;
	
	private DefaultListModel<MyListData>model;
	private JList<MyListData> list_friendslist;
	
	private Client client;
	
	public FriendListPanel(Client client) {
		this.client=client;
		setBounds(0, 96, 317, 490);
		setLayout(null);
		
		txtf_search = new JTextField();
		txtf_search.setBounds(0, 0, 317, 29);
		add(txtf_search);
		txtf_search.setColumns(10);
		txtf_search.setText("搜索好友");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 27, 317, 463);
		add(scrollPane);
		
		JPopupMenu popu=new JPopupMenu();
		addPopuMenuItem(popu, "进入聊天");
		addPopuMenuItem(popu, "删除好友");
		addPopuMenuItem(popu, "添加好友");
		
		model=new DefaultListModel<>();
		list_friendslist = new JList<MyListData>(model);
		list_friendslist.setFixedCellHeight(50);
		list_friendslist.setCellRenderer(new MyListCellRenderer());
		list_friendslist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index=list_friendslist.locationToIndex(e.getPoint());
				list_friendslist.setSelectedIndex(index);
				if(e.isMetaDown()){
					popu.show(list_friendslist, e.getX(), e.getY());
				}
				if(e.getClickCount()==2){
					System.out.println(model.get(index).getId());
				}
			}
		});
		
		scrollPane.setViewportView(list_friendslist);
	}

	public void refreshFriendsList(List<User>friends){
		model=new DefaultListModel<>();
		for (User user : friends) {
			model.addElement(new MyListData(user.getNickname(), user.getUsername(), ""+user.getState(), null, null));
		}
		list_friendslist.setModel(model);
		list_friendslist.invalidate();
		//初始化ChatMessageManger
		if (!ChatMessageManger.privateIsInit) {
			ChatMessageManger.initPrivate(friends);
		}
	}
	
	public void friendStateChanged(String username,String state){
		model=(DefaultListModel<MyListData>) list_friendslist.getModel();
		for(int i=0;i<model.size();i++){
			MyListData data=model.getElementAt(i);
			if(data.getId().equals(username)){
				data.setDiscribe(state);
				break;
			}
		}
		list_friendslist.setModel(model);
		list_friendslist.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int index=list_friendslist.getSelectedIndex();
		switch (e.getActionCommand()) {
		case "0":
			MyListData data=model.getElementAt(index);
			User user=new User(data.getId(),null,data.getName());
			PrivateChatFrame frame=new PrivateChatFrame(client, user);
			//在ChatMessageManger注册
			ChatMessageManger.registPrivateChatFrame(user.getUsername(), frame);
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					//注销在ChatMessageManger的管理
					ChatMessageManger.unRegistPrivateChatFrame(user.getUsername());
				}
			});
			frame.setVisible(true);
			break;
		case "1":
			String username=model.getElementAt(index).getId();
			int s=JOptionPane.showConfirmDialog(this, "确定要删除好友："+username+"?",
										  "警告", JOptionPane.OK_CANCEL_OPTION);
			if(s==JOptionPane.OK_OPTION){
				client.delFriend(new User(username,null,null));
			}
			break;
		case "2":
			EnterDialog dialog=new EnterDialog("添加好友");
			JPanel panel=new AddFenRoDialogPanel(dialog, client, AddFenRoDialogPanel.FRIEND);
			dialog.setContentPanel(panel);
			dialog.setVisible(true);
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