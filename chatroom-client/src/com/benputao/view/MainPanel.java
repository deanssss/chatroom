package com.benputao.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.benputao.listen.Client;
import com.benputao.model.Chatroom;
import com.benputao.model.User;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private User user;
	private Client client;
	
	private JPanel panel_list;
	private JLabel lbl_discribe;
	private JLabel lbl_username;
	private JLabel lb_nickname;
	private JPanel panel_avatar;
	
	private FriendListPanel panel_friendlist;
	private ChatroomListPanel panel_chatroomlist;
	private NoticeListPanel panel_noticelist;
	
	public MainPanel(User u,Client client) {
		setBounds(0, 0, 317, 635);
		setBackground(UIManager.getColor("Button.light"));
		setLayout(null);
		this.user=u;
		this.client=client;
		//---------------用户信息panel----------------//
		JPanel panel_userbar = new JPanel();
		panel_userbar.setBackground(SystemColor.menu);
		panel_userbar.setBorder(new LineBorder(Color.GRAY));
		panel_userbar.setBounds(0, 0, 317, 96);
		add(panel_userbar);
		panel_userbar.setLayout(null);
		
		panel_avatar = new JPanel();
		panel_avatar.setToolTipText("用户头像");
		panel_avatar.setBounds(10, 10, 76, 76);
		panel_userbar.add(panel_avatar);
		
		lb_nickname = new JLabel(user.getNickname());
		lb_nickname.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		lb_nickname.setBounds(96, 10, 211, 29);
		panel_userbar.add(lb_nickname);
		
		lbl_username = new JLabel(user.getUsername());
		lbl_username.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lbl_username.setBounds(96, 49, 211, 15);
		panel_userbar.add(lbl_username);
		
		lbl_discribe = new JLabel("这是一个有个性的个性签名！");
		lbl_discribe.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lbl_discribe.setBounds(96, 74, 211, 15);
		panel_userbar.add(lbl_discribe);
		
		//---------------列表panel---------------//
		panel_list = new JPanel();
		panel_list.setBorder(new LineBorder(Color.GRAY));
		panel_list.setBounds(0, 96, 317, 490);
		add(panel_list);
		CardLayout card=new CardLayout();
		panel_list.setLayout(card);
		
		panel_friendlist = new FriendListPanel(client);
		client.getFriends(user);	//向服务器发送好友列表请求
		panel_chatroomlist = new ChatroomListPanel(client);
		client.getChatrooms(user);
		panel_noticelist = new NoticeListPanel();
		
		panel_chatroomlist.add(new JLabel("chatroomlist"));
		panel_noticelist.add(new JLabel("noticelist"));
		panel_list.add(panel_friendlist, "friendlist");
		panel_list.add(panel_chatroomlist,"chatroomlist");
		panel_list.add(panel_noticelist,"noticelist");
		//-------------菜单栏panel---------------//
		JPanel panel_menubar = new JPanel();
		panel_menubar.setBackground(Color.WHITE);
		panel_menubar.setBorder(null);
		panel_menubar.setBounds(0, 586, 317, 49);
		add(panel_menubar);
		panel_menubar.setLayout(null);
		
		MyMenuLabel label_mianmenu = new MyMenuLabel("主菜单");
		label_mianmenu.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_mianmenu.setOpaque(true);
		label_mianmenu.setHorizontalAlignment(SwingConstants.CENTER);
		label_mianmenu.setBounds(0, 0, 49, 49);
		panel_menubar.add(label_mianmenu);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(label_mianmenu, popupMenu);
		
		MyMenuLabel label_chatroom = new MyMenuLabel("聊天室");
		label_chatroom.setOpaque(true);
		label_chatroom.setHorizontalAlignment(SwingConstants.CENTER);
		label_chatroom.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_chatroom.setBounds(142, 0, 73, 49);
		panel_menubar.add(label_chatroom);
		label_chatroom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				card.show(panel_list, "chatroomlist");
			}
		});
		
		MyMenuLabel label_notice = new MyMenuLabel("消息通知");
		label_notice.setOpaque(true);
		label_notice.setHorizontalAlignment(SwingConstants.CENTER);
		label_notice.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_notice.setBounds(225, 0, 92, 49);
		panel_menubar.add(label_notice);
		label_notice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				card.show(panel_list, "noticelist");
			}
		});
		
		MyMenuLabel label_friendlist = new MyMenuLabel("好友列表");
		label_friendlist.setOpaque(true);
		label_friendlist.setHorizontalAlignment(SwingConstants.CENTER);
		label_friendlist.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_friendlist.setBounds(59, 0, 73, 49);
		panel_menubar.add(label_friendlist);
		label_friendlist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				card.show(panel_list, "friendlist");
			}
		});
	}
	
	/**
	 * 设置弹出主菜单
	 * @param menu
	 * @param popup
	 */
	private void addPopup(MyMenuLabel menu, final JPopupMenu popup) {
		JMenuItem mntmNewMenuItem = new JMenuItem("帮      助");
		popup.add(mntmNewMenuItem);
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("设      置");
		popup.add(mntmNewMenuItem_2);
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("关      于");
		popup.add(mntmNewMenuItem_1);
		popup.add(new AbstractAction("退出登录") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				client.logout(user.getUsername(), user.getPassword());
			}
		});
		//为菜单栏添加鼠标效果
		menu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if(menu.getState()==MyMenuLabel.NORMAL){
					menu.setState(MyMenuLabel.HOVER);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (menu.getState()==MyMenuLabel.HOVER) {
					menu.setState(MyMenuLabel.NORMAL);
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (menu.getState()!=MyMenuLabel.SELECT) {
					menu.setState(MyMenuLabel.SELECT);
					showMenu(e);
				}else {
					menu.setState(MyMenuLabel.HOVER);
				}
			}
			/**
			 * 在指定位置显示弹出菜单
			 * @param e
			 */
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(),menu.getX(), menu.getY()-25*popup.getComponentCount()-10);
			}
		});
		//为弹出主菜单添加鼠标效果
		popup.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				menu.setState(MyMenuLabel.NORMAL);
			}
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {}
		});
	}
	
	/**
	 * 刷新所有列表
	 * @param friends
	 */
	public void refreshList(List<User>friends,List<Chatroom>chatrooms,List<?>notices){
		if(friends!=null)panel_friendlist.refreshFriendsList(friends);
		if(chatrooms!=null)panel_chatroomlist.refreshChatroomsList(chatrooms);
	}
	
	/**
	 * 更新列表信息
	 */
	public void stateChanged(String username,String state,String kind){
		if (kind.equals("friends")) {
			panel_friendlist.friendStateChanged(username, state);
		}
	}
}

/**
 * 自定义的菜单按钮
 *<br>继承自JLabel
 */
class MyMenuLabel extends JLabel{
	private static final long serialVersionUID = 1L;
	public static final int NORMAL=0;
	public static final int HOVER=1;
	public static final int SELECT=2;
	
	public MyMenuLabel(String name) {
		setText(name);
		setMouseListener();
	}
	
	private int state=NORMAL;
	
	public int getState() 			{	return state;		}
	public void setState(int state) {
		this.state = state;
		setColor(state);
	}
	
	/**
	 * 设置label的鼠标效果
	 */
	private void setMouseListener(){
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if(getState()==MyMenuLabel.NORMAL){
					setState(MyMenuLabel.HOVER);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (getState()==MyMenuLabel.HOVER) {
					setState(MyMenuLabel.NORMAL);
				}
			}
		});
	}
	
	private void setColor(int state) {
		Color[][]colors={
				{Color.black,Color.white},		//普通状态
				{Color.black,new Color(0x6495ED)},	//鼠标覆盖
				{Color.white,new Color(0x00BFee)}   //选定状态
		};
		this.setBackground(colors[state][1]);
		this.setForeground(colors[state][0]);
	}
}
