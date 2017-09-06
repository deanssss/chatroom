package com.benputao.utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import com.benputao.dtos.Message;
import com.benputao.model.Chatroom;
import com.benputao.model.User;
import com.benputao.view.Frame.GroupChatFrame;
import com.benputao.view.Frame.PrivateChatFrame;

public class ChatMessageManger {
	private static HashMap<String, MessageRecord>privaterecords=new HashMap<>();
	private static HashMap<String, MessageRecord>grouprecords=new HashMap<>();
	private static HashMap<String, PrivateChatFrame>privateframes=new HashMap<>();
	private static HashMap<String, GroupChatFrame>groupframes=new HashMap<>();
	public static boolean privateIsInit=false;
	public static boolean groupIsInit=false;
	/**
	 * 初始化私聊消息管理
	 * @param friends
	 */
	public static void initPrivate(List<User>friends){
		for (User user : friends) {
			MessageRecord record=new MessageRecord(user.getUsername());
			privaterecords.put(user.getUsername(), record);
		}
		privateIsInit=true;
	}
	
	/**
	 * 初始化群聊消息管理
	 * @param rooms
	 */
	public static void initGroup(List<Chatroom>rooms){
		for (Chatroom chatroom : rooms) {
			MessageRecord record=new MessageRecord(chatroom.getId());
			grouprecords.put(chatroom.getId(), record);
		}
		groupIsInit=true;
	}
	
	/**
	 * 获取与username 用户/群 的近num条聊天记录+所有未读消息
	 * @param username
	 */
	public static List<ChatMessage> getMsg(String username,int num,int kind){
		List<ChatMessage>messages;
		MessageRecord record;
		if(kind==Message.PRIVATE){
			record=privaterecords.get(username);
		}else {
			record=grouprecords.get(username);
		}
		messages=record.getMessage(num);	
		return messages;
	}
	
	/**
	 * 接受到来自username的消息
	 * @param username
	 * @param msg
	 */
	public static void receive(Message message){
		//获取数据
		String username=message.getSenduser();
		String nickname=message.getNickname();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String datetime=format.format(message.getDatetime());
		String msg=message.getMessage();
		//处理消息
		if(message.getType()==Message.PRIVATE){
			MessageRecord record=privaterecords.get(username);
			PrivateChatFrame frame=privateframes.get(username);
			ChatMessage m=new ChatMessage(nickname, datetime, msg);
			if (frame!=null) {
				frame.recieve(m);
				m.setIsread(true);
				record.addMessage(m);
			}else {
				m.setIsread(false);
				record.addMessage(m);
			}
		}else {
			String roomid=message.getRecvuser();
			MessageRecord record=grouprecords.get(roomid);
			GroupChatFrame frame=groupframes.get(roomid);
			ChatMessage m=new ChatMessage(nickname, datetime, msg);
			if (frame!=null) {
				frame.recieve(m);
				m.setIsread(true);
				record.addMessage(m);
			}else {
				m.setIsread(false);
				record.addMessage(m);
			}
		}
	}
	
	/**
	 * 添加记录
	 * @param username
	 * @param kind
	 */
	public static void addRecord(String username,int kind){
		if(kind==Message.PRIVATE){
			MessageRecord record=new MessageRecord(username);
			privaterecords.put(username, record);
		}else {
			MessageRecord record=new MessageRecord(username);
			grouprecords.put(username, record);
		}
		
	}
	
	/**
	 * 移除记录
	 * @param username
	 * @param kind
	 */
	public static void removeRecord(String username,int kind){
		if(kind==Message.PRIVATE){
			PrivateChatFrame frame=privateframes.get(username);
			privaterecords.remove(username);
			if(frame!=null){
				frame.dispose();
			}
		}else {
			GroupChatFrame gFrame=groupframes.get(username);
			grouprecords.remove(username);
			if(gFrame!=null){
				gFrame.dispose();
			}
		}
	}
	
	/**
	 * 发送消息直接添加到相应记录中
	 * @param username
	 * @param message
	 */
	public static void send(String username,ChatMessage message,int kind){
		MessageRecord record;
		if(kind==Message.PRIVATE){
			record=privaterecords.get(username);
		}else {
			record=grouprecords.get(username);
		}
		message.setIsread(true);
		record.addMessage(message);
	}
	
	/**
	 * 注册打开的私聊窗口
	 * @param username
	 * @param frame
	 */
	public static void registPrivateChatFrame(String username,PrivateChatFrame frame){
		privateframes.put(username, frame);
	}
	
	/**
	 * 注册打开的群聊窗口
	 * @param id
	 * @param frame
	 */
	public static void registGroupChatFrame(String id,GroupChatFrame frame){
		groupframes.put(id, frame);
	}
	
	/**
	 * 注销打开的私聊窗口
	 * @param username
	 */
	public static void unRegistPrivateChatFrame(String username){
		privateframes.remove(username);
	}
	
	/**
	 * 注销打开的群聊窗口
	 * @param id
	 */
	public static void unRegistGroupChatFrame(String id){
		groupframes.remove(id);
	}
	
	/**
	 * 清除所有消息记录，通常在退出时调用
	 */
	public static void destoryAllRecord(){
		privateframes=null;
		groupframes=null;
		privaterecords=null;
		grouprecords=null;
	}
}
