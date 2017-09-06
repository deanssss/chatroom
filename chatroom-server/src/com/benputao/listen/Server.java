package com.benputao.listen;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.benputao.config.Const;
import com.benputao.dtos.ActionData.Action;
import com.benputao.dtos.MsgData;
import com.benputao.dtos.Notification;
import com.benputao.dtos.Reply;
import com.benputao.model.User;
import com.benputao.utils.ClientsManger;

public class Server{
	public static final int WORKING=1;
	public static final int STOP=0;
	
	private int port;
	private int status;
	private ServerSocket server;
	private Thread serverThread;
	private Thread clientManageThread;
	
	private ServerListener listener;
	public interface ServerListener{
		public void serverNotify(MsgData msg);
		public void refreshStatus(int user_count, int online_user_count, int chatroom_count, int server_port, boolean server_status);
		public void appendMsg(String msg);
	};
	
	public Server(int port) {
		this.port=port;
		status=STOP;
	}
	
	public void setServerPort(int port){
		this.port=port;
	}
	
	public void setSeverListener(ServerListener listener){
		this.listener=listener;
	}
	
	public void start() {
		status=WORKING;
		init();
		serverThread.start();
		clientManageThread.start();
	}
	
	public void stop(){
		status=STOP;
		try {
			new Socket("127.0.0.1",port).close();	//���server.accept()������
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void init(){
		serverThread=new Thread(){		//�����������߳������ͻ�������
			public void run(){
				try {
					server=new ServerSocket(port);
					System.out.println("Thread-"+Thread.currentThread().getName()+":���������������С���");
					Socket incoming=server.accept();
					while(status==WORKING){
						incoming = server.accept();
						Runnable runnable=new ClientManger(incoming,listener);
						Thread thread=new Thread(runnable);
						thread.start();
					}
					Notification notice=new Notification(Notification.SERVERCLOSED,"�������ѹرգ�");
					sendNotification(notice);	//��ͻ��˷��ͷ������ر�֪ͨ
					closeAllConnection();	//�ر����������Դ
					server.close();
					System.out.println("Thread-"+Thread.currentThread().getName()+":����ֹͣ��");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Thread-"+Thread.currentThread().getName()+":�߳���ֹ��");
			}
		};
		serverThread.setName("serverThread");
		clientManageThread=new Thread(){
			public void run() {
				while(status==WORKING){
					List<String>list=new ArrayList<>();
					for (Map.Entry<String, ClientManger> entry : ClientsManger.getClientManagers().entrySet()) {
						Socket socket=entry.getValue().getIncoming();
						if (socket.isClosed()) {
							list.add(entry.getKey());
						}
					}
					//ѭ�����������û����������������淢����Ϣ
					for (String key : list) {
						ClientsManger.removeClientManager(key);
						MsgData msg=new MsgData(MsgData.Type.REPLY,null);
						Reply data=new Reply(Action.LOGOUT,Const.Status.SUCCESS,0,new User(key,null,null));
						msg.setData(data);
						listener.serverNotify(msg);
					}
					System.out.println("Client count:"+ClientsManger.getSize());
					try {
						sleep(1000*10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		clientManageThread.setName("clientManageThread");
	}
	
	/**
	 * �����������û����ͷ�����֪ͨ
	 * @param msg
	 */
	public void sendNotification(Notification notice){
		MsgData msg=new MsgData();
		msg.setType(MsgData.Type.NOTIFICATION);
		msg.setData(notice);
		for (Map.Entry<String, ClientManger> entry : ClientsManger.getClientManagers().entrySet()) {
			PrintWriter out=entry.getValue().getOut();
			out.println(msg.getJson());
		}
	}
	
	/**
	 * �ر���������������ӵĿͻ���socket���Ƴ�ClientsManger�����е�Manger
	 */
	public void closeAllConnection(){
		List<String>list=new ArrayList<>();
		for (Map.Entry<String, ClientManger> entry : ClientsManger.getClientManagers().entrySet()) {
			try {
				Socket socket=entry.getValue().getIncoming();
				if (!socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			list.add(entry.getKey());
		}
		for (String key : list) {
			ClientsManger.removeClientManager(key);
		}
		//listener.refreshStatus(0, 0, 0, 0, false);
	}

}