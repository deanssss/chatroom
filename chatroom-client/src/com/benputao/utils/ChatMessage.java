package com.benputao.utils;

public class ChatMessage {
	private String sendUser;
	private String datetime;
	private String message;
	private boolean isread;
	
	public ChatMessage() {	}
	public ChatMessage(String sendUser, String datetime,String message) {
		this.sendUser = sendUser;
		this.datetime=datetime;
		this.message = message;
	}
	
	public String getSendUser() {
		return sendUser;
	}
	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isIsread() {
		return isread;
	}
	public void setIsread(boolean isread) {
		this.isread = isread;
	}
	@Override
	protected ChatMessage clone() throws CloneNotSupportedException {
		return new ChatMessage(sendUser,datetime,message);
	}
}
