package com.benputao.utils;

import java.util.ArrayList;
import java.util.List;

public class MessageRecord {
	private String id;
	private List<ChatMessage>messages;
	private int noreadIndex=-1;
	private int msgnum=0;
	
	public MessageRecord(String id) {
		this.id=id;
		messages=new ArrayList<>();
		noreadIndex=-1;
		msgnum=0;
	}
	
	public void addMessage(ChatMessage message){
		messages.add(message);
		msgnum++;
		if(message.isIsread()){	//δ����Ϣ
			noreadIndex++;
		}
	}
	
	/**
	 * ��ȡδ����Ϣ��num���Ѷ���Ϣ
	 * @param num
	 * @return
	 */
	public List<ChatMessage> getMessage(int num){
		List<ChatMessage>s=new ArrayList<>();
		int start=0;
		if (num-1>=noreadIndex) {	//δ����Ϣ����num��
			start=0;
		}else {
			start=noreadIndex-num+1;
		}
		//��ȡ��Ϣ
		for(;start<msgnum;start++){
			try {
				s.add(messages.get(start).clone());
				if(start>=noreadIndex){	//������Ϣ�Ѷ�
					messages.get(start).setIsread(true);
				}
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return s;
	}
}
