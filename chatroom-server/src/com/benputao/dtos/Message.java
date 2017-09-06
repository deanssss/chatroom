package com.benputao.dtos;

import java.util.Date;

import com.google.gson.Gson;

public class Message implements IData{
	/**
	 * к╫ад
	 */
	public static final int PRIVATE=0x000201;
	/**
	 * х╨ад
	 */
	public static final int GROUP=0x000202;
	
	private String senduser;
	private String nickname;
	private int type;
	private String recvuser;
	private Date datetime;
	private String message;
	
	public Message() {	}
	public Message(int type, String recvuser, Date datetime, String message) {
		this.type=type;
		this.recvuser = recvuser;
		this.datetime = datetime;
		this.message = message;
	}
	
	public String getSenduser() {
		return senduser;
	}
	public void setSenduser(String senduser) {
		this.senduser = senduser;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getRecvuser() {
		return recvuser;
	}
	public void setRecvuser(String recvuser) {
		this.recvuser = recvuser;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String getJson() {
		Gson gson=new Gson();
		return gson.toJson(this);
	}
	
}
