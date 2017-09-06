package com.benputao.view;

public class MyListData {
	private String name;
	private String id;
	private String discribe;
	private String imgpath;
	private String time;
	
	public MyListData() {	}
	public MyListData(String name, String id,String discribe, String imgpath,String time) {
		super();
		this.name = name;
		this.id=id;
		this.discribe = discribe;
		this.imgpath = imgpath;
		this.time=time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDiscribe() {
		return discribe;
	}
	public void setDiscribe(String discribe) {
		this.discribe = discribe;
	}
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
