package com.benputao.view.Frame;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.benputao.config.Const;
import com.benputao.dtos.Message;
import com.benputao.dtos.MsgData;
import com.benputao.dtos.Notification;
import com.benputao.dtos.Reply;
import com.benputao.listen.Client;
import com.benputao.listen.Client.ClientListener;
import com.benputao.model.Chatroom;
import com.benputao.model.User;
import com.benputao.utils.ChatMessageManger;
import com.benputao.view.LoginPanel;
import com.benputao.view.MainPanel;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private CardLayout card;
	
	private JPanel loginpanel;
	private Client client;
	private MainPanel mainPanel;
	private User user;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
					MainFrame frame = new MainFrame(new Client("192.168.104", 612));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame(Client client) {
		this.client=client;
		setTitle("Biu Biu");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 371, 715);
		contentPane = new JPanel();
		setContentPane(contentPane);
		card=new CardLayout();
		contentPane.setLayout(card);
		
		loginpanel=new LoginPanel(this.client);
		loginpanel.setBounds(0, 0, 317, 635);
		contentPane.add(loginpanel,"login");
		
		this.client.setClientListener(new ClientListener() {
			@Override
			public void clientReply(MsgData msg) {
				dealMsg(msg);
			}
		});
	}
	
	private void dealMsg(MsgData msg) {
		if(msg==null)return;
		switch (msg.getType()) {
		case REPLY:
			dealData((Reply) msg.getData());
			break;
		case NOTIFICATION:
			dealNotice((Notification) msg.getData());
			break;
		case MESSAGE:
			dealMessage((Message)msg.getData());
			break;
		default:
			break;
		}
	}
	
	private void dealMessage(Message message){
		if(message==null) return;
		ChatMessageManger.receive(message);
	}
	
	private void dealNotice(Notification notice) {
		if (notice==null)return;
		String[]parts=notice.getMsg().split("\\|");
		switch (notice.getCode()) {
		case Notification.USERLOGIN:
			if(mainPanel!=null)mainPanel.stateChanged(parts[1], "ONLINE", "friends");
			break;
		case Notification.USERLOGOUT:
			if(mainPanel!=null)mainPanel.stateChanged(parts[1], "OFFLINE", "friends");
			break;
		case Notification.SERVERCLOSED:
			JOptionPane.showMessageDialog(this, "服务器已关闭！", "提示", JOptionPane.WARNING_MESSAGE);
			card.show(contentPane, "login");
			card.removeLayoutComponent(mainPanel);
			mainPanel=null;
			break;
		case Notification.ADDFRIEND:
			int r=JOptionPane.showConfirmDialog(this, "用户："+parts[0]+"发来好友请求验证："+parts[1],
												"添加好友请求", JOptionPane.OK_CANCEL_OPTION);
			boolean f=(r==JOptionPane.OK_OPTION);
			client.accept(parts[0], null,f);
			break;
		case Notification.ACCEPTFRIEND:
			JOptionPane.showMessageDialog(this, "用户<"+parts[0]+">同意了你的请求！", "通知", JOptionPane.INFORMATION_MESSAGE);
			//刷新用户列表
			ChatMessageManger.addRecord(parts[0], Message.PRIVATE);
			client.getFriends(user);
			break;
		case Notification.DELFRIEND:
			JOptionPane.showMessageDialog(this, notice.getMsg(), "通知", JOptionPane.INFORMATION_MESSAGE);
			String[]ps=notice.getMsg().split("\\|");
			//刷新用户列表
			ChatMessageManger.removeRecord(ps[2], Message.PRIVATE);
			client.getFriends(user);
			break;
		case Notification.OTHER:
			JOptionPane.showMessageDialog(this, ""+notice.getMsg(), "服务器消息", JOptionPane.INFORMATION_MESSAGE);
			break;
		case Notification.JOINROOM:
			String[]s1=notice.getMsg().split("\\|");
			int s=JOptionPane.showConfirmDialog(this, "用户<"+s1[0]+">想加入群<"+s1[2]+">验证消息："+s1[3],
												"请求消息" , JOptionPane.OK_CANCEL_OPTION);
			boolean b1=(s==JOptionPane.OK_OPTION);
			client.accept(s1[0], s1[2], b1);
			break;
		case Notification.ACCEPTROOM:
			String []s2=notice.getMsg().split("\\|");
			//刷新聊天室列表
			ChatMessageManger.addRecord(s2[0], Message.GROUP);
			client.getChatrooms(user);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void dealData(Reply data){
		if (data==null)return;
		switch (data.getAction()) {
		case LOGIN:
			if(data.getResult()==Const.Status.SUCCESS&&data.getCode()==Const.Code.LOGINSUCCESS){
				mainPanel=new MainPanel((User) data.getRes(),client);
				user=(User) (data.getRes());
				contentPane.add(mainPanel, "main");
				card.show(contentPane, "main");
			}
			break;
		case LOGOUT:
			if(data.getResult()==Const.Status.SUCCESS&&data.getCode()==Const.Code.LOGOUT){
				card.show(contentPane, "login");
				card.removeLayoutComponent(mainPanel);
				mainPanel=null;
			}
			break;
		case REGIST:
			if(data.getResult()==Const.Status.SUCCESS&&data.getCode()==Const.Code.REGISTSUCCESS){
				JOptionPane.showMessageDialog(this, "用户已成功注册！", "提示", JOptionPane.INFORMATION_MESSAGE);
			}
			break;
		case GETFRIENDS:
			if(data.getResult()==Const.Status.SUCCESS&&data.getCode()==Const.Code.QUERRYFRIEND){
				List<User>friends=(List<User>) data.getRes();
				mainPanel.refreshList(friends,null,null);
			}else if (data.getResult()==Const.Status.SUCCESS&&data.getCode()==Const.Code.NOFRIENDS) {
				System.out.println("一个朋友也没有……");
				mainPanel.refreshList(new ArrayList<User>(),null,null);
			}
			break;
		case DELFRIEND:
			if (data.getResult()==Const.Status.SUCCESS) {
				JOptionPane.showMessageDialog(this, "你已成功与用户<"+((User) data.getRes()).getUsername()+">解除好友关系！", "通知", JOptionPane.INFORMATION_MESSAGE);
				//刷新好友列表
				ChatMessageManger.removeRecord(((User) data.getRes()).getUsername(), Message.PRIVATE);
				client.getFriends(user);
			}
			break;
		case SENDMSG:
			if(data.getResult()==Const.Status.FAILURE&&data.getCode()==Const.Code.NOTONLINE){
				JOptionPane.showMessageDialog(this, data.getRes(), "提醒", JOptionPane.INFORMATION_MESSAGE);
			}
			break;
		case GETCHATROOMS:
			if(data.getResult()==Const.Status.SUCCESS&&data.getCode()==Const.Code.QUERRYCHATROOM){
				List<Chatroom>rooms=(List<Chatroom>) data.getRes();
				mainPanel.refreshList(null, rooms, null);
			}else if (data.getResult()==Const.Status.SUCCESS&&data.getCode()==Const.Code.NOCHATROOM) {
				System.out.println("没有聊天室");
				mainPanel.refreshList(null, new ArrayList<Chatroom>(), null);
			}
			break;
		case JOINCHATROOM:
			JOptionPane.showMessageDialog(this, data.getRes(), "消息", JOptionPane.INFORMATION_MESSAGE);
			break;
		default:
			break;
		}
	}
}
