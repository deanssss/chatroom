package com.benputao.view;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.benputao.config.Const;
import com.benputao.dtos.MsgData;
import com.benputao.dtos.Notification;
import com.benputao.dtos.Reply;
import com.benputao.listen.Server;
import com.benputao.listen.Server.ServerListener;
import com.benputao.model.User;

public class ServerFrame implements ServerListener{
	private JFrame frame;
	private Server server;
	private ServerStatusPanel panel_serverstatus;
	/**
	 * Ĭ�Ϸ���˿�
	 */
	private int server_port=612;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				    org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
					ServerFrame window = new ServerFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerFrame() {
		server=new Server(server_port);
		panel_serverstatus=new ServerStatusPanel(server,this);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("biu biu���������������");
		frame.setAlwaysOnTop(true);
		frame.setBounds(100, 100, 608, 414);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		//��ʼ���˵�
		initializeMenu();

		frame.getContentPane().add(panel_serverstatus,"name_serverstatus");
	}
	
	/**
	 * ��ʼ�������˵�
	 */
	private void initializeMenu(){
		//�˵���
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		JMenu menu = new JMenu("����������");
		menuBar.add(menu);
		JMenuItem startserver = new JMenuItem("��ʼ");
		startserver.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				appendMsg("�����������С���");
				server.setSeverListener(ServerFrame.this);
				server.start();
				appendMsg("�����������ɹ���");
				try {
					//��ȡ����ip��ַ���û��ο�����������Java�ڵײ㷽�����ܱ�������˻�ȡ��IP��ַ���ױ��û���װ��������š�
					appendMsg("�ο�����ip��"+InetAddress.getLocalHost().getHostAddress());
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				refreshStatus(10, 0, 0, server_port, true);
			}
		});
		menu.add(startserver);
		JMenuItem stopserver = new JMenuItem("ֹͣ");
		stopserver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				appendMsg("����ֹͣ����������");
				server.stop();
				appendMsg("�������ѳɹ�ֹͣ��");
				resetStatus();
			}
		});
		menu.add(stopserver);
	}
	
	public void setServerport(int server_port) {
		this.server_port = server_port;
	}
	
	@Override
	public void serverNotify(MsgData msg) {
		switch (msg.getType()) {
			case ACTION:
				break;
			case MESSAGE:
				
				break;
			case REPLY:
				dealActionMsg((Reply) msg.getData());
				break;
			case NOTIFICATION:
				
				break;
		default:
			break;
		}
	}
	
	@Override
	public void refreshStatus(int user_count, int online_user_count, 
										   int chatroom_count, int server_port,
										   boolean server_status) {
		panel_serverstatus.refreshStatus(user_count, online_user_count, chatroom_count, server_port, server_status);
	}
	
	private void resetStatus() {
		panel_serverstatus.resetStatus();
	}
	
	@Override
	public void appendMsg(String msg) {
		panel_serverstatus.appendMsg(msg);
	}
	
	public void dealActionMsg(Reply reply) {
		if(reply.getResult()!=Const.Status.SUCCESS)return;
		Object res=reply.getRes();
		switch (reply.getAction()) {
			case LOGIN:
				panel_serverstatus.userLogin(((User) res).getUsername());
				Notification notice=new Notification(Notification.USERLOGIN, "user:|"+((User) res).getUsername()+"|�����ߣ�");
				server.sendNotification(notice);
				break;
			case LOGOUT:
				panel_serverstatus.userLogout(((User) res).getUsername());
				Notification notice1=new Notification(Notification.USERLOGOUT, "user:|"+((User) res).getUsername()+"|�����ߣ�");
				server.sendNotification(notice1);
				break;
			case REGIST:
				appendMsg(((User) res).getUsername()+"ע��ɹ���");
				break;
			default:
				break;
		}
	}
}