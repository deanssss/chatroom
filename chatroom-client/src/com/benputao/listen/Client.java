package com.benputao.listen;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.benputao.config.Const;
import com.benputao.dtos.ActionData;
import com.benputao.dtos.ActionData.Action;
import com.benputao.dtos.Message;
import com.benputao.dtos.MsgData;
import com.benputao.dtos.Reply;
import com.benputao.dtos.MsgData.Type;
import com.benputao.model.Chatroom;
import com.benputao.model.User;

public class Client {
	private String address="127.0.0.1";
	private int port=612;
	private Socket client_socket=null;
	
	private ClientListener listener;
	public interface ClientListener{
		public void clientReply(MsgData msg);
	}
	public void setClientListener(ClientListener listener){
		this.listener=listener;
	}
	
	public Client(String address,int port) {
		this.address=address;
		this.port=port;
	}
	
	/**
	 * ���ӷ�����
	 */
	public void linkServer(){
		try {
			client_socket=new Socket(address, port);
			clientListen();
		} catch (IOException e) {
			System.out.println("�������ܾ�����������󣬲���װ�Լ���˯����");
			e.printStackTrace();
		}
	}
	
	/**
	 * �ṩ�û��������¼������
	 * @param username
	 * @param password
	 */
	public void login(String username,CharSequence password){
		linkServer();	//���ӷ�����
		//��װ��½����
		MsgData msg=new MsgData();
		msg.setType(MsgData.Type.ACTION);
		ActionData data=new ActionData();
		data.setAction(ActionData.Action.LOGIN);
		data.setUser(new User(username,password,null));
		msg.setData(data);
		//������������
		sendMsg(msg);
	}
	
	/**
	 * �˳���¼
	 * @param username
	 * @param password
	 */
	public void logout(String username,CharSequence password){
		MsgData msg=new MsgData(MsgData.Type.ACTION,null);
		ActionData data=new ActionData(ActionData.Action.LOGOUT,null);
		data.setUser(new User(username, password, null));
		msg.setData(data);
		sendMsg(msg);
	}
	
	/**
	 * ע���û�
	 * @param user
	 * @return
	 */
	public boolean regist(User user){
		try {
			client_socket=new Socket(address, port);
			MsgData msg=new MsgData();
			msg.setType(MsgData.Type.ACTION);
			ActionData data=new ActionData();
			data.setAction(ActionData.Action.REGIST);
			data.setUser(user);
			msg.setData(data);
			if (client_socket!=null) {
				try {
					OutputStream outputStream = client_socket.getOutputStream();
					InputStream inputStream= client_socket.getInputStream();
					Scanner in=new Scanner(inputStream);
					PrintWriter out=new PrintWriter(outputStream,true);
					out.println(msg.getJson());
					while(in.hasNext()){
						String json=in.nextLine();
						System.err.println(json);
						MsgData reply=MsgData.getDataFromJson(json);
						//���Ҵ���
						Reply r=(Reply) reply.getData();
						if(r!=null){
							if(r.getResult()==Const.Status.SUCCESS){
								in.close();
								out.close();
								return true;
							}
						}
					}
					in.close();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}
	
	/**
	 * ��ȡ�����б�����
	 * @param user �û�����Ϣ
	 */
	public void getFriends(User user){
		MsgData msg=new MsgData(MsgData.Type.ACTION, null);
		ActionData data=new ActionData(ActionData.Action.GETFRIENDS,user);
		msg.setData(data);
		sendMsg(msg);
	}
	
	/**
	 * ��Ӻ�������
	 * @param user �洢�����û���
	 */
	public void addFriend(User user){
		MsgData msg=new MsgData(MsgData.Type.ACTION, null);
		ActionData data=new ActionData(ActionData.Action.ADDFRIEND,user);
		msg.setData(data);
		sendMsg(msg);
	}
	
	/**
	 * ���ܺ�������
	 * @param username ������û�
	 * @param room �������ķ��� �����������Ϊ��
	 * @param f �Ƿ����
	 */
	public void accept(String username,String room,boolean f){
		MsgData msg=new MsgData(MsgData.Type.ACTION,null);
		ActionData data=new ActionData(null,new User(username,null,room));
		msg.setData(data);
		if (f) {	//�û���������
			data.setAction(ActionData.Action.ACCEPT);
		}else {
			data.setAction(ActionData.Action.REFUSE);
		}
		sendMsg(msg);
	}
	
	/**
	 * ɾ������
	 * @param user
	 */
	public void delFriend(User user){
		MsgData msg=new MsgData(MsgData.Type.ACTION,null);
		ActionData data=new ActionData(ActionData.Action.DELFRIEND,user);
		msg.setData(data);
		sendMsg(msg);
	}
	
	/**
	 * �����������������Ϣ
	 * @param message
	 */
	public void sendChatMessage(Message message){
		MsgData msg=new MsgData(MsgData.Type.MESSAGE, message);
		sendMsg(msg);
	}
	
	/**
	 * ���ͻ�ȡ�������б�����
	 * @param user
	 */
	public void getChatrooms(User user){
		ActionData data=new ActionData(Action.GETCHATROOMS, user);
		MsgData msg=new MsgData(Type.ACTION,data);
		sendMsg(msg);
	}
	
	/**
	 * ���ͼ�������������
	 * @param room
	 */
	public void joinChatroom(Chatroom room){
		//ʹ��uer�洢id��msg
		ActionData data=new ActionData(Action.JOINCHATROOM,new User(room.getId(),null,room.getName()));
		MsgData msg=new MsgData(Type.ACTION,data );
		sendMsg(msg);
	}
	
	/**
	 * ������Ϣ��������
	 */
	private void sendMsg(MsgData msg){
		new Thread(){
			public void run() {
				if (client_socket!=null) {
					try {
						OutputStream outputStream = client_socket.getOutputStream();
						PrintWriter out=new PrintWriter(outputStream,true);
						out.println(msg.getJson());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
	
	
	/**
	 * �ͻ��˼���
	 */
	private void clientListen(){
		if (client_socket==null) return;
		new Thread(){
			public void run() {
				InputStream inputStream;
				try {
					inputStream = client_socket.getInputStream();
					Scanner in=new Scanner(inputStream);
					while(in.hasNext()){
						String json=in.nextLine();
						System.err.println(json);
						MsgData reply=MsgData.getDataFromJson(json);
						//���������������ݽ���linstener����
						listener.clientReply(reply);
					}
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
