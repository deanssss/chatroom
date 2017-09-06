package com.benputao.dtos;

import com.benputao.model.Chatroom;
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

/**
 * 服务器与客户端之间传送消息类
 * @author benputao
 *
 */
public class MsgData implements IData,JsonSerializer<MsgData>,JsonDeserializer<MsgData>{
	static public enum Type{
		ACTION,MESSAGE,REPLY,NOTIFICATION,UNDO
	}
	private Type type;
	private IData data;
	
	
	public MsgData() {	}
	public MsgData(Type type, IData data) {
		super();
		this.type = type;
		this.data = data;
	}
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public IData getData() {
		return data;
	}
	public void setData(IData data) {
		this.data = data;
	}
	
	@Override
	public String getJson(){
		Gson gson=new GsonBuilder().registerTypeAdapter(MsgData.class, this)
								   .registerTypeAdapter(ActionData.class, new ActionData())
								   .registerTypeAdapter(Chatroom.class, new Chatroom())
								   .registerTypeAdapter(User.class, new User())
								   .create();
		return gson.toJson(this);
	}
	
	public static MsgData getDataFromJson(String json) {
		GsonBuilder builder=new GsonBuilder().registerTypeAdapter(MsgData.class, new MsgData())
				 							 .registerTypeAdapter(ActionData.class, new ActionData())
				 							 .registerTypeAdapter(User.class, new User())
				 							 .registerTypeAdapter(Chatroom.class, new Chatroom())
				 							 .registerTypeAdapter(Reply.class, new Reply());
		Gson gson=builder.create();
		return gson.fromJson(json, MsgData.class);
	}
	@Override
	public JsonElement serialize(MsgData msgData, java.lang.reflect.Type arg1, JsonSerializationContext context) {
		final JsonObject object=new JsonObject();
		
		object.addProperty("type", getTypeStr(msgData.getType()));
		IData data=msgData.getData();
		final JsonElement element=context.serialize(data==null?"null":data);
		object.add("data", element);
		
		return object;
	}
	@Override
	public MsgData deserialize(JsonElement json, java.lang.reflect.Type arg1, JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject=json.getAsJsonObject();
		
		String typestr=jsonObject.get("type").getAsString();
		Type type=getTypeFromStr(typestr);
		JsonElement edata=jsonObject.get("data");
		IData data;
		if(edata.isJsonObject()&&!edata.isJsonNull()){
			if (typestr.equals("ACTION")) {
				data=context.deserialize(edata, ActionData.class);
			}else if (typestr.equals("REPLY")) {
				data=context.deserialize(edata, Reply.class);
			}else if (typestr.equals("MESSAGE")) {
				data=context.deserialize(edata, Message.class);
			} else if(typestr.equals("NOTIFICATION")){
				data=context.deserialize(edata, Notification.class);
			}else {
				data=null;
			}
		}else{
			data=null;
		}
		final MsgData msgData=new MsgData(type, data);
		return msgData;
	}
	
	private String getTypeStr(Type type){
		String typestr;
		if (type==null) {
			return "null";
		}
		switch (type) {
			case ACTION:
				typestr="ACTION";
				break;
			case MESSAGE:
				typestr="MESSAGE";
				break;
			case REPLY:
				typestr="REPLY";
				break;
			case NOTIFICATION:
				typestr="NOTIFICATION";
				break;	
			default:typestr="UNDO";
				break;
		}
		return typestr;
	}
	private Type getTypeFromStr(String typestr){
		if (typestr.equals("null")) {
			return null;
		}
		Type type;
		switch (typestr) {
		case "ACTION":
			type=Type.ACTION;
			break;
		case "MESSAGE":
			type=Type.MESSAGE;
			break;
		case "REPLY":
			type=Type.REPLY;
			break;
		case "NOTIFICATION":
			type=Type.NOTIFICATION;
			break;
		default:type=Type.UNDO;
			break;
		}
		return type;
	}
}
