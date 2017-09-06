package com.benputao.dtos;

import java.lang.reflect.Type;

import com.benputao.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ActionData implements IData,JsonSerializer<ActionData>,JsonDeserializer<ActionData>{
	static public enum Action{
		LOGIN,REGIST,LOGOUT,UPDATE,
		GETFRIENDS,DELFRIEND,ADDFRIEND,ACCEPT,REFUSE,
		SENDMSG,
		GETCHATROOMS,JOINCHATROOM,
		UNDO
	}
	private Action action;
	private User user;
	
	public ActionData() {	}
	public ActionData(Action action, User user) {
		this.action = action;
		this.user = user;
	}
	
	public Action getAction() 				{ return action; 		}
	public void setAction(Action action) 	{ this.action = action; }
	public User getUser()					{ return user; 			}
	public void setUser(User user) 			{ this.user = user; 	}
	
	@Override
	public String getJson() {
		Gson gson=new GsonBuilder().registerTypeAdapter(ActionData.class, this)
								   .registerTypeAdapter(User.class, new User()).create();
		return  gson.toJson(this);
	}
	
	/**
	 * 从json中获取数据对象
	 * @param json
	 * @return
	 */
	public static ActionData getDataFromJson(String json) {
		GsonBuilder builder=new GsonBuilder().registerTypeAdapter(ActionData.class, new ActionData())
				 							 .registerTypeAdapter(User.class, new User());
		Gson gson=builder.create();
		return gson.fromJson(json, ActionData.class);
	}
	
	@Override
	public ActionData deserialize(JsonElement json, Type arg1, JsonDeserializationContext context)
			throws JsonParseException {
		final JsonObject jsonObject=json.getAsJsonObject();
		String actionstr=jsonObject.get("action").getAsString();
		Action action=getActionFromStr(actionstr);
		JsonElement euser=jsonObject.get("user");
		User user;
		if (euser.isJsonObject()) {
			user=context.deserialize(euser, User.class);
		}else {
			user=null;
		}
		ActionData actionData=new ActionData(action,user);
		return actionData;
	}
	
	@Override
	public JsonElement serialize(ActionData data, Type arg1, JsonSerializationContext context) {
		final JsonObject object=new JsonObject();
		object.addProperty("action", getActionStr(data.getAction()));
		User user=data.getUser();
		final JsonElement element=context.serialize(user==null?"null":user);
		object.add("user", element);
		
		return object;
	}
	
	/**
	 * 通过Action获取对应的状态字符串
	 * @param action
	 * @return
	 */
	public static String getActionStr(Action action){
		String actionstr;
		if (action==null) {
			return "null";
		}
		switch (action) {
		case LOGIN:
			actionstr="LOGIN";
			break;
		case LOGOUT:
			actionstr="LOGOUT";
			break;
		case REGIST:
			actionstr="REGIST";
			break;
		case UPDATE:
			actionstr="UPDATE";
			break;
		case GETFRIENDS:
			actionstr="GETFRIENDS";
			break;
		case DELFRIEND:
			actionstr="DELFRIEND";
			break;
		case ADDFRIEND:
			actionstr="ADDFRIEND";
			break;
		case ACCEPT:
			actionstr="ACCEPT";
			break;
		case REFUSE:
			actionstr="REFUSE";
			break;
		case SENDMSG:
			actionstr="SENDMSG";
			break;
		case GETCHATROOMS:
			actionstr="GETCHATROOMS";
			break;
		case JOINCHATROOM:
			actionstr="JOINCHATROOM";
			break;
		default:
			actionstr="UNDO";
			break;
		}
		return actionstr;
	}
	
	/**
	 * 通过字符串获取对应状态枚举值
	 * @param actionstr
	 * @return
	 */
	public static Action getActionFromStr(String actionstr){
		if (actionstr.equals("null")) {
			return null;
		}
		Action action;
		switch (actionstr) {
		case "LOGIN":
			action=Action.LOGIN;
			break;
		case "LOGOUT":
			action=Action.LOGOUT;
			break;
		case "REGIST":
			action=Action.REGIST;
			break;
		case "UPDATE":
			action=Action.UPDATE;
			break;
		case "GETFRIENDS":
			action=Action.GETFRIENDS;
			break;
		case "DELFRIEND":
			action=Action.DELFRIEND;
			break;
		case "ADDFRIEND":
			action=Action.ADDFRIEND;
			break;
		case "ACCEPT":
			action=Action.ACCEPT;
			break;
		case "REFUSE":
			action=Action.REFUSE;
			break;
		case "SENDMSG":
			action=Action.SENDMSG;
			break;
		case "GETCHATROOMS":
			action=Action.GETCHATROOMS;
			break;
		case "JOINCHATROOM":
			action=Action.JOINCHATROOM;
			break;
		default:
			action=Action.UNDO;
			break;
		}
		return action;
	}
}
