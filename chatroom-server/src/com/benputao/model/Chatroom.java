package com.benputao.model;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.benputao.dtos.IData;
import com.benputao.utils.sqlbulider.Column;
import com.benputao.utils.sqlbulider.Table;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Table("chatrooms")
public class Chatroom implements IData,JsonSerializer<Chatroom>,JsonDeserializer<Chatroom>{
	@Column("id")
	private String id;
	@Column("name")
	private String name;
	@Column("groupuser")
	private String groupuser;
	@Column("maxmember")
	private int maxmember;
	@Column("hadmember")
	private int hadmember;
	@Column("grouptime")
	private Date grouptime;
	
	public Chatroom() {	}
	public Chatroom(String id, String name, String groupuser) {
		this.id = id;
		this.name = name;
		this.groupuser = groupuser;
		maxmember=-1;
		hadmember=-1;
	}
	public Chatroom(String id, String name, String groupuser, int maxmember, int hadmember, Date grouptime) {
		this.id = id;
		this.name = name;
		this.groupuser = groupuser;
		this.maxmember = maxmember;
		this.hadmember = hadmember;
		this.grouptime = grouptime;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroupuser() {
		return groupuser;
	}
	public void setGroupuser(String groupuser) {
		this.groupuser = groupuser;
	}
	public int getMaxmember() {
		return maxmember;
	}
	public void setMaxmember(int maxmember) {
		this.maxmember = maxmember;
	}
	public int getHadmember() {
		return hadmember;
	}
	public void setHadmember(int hadmember) {
		this.hadmember = hadmember;
	}
	public Date getGrouptime() {
		return grouptime;
	}
	public void setGrouptime(Date grouptime) {
		this.grouptime = grouptime;
	}
	@Override
	public String getJson() {
		Gson gson=new Gson();
		return gson.toJson(this);
	}
	@Override
	public Chatroom deserialize(JsonElement json, Type arg1, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject=json.getAsJsonObject();
		String id=jsonObject.get("id").getAsString();
		id=id.equals("null")?null:id;
		String name=jsonObject.get("name").getAsString();
		name=name.equals("null")?null:name;
		String groupuser=jsonObject.get("groupuser").getAsString();
		groupuser=groupuser.equals("null")?null:groupuser;
		int maxmember=jsonObject.get("maxmember").getAsInt();
		int hadmember=jsonObject.get("hadmember").getAsInt();
		String str=jsonObject.get("grouptime").getAsString();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date grouptime=null;
		if(!str.equals("null")){
			try {
				grouptime=format.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Chatroom room=new Chatroom(id, name, groupuser, maxmember, hadmember, grouptime);
		return room;
	}
	@Override
	public JsonElement serialize(Chatroom room, Type arg1, JsonSerializationContext context) {
		JsonObject object=new JsonObject();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String id=room.getId();
		object.addProperty("id", id==null?"null":id);
		String name=room.getName();
		object.addProperty("name", name==null?"null":name);
		String groupuser=room.getGroupuser();
		object.addProperty("groupuser", groupuser==null?"null":groupuser);
		int maxmember=room.getMaxmember();
		object.addProperty("maxmember", maxmember);
		int hadmember=room.getHadmember();
		object.addProperty("hadmember", hadmember);
		Date grouptime=room.getGrouptime();
		if(grouptime!=null){
			object.addProperty("grouptime", format.format(grouptime));
		}else {
			object.addProperty("grouptime", "null");
		}
		return object;
	}
}
