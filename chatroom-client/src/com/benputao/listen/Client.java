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
	 * 连接服务器
	 */
	public void linkServer(){
		try {
			client_socket=new Socket(address, port);
			clientListen();
		} catch (IOException e) {
			System.out.println("服务器拒绝接受你的请求，并假装自己在睡觉！");
			e.printStackTrace();
		}
	}
	
	/**
	 * 提供用户名密码登录服务器
	 * @param username
	 * @param password
	 */
	public void login(String username,CharSequence password){
		linkServer();	//链接服务器
		//封装登陆请求
		MsgData msg=new MsgData();
		msg.setType(MsgData.Type.ACTION);
		ActionData data=new ActionData();
		data.setAction(ActionData.Action.LOGIN);
		data.setUser(new User(username,password,null));
		msg.setData(data);
		//发送请求数据
		sendMsg(msg);
	}
	
	/**
	 * 退出登录
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
	 * 注册用户
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
						//自我处理
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
	 * 获取好友列表请求
	 * @param user 用户名信息
	 */
	public void getFriends(User user){
		MsgData msg=new MsgData(MsgData.Type.ACTION, null);
		ActionData data=new ActionData(ActionData.Action.GETFRIENDS,user);
		msg.setData(data);
		sendMsg(msg);
	}
	
	/**
	 * 添加好友请求
	 * @param user 存储好友用户名
	 */
	public void addFriend(User user){
		MsgData msg=new MsgData(MsgData.Type.ACTION, null);
		ActionData data=new ActionData(ActionData.Action.ADDFRIEND,user);
		msg.setData(data);
		sendMsg(msg);
	}
	
	/**
	 * 接受好友请求
	 * @param username 请求的用户
	 * @param room 请求加入的房间 好友请求此项为空
	 * @param f 是否接受
	 */
	public void accept(String username,String room,boolean f){
		MsgData msg=new MsgData(MsgData.Type.ACTION,null);
		ActionData data=new ActionData(null,new User(username,null,room));
		msg.setData(data);
		if (f) {	//用户接受请求
			data.setAction(ActionData.Action.ACCEPT);
		}else {
			data.setAction(ActionData.Action.REFUSE);
		}
		sendMsg(msg);
	}
	
	/**
	 * 删除好友
	 * @param user
	 */
	public void delFriend(User user){
		MsgData msg=new MsgData(MsgData.Type.ACTION,null);
		ActionData data=new ActionData(ActionData.Action.DELFRIEND,user);
		msg.setData(data);
		sendMsg(msg);
	}
	
	/**
	 * 向服务器发送聊天消息
	 * @param message
	 */
	public void sendChatMessage(Message message){
		MsgData msg=new MsgData(MsgData.Type.MESSAGE, message);
		sendMsg(msg);
	}
	
	/**
	 * 发送获取聊天室列表请求
	 * @param user
	 */
	public void getChatrooms(User user){
		ActionData data=new ActionData(Action.GETCHATROOMS, user);
		MsgData msg=new MsgData(Type.ACTION,data);
		sendMsg(msg);
	}
	
	/**
	 * 发送加入聊天室请求
	 * @param room
	 */
	public void joinChatroom(Chatroom room){
		//使用uer存储id与msg
		ActionData data=new ActionData(Action.JOINCHATROOM,new User(room.getId(),null,room.getName()));
		MsgData msg=new MsgData(Type.ACTION,data );
		sendMsg(msg);
	}
	
	/**
	 * 发送消息到服务器
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
	 * 客户端监听
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
						//将服务器返回数据交由linstener处理
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
