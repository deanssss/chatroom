package com.benputao.action;

import com.benputao.dtos.ActionData.Action;
import com.benputao.dtos.MsgData.Type;

import java.util.ArrayList;
import java.util.List;

import com.benputao.config.Const;
import com.benputao.daos.ChatroomDao;
import com.benputao.dtos.Message;
import com.benputao.dtos.MsgData;
import com.benputao.dtos.Reply;
import com.benputao.listen.ClientManger;
import com.benputao.model.User;
import com.benputao.utils.ClientsManger;

public class MessageManger {
	private User user;
	
	public MessageManger(User user) {
		this.user=user;
	}
	
	public User getUser() 						{	return user;			}
	public void setUser(User user) 				{	this.user = user;		}
	
	/**
	 * 向recvuser发送在线消息
	 * @param message
	 * @return
	 */
	public Reply sendPrivate(Message message){
		Reply res=new Reply(Action.SENDMSG,0,0,null);
		String recvuser=message.getRecvuser();
		ClientManger manger=ClientsManger.getClientManager(recvuser);
		if(manger!=null){	//用户在线
			message.setSenduser(user.getUsername());
			message.setNickname(user.getNickname());
			MsgData msg=new MsgData(Type.MESSAGE, message);
			manger.sendMsgToClient(msg);
			res.setResult(Const.Status.SUCCESS);
			res.setCode(Const.Code.SENDSUCCESS);
			res.setRes("发送成功！");
		}else {	//用户离线
			res.setResult(Const.Status.FAILURE);
			res.setCode(Const.Code.NOTONLINE);
			res.setRes("用户不在线！");
		}
		return res;
	}
	
	/**
	 * 发送群聊消息
	 * @param message
	 * @return
	 */
	public Reply sendGroup(Message message){
		Reply res=new Reply(Action.SENDMSG,0,0,null);
		String roomid=message.getRecvuser();		//发送出去的群组消息，recvuser为群id
		//获取群组所有用户id
		List<String>members=new ChatroomDao().getMembers(roomid);
		if(members!=null){
			if(!members.isEmpty()){
				//获取所有在线用户的Clientmanger
				List<ClientManger>mangers=new ArrayList<>();
				for (String string : members) {
					//跳过发送用户
					if(string.equals(user.getUsername()))continue;
					ClientManger m=ClientsManger.getClientManager(string);
					if (m!=null) {	mangers.add(m);	}
				}
				//向所有用户转发消息
				message.setSenduser(user.getUsername());
				message.setNickname(user.getNickname());
				MsgData msg=new MsgData(Type.MESSAGE,message);
				for (ClientManger clientManger : mangers) {
					clientManger.sendMsgToClient(msg);
				}
				//返回结果
				res.setResult(Const.Status.SUCCESS);
				res.setCode(Const.Code.SENDSUCCESS);
				res.setRes("发送群组消息成功！");
			}else {
				res.setResult(Const.Status.SUCCESS);
				res.setCode(Const.Code.NOMEMBER);
				res.setRes("没有成员");
			}
		}else {
			res.setResult(Const.Status.ERROR);
			res.setCode(Const.Code.DATABASEERROR);
			res.setRes("数据库发生错误！");
		}
		return res;
	}
}
