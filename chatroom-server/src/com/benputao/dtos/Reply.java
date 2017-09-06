package com.benputao.dtos;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.benputao.dtos.ActionData.Action;
import com.benputao.model.Chatroom;
import com.benputao.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class Reply implements IData,JsonSerializer<Reply>,JsonDeserializer<Reply>{
	private ActionData.Action action;
	private int result;
	private int code;
	private Object res;
	
	public Reply() {	}
	public Reply(Action action, int result, int code, Object res) {
		this.action = action;
		this.result = result;
		this.code = code;
		this.res = res;
	}
	public ActionData.Action getAction() {
		return action;
	}
	public void setAction(ActionData.Action action) {
		this.action = action;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Object getRes() {
		return res;
	}
	public void setRes(Object res) {
		this.res = res;
	}
	
	@Override
	public String getJson() {
		Gson gson=new GsonBuilder().registerTypeAdapter(User.class, new User())
								   .registerTypeAdapter(Chatroom.class, new Chatroom())
				   				   .create();
		return gson.toJson(this);
	}
	
	public static Reply getDataFromJson(String json){
		GsonBuilder builder=new GsonBuilder().registerTypeAdapter(Chatroom.class, new Chatroom())
											 .registerTypeAdapter(User.class, new User());
		Gson gson=builder.create();
		return gson.fromJson(json, Reply.class);
	}
	
	@Override
	public Reply deserialize(JsonElement json, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object=json.getAsJsonObject();
		String actionstr=object.get("action").getAsString();
		Action action=ActionData.getActionFromStr(actionstr);
		int result=object.get("result").getAsInt();
		int code=object.get("code").getAsInt();
		JsonElement element=object.get("res");
		Object res;
		if(element!=null){
			switch (actionstr) {
				case "LOGIN":
				case "DELFRIEND":
					res=context.deserialize(element, User.class);
					break;
				case "GETFRIENDS":
					JsonArray array=element.getAsJsonArray();
					List<User>users=new ArrayList<>();
					for (JsonElement jsonElement : array) {
						User user=context.deserialize(jsonElement, User.class);
						users.add(user);
					}
					res=users;
					break;
				case "SENDMSG":
				case "JOINCHATROOM":
					res=element.getAsString();
					break;
				case "GETCHATROOMS":
					JsonArray array1=element.getAsJsonArray();
					List<Chatroom>rooms=new ArrayList<>();
					for (JsonElement jsonElement : array1) {
						Chatroom room=context.deserialize(jsonElement, Chatroom.class);
						rooms.add(room);
					}
					res=rooms;
					break;
				default:
					res=null;
					break;
			}
		}else {
			res=null;
		}
		return new Reply(action, result, code, res);
	}
	@Override
	public JsonElement serialize(Reply data, Type arg1, JsonSerializationContext context) {
		JsonObject object=new JsonObject();
		object.addProperty("action", ActionData.getActionStr(data.getAction()));
		object.addProperty("result", data.getResult());
		object.addProperty("code", data.getCode());
		Object res=data.getRes();
		JsonElement element=context.serialize(res==null?"null":res);
		object.add("res", element);
		return object;
	}
}
