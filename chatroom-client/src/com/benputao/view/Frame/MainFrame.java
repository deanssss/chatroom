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
			JOptionPane.showMessageDialog(this, "�������ѹرգ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
			card.show(contentPane, "login");
			card.removeLayoutComponent(mainPanel);
			mainPanel=null;
			break;
		case Notification.ADDFRIEND:
			int r=JOptionPane.showConfirmDialog(this, "�û���"+parts[0]+"��������������֤��"+parts[1],
												"��Ӻ�������", JOptionPane.OK_CANCEL_OPTION);
			boolean f=(r==JOptionPane.OK_OPTION);
			client.accept(parts[0], null,f);
			break;
		case Notification.ACCEPTFRIEND:
			JOptionPane.showMessageDialog(this, "�û�<"+parts[0]+">ͬ�����������", "֪ͨ", JOptionPane.INFORMATION_MESSAGE);
			//ˢ���û��б�
			ChatMessageManger.addRecord(parts[0], Message.PRIVATE);
			client.getFriends(user);
			break;
		case Notification.DELFRIEND:
			JOptionPane.showMessageDialog(this, notice.getMsg(), "֪ͨ", JOptionPane.INFORMATION_MESSAGE);
			String[]ps=notice.getMsg().split("\\|");
			//ˢ���û��б�
			ChatMessageManger.removeRecord(ps[2], Message.PRIVATE);
			client.getFriends(user);
			break;
		case Notification.OTHER:
			JOptionPane.showMessageDialog(this, ""+notice.getMsg(), "��������Ϣ", JOptionPane.INFORMATION_MESSAGE);
			break;
		case Notification.JOINROOM:
			String[]s1=notice.getMsg().split("\\|");
			int s=JOptionPane.showConfirmDialog(this, "�û�<"+s1[0]+">�����Ⱥ<"+s1[2]+">��֤��Ϣ��"+s1[3],
												"������Ϣ" , JOptionPane.OK_CANCEL_OPTION);
			boolean b1=(s==JOptionPane.OK_OPTION);
			client.accept(s1[0], s1[2], b1);
			break;
		case Notification.ACCEPTROOM:
			String []s2=notice.getMsg().split("\\|");
			//ˢ���������б�
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
				JOptionPane.showMessageDialog(this, "�û��ѳɹ�ע�ᣡ", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			}
			break;
		case GETFRIENDS:
			if(data.getResult()==Const.Status.SUCCESS&&data.getCode()==Const.Code.QUERRYFRIEND){
				List<User>friends=(List<User>) data.getRes();
				mainPanel.refreshList(friends,null,null);
			}else if (data.getResult()==Const.Status.SUCCESS&&data.getCode()==Const.Code.NOFRIENDS) {
				System.out.println("һ������Ҳû�С���");
				mainPanel.refreshList(new ArrayList<User>(),null,null);
			}
			break;
		case DELFRIEND:
			if (data.getResult()==Const.Status.SUCCESS) {
				JOptionPane.showMessageDialog(this, "���ѳɹ����û�<"+((User) data.getRes()).getUsername()+">������ѹ�ϵ��", "֪ͨ", JOptionPane.INFORMATION_MESSAGE);
				//ˢ�º����б�
				ChatMessageManger.removeRecord(((User) data.getRes()).getUsername(), Message.PRIVATE);
				client.getFriends(user);
			}
			break;
		case SENDMSG:
			if(data.getResult()==Const.Status.FAILURE&&data.getCode()==Const.Code.NOTONLINE){
				JOptionPane.showMessageDialog(this, data.getRes(), "����", JOptionPane.INFORMATION_MESSAGE);
			}
			break;
		case GETCHATROOMS:
			if(data.getResult()==Const.Status.SUCCESS&&data.getCode()==Const.Code.QUERRYCHATROOM){
				List<Chatroom>rooms=(List<Chatroom>) data.getRes();
				mainPanel.refreshList(null, rooms, null);
			}else if (data.getResult()==Const.Status.SUCCESS&&data.getCode()==Const.Code.NOCHATROOM) {
				System.out.println("û��������");
				mainPanel.refreshList(null, new ArrayList<Chatroom>(), null);
			}
			break;
		case JOINCHATROOM:
			JOptionPane.showMessageDialog(this, data.getRes(), "��Ϣ", JOptionPane.INFORMATION_MESSAGE);
			break;
		default:
			break;
		}
	}
}
