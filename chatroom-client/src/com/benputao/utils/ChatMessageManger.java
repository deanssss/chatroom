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
	 * ��ʼ��˽����Ϣ����
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
	 * ��ʼ��Ⱥ����Ϣ����
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
	 * ��ȡ��username �û�/Ⱥ �Ľ�num�������¼+����δ����Ϣ
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
	 * ���ܵ�����username����Ϣ
	 * @param username
	 * @param msg
	 */
	public static void receive(Message message){
		//��ȡ����
		String username=message.getSenduser();
		String nickname=message.getNickname();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String datetime=format.format(message.getDatetime());
		String msg=message.getMessage();
		//������Ϣ
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
	 * ��Ӽ�¼
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
	 * �Ƴ���¼
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
	 * ������Ϣֱ����ӵ���Ӧ��¼��
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
	 * ע��򿪵�˽�Ĵ���
	 * @param username
	 * @param frame
	 */
	public static void registPrivateChatFrame(String username,PrivateChatFrame frame){
		privateframes.put(username, frame);
	}
	
	/**
	 * ע��򿪵�Ⱥ�Ĵ���
	 * @param id
	 * @param frame
	 */
	public static void registGroupChatFrame(String id,GroupChatFrame frame){
		groupframes.put(id, frame);
	}
	
	/**
	 * ע���򿪵�˽�Ĵ���
	 * @param username
	 */
	public static void unRegistPrivateChatFrame(String username){
		privateframes.remove(username);
	}
	
	/**
	 * ע���򿪵�Ⱥ�Ĵ���
	 * @param id
	 */
	public static void unRegistGroupChatFrame(String id){
		groupframes.remove(id);
	}
	
	/**
	 * ���������Ϣ��¼��ͨ�����˳�ʱ����
	 */
	public static void destoryAllRecord(){
		privateframes=null;
		groupframes=null;
		privaterecords=null;
		grouprecords=null;
	}
}
