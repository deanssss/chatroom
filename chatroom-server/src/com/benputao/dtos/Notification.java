package com.benputao.dtos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Notification implements IData{
	/**
	 * �������ر�
	 */
	public static final int SERVERCLOSED=0x000100;
	/**
	 * �û�����
	 */
	public static final int USERLOGIN=0x000101;
	/**
	 * �û�����
	 */
	public static final int USERLOGOUT=0x000102;
	/**
	 * ��������
	 */
	public static final int ADDFRIEND=0x000103;
	/**
	 * �������󱻽���
	 */
	public static final int ACCEPTFRIEND=0x000104;
	/**
	 * �������󱻾ܾ�
	 */
	public static final int REFUSEFRIEND=0x000105;
	/**
	 * ����ɾ��֪ͨ
	 */
	public static final int DELFRIEND=0x000106;
	/**
	 * ����Ⱥ����
	 */
	public static final int JOINROOM=0x000107;
	/**
	 * ��Ⱥ����ͬ��
	 */
	public static final int ACCEPTROOM=0x000108;
	/**
	 * ��Ⱥ���󱻾ܾ�
	 */
	public static final int REFUSEROOM=0x000109;
	/**
	 * ����֪ͨ
	 */
	public static final int OTHER=0x0001ff;
	
	private int code;
	private String msg;
	
	public Notification(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public int getCode() 			{	return code;		}
	public void setCode(int code) 	{	this.code = code;	}
	public String getMsg() 			{	return msg;			}
	public void setMsg(String msg) 	{	this.msg = msg;		}

	@Override
	public String getJson() {
		Gson gson=new GsonBuilder().serializeNulls().create();
		return gson.toJson(this);
	}

}
