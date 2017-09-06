package com.benputao.model;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.benputao.dtos.ActionData;
import com.benputao.dtos.IData;
import com.benputao.dtos.MsgData;
import com.benputao.utils.sqlbulider.Column;
import com.benputao.utils.sqlbulider.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Table("users")
public class User implements IData,JsonSerializer<User>,JsonDeserializer<User>{
	static public enum State{
		ONLINE,OFFLINE,UNKNOWN
	}
	@Column("username")
	private String username;
	@Column("password")
	private CharSequence password;
	@Column("nickname")
	private String nickname;
	@Column("registtime")
	private Date registtime;
	@Column("lastlogin")
	private Date lastlogin;
	@Column("state")
	private State state;
	
	public User() {;}
	public User(String username, CharSequence password, String nickname) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
	}
	public User(String username, CharSequence password, String nickname, Date registtime, Date lastlogin, State state) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.registtime = registtime;
		this.lastlogin = lastlogin;
		this.state = state;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public CharSequence getPassword() {
		return password;
	}
	public void setPassword(CharSequence password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Date getRegisttime() {
		return registtime;
	}
	public void setRegisttime(Date registtime) {
		this.registtime = registtime;
	}
	public Date getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	@Override
	public String getJson() {
		GsonBuilder builder=new GsonBuilder().registerTypeAdapter(User.class, this);
		Gson gson=builder.create();
		return gson.toJson(this);
	}
	
	public static User getDataFromJson(String json) {
		GsonBuilder builder=new GsonBuilder().registerTypeAdapter(MsgData.class, new MsgData())
				 .registerTypeAdapter(ActionData.class, new ActionData())
				 .registerTypeAdapter(User.class, new User());
		Gson gson=builder.create();
		User user=gson.fromJson(json, User.class);
		return user;
	}
	@Override
	public User deserialize(JsonElement json, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject=json.getAsJsonObject();
		String username=jsonObject.get("username").getAsString();
		username=username.equals("null")?null:username;
		CharSequence password=jsonObject.get("password").getAsString();
		password=password.equals("null")?null:password;
		String nickname=jsonObject.get("nickname").getAsString();
		nickname=nickname.equals("null")?null:nickname;
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String datestr=jsonObject.get("registtime").getAsString();
		Date registtime=null,lastlogin=null;
		try {
			if (datestr.equals("null")) {
				registtime=null;
			}else {
				registtime=dateFormat.parse(datestr);
			}
		} catch (ParseException e) {
			System.out.println("×ª»»´íÎó£¡"+datestr);
			e.printStackTrace();
		}
		datestr=jsonObject.get("lastlogin").getAsString();
		try {
			if (datestr.equals("null")) {
				lastlogin=null;
			}else {
				lastlogin=dateFormat.parse(datestr);
			}
		} catch (ParseException e) {
			System.out.println("×ª»»´íÎó£¡"+datestr);
			e.printStackTrace();
		}
		State state=getStateFromStr(jsonObject.get("state").getAsString());
		User user=new User(username,password,nickname,registtime,lastlogin,state);
		return user;
	}
	@Override
	public JsonElement serialize(User user, Type arg1, JsonSerializationContext context) {
		final JsonObject object=new JsonObject();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String username=user.getUsername();
		object.addProperty("username", username==null?"null":username);
		CharSequence password=user.getPassword();
		object.addProperty("password",password==null?"null":password.toString());
		String nickname=user.getNickname();
		object.addProperty("nickname", nickname==null?"null":nickname);
		Date registtime=user.getRegisttime();
		String datestr;
		if(registtime!=null){
			datestr=dateFormat.format(registtime);
		}else {
			datestr="null";
		}
		object.addProperty("registtime", datestr);
		Date lastlogin=user.getLastlogin();
		if(lastlogin!=null){
			datestr=dateFormat.format(lastlogin);
		}else {
			datestr="null";
		}
		object.addProperty("lastlogin", datestr);
		object.addProperty("state", getStateStr(user.getState()));
		return object;
	}
	
	final public String getStateStr(State state){
		String statestr;
		if(state==null){
			return "null";
		}
		switch (state) {
		case ONLINE:
			statestr="ONLINE";
			break;
		case OFFLINE:
			statestr="OFFLINE";
			break;
		default:statestr="UNKNOWN";
			break;
		}
		return statestr;
	}
	final public State getStateFromStr(String statestr){
		if (statestr.equals("null")) {
			return null;
		}
		State state;
		switch (statestr) {
		case "ONLINE":
			state=State.ONLINE;
			break;
		case "OFFLINE":
			state=State.OFFLINE;
			break;
		default:state=State.UNKNOWN;
			break;
		}
		return state;
	}
}
