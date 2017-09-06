package com.benputao.action;

import com.benputao.config.Const;
import com.benputao.daos.ChatroomDao;
import com.benputao.dtos.Reply;
import com.benputao.listen.ClientManger;

import java.util.List;

import com.benputao.dtos.ActionData.Action;
import com.benputao.dtos.MsgData.Type;
import com.benputao.dtos.MsgData;
import com.benputao.dtos.Notification;
import com.benputao.model.Chatroom;
import com.benputao.model.User;
import com.benputao.utils.ClientsManger;

public class ChatroomManger {
	private User user;
	
	public ChatroomManger(User user) {
		this.user=user;
	}
	
	public User getUser() {	return user;	}
	public void setUser(User user) {	this.user = user;	}
	
	public Reply getChatrooms(){
		Reply res=new Reply(Action.GETCHATROOMS,0,0,null);
		List<Chatroom>rooms=new ChatroomDao().getChatrooms(user.getUsername());
		if (rooms!=null) {
			if(rooms.size()>0){
				res.setResult(Const.Status.SUCCESS);
				res.setCode(Const.Code.QUERRYCHATROOM);
				res.setRes(rooms);
			}else {
				res.setResult(Const.Status.SUCCESS);
				res.setCode(Const.Code.NOCHATROOM);
			}
		}else {
			res.setResult(Const.Status.ERROR);
			res.setCode(Const.Code.DATABASEERROR);
		}
		return res;
	}
	
	public Reply joinChatroom(String id,String msg){
		Reply res=new Reply(Action.JOINCHATROOM, 0, 0, null);
		Chatroom model=new Chatroom(id, null, null);
		ChatroomDao dao=new ChatroomDao();
		List<Chatroom>rooms=dao.querryChatrrom(model);
		if(rooms!=null){
			if (!rooms.isEmpty()) {
				Chatroom room=rooms.get(0);
				//检测用户是否已在群内
				List<String>members=dao.getMembers(id);
				boolean isJoin=false;
				for (String m : members) {
					if (m.equals(user.getUsername())) {
						isJoin=true;
						break;
					}
				}
				if (isJoin) {
					res.setResult(Const.Status.FAILURE);
					res.setCode(Const.Code.HADJOIN);
					res.setRes("已加入该群");
				}else {
					//向群主发送验证请求
					ClientManger manger=ClientsManger.getClientManager(room.getGroupuser());
					if (manger!=null) {
						Notification notice=new Notification(Notification.JOINROOM, 
															 user.getUsername()+"|想加入群|"+id+"|"+msg);
						MsgData m=new MsgData(Type.NOTIFICATION, notice);
						manger.sendMsgToClient(m);
						res.setResult(Const.Status.SUCCESS);
						res.setCode(Const.Code.ADDSUCCESS);
						res.setRes("发送成功！");
					}else {	//不在线 未处理
						res.setResult(Const.Status.FAILURE);
						res.setCode(Const.Code.NOTONLINE);
						res.setRes("用户不在线！");
					}
				}
			}else {
				res.setResult(Const.Status.FAILURE);
				res.setCode(Const.Code.NOCHATROOM);
				res.setRes("没有该聊天室");
			}
		}else {
			res.setResult(Const.Status.ERROR);
			res.setCode(Const.Code.DATABASEERROR);
			res.setRes("操作数据库发生错误！");
		}
		return res;
	}
	
	public Notification accept(String username,String roomid){
		Notification notice=new Notification(Notification.ACCEPTROOM, null);
		//添加好友到群
		new ChatroomDao().addMember(username,roomid);
		//向请求人发送通知
		notice.setMsg(roomid+"|群主同意了你的请求！");
		MsgData msg=new MsgData(Type.NOTIFICATION,notice);
		ClientManger manger=ClientsManger.getClientManager(username);
		if(manger!=null){
			manger.sendMsgToClient(msg);
		}
		//向用户返回结果
		notice.setCode(Notification.OTHER);
		notice.setMsg("您已同意用户<"+username+">加入群！");
		return notice;
	}
	
	public Notification refuse(String username,String roomid){
		Notification notice=new Notification(Notification.OTHER, null);
		//通知请求用户
		ClientManger manger=ClientsManger.getClientManager(username);
		if(manger!=null){
			notice.setMsg("群主拒绝了你的请求！");
			MsgData msg=new MsgData(Type.NOTIFICATION, notice);
			manger.sendMsgToClient(msg);
		}
		notice.setMsg("已拒绝用户<"+username+">的请求！");
		return notice;
	}
}
