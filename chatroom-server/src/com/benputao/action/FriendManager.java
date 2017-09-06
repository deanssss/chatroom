package com.benputao.action;

import com.benputao.dtos.Reply;
import com.benputao.listen.ClientManger;

import java.util.List;

import com.benputao.config.Const;
import com.benputao.daos.FriendsDao;
import com.benputao.daos.UserDao;
import com.benputao.dtos.ActionData.Action;
import com.benputao.dtos.MsgData.Type;
import com.benputao.dtos.MsgData;
import com.benputao.dtos.Notification;
import com.benputao.model.User;
import com.benputao.utils.ClientsManger;

public class FriendManager {
	private User user;
	
	public FriendManager(User user) {
		this.user = user;
	}
	
	public User getUser() 						{	return user;			}
	public void setUser(User user) 				{	this.user = user;		}
	
	public Reply getFriends(){
		Reply res=new Reply();
		res.setAction(Action.GETFRIENDS);
		List<User>friends=new FriendsDao().QuerryFriends(user.getUsername());
		if(friends==null){
			res.setResult(Const.Status.ERROR);
			res.setCode(Const.Code.DATABASEERROR);
		}else if(!friends.isEmpty()){
			for (User user2 : friends) {
				if(ClientsManger.getClientManager(user2.getUsername())!=null){
					user2.setState(User.State.ONLINE);
				}else {
					user2.setState(User.State.OFFLINE);
				}
			}
			res.setResult(Const.Status.SUCCESS);
			res.setCode(Const.Code.QUERRYFRIEND);
			res.setRes(friends);
			for (User user : friends) {
				System.out.println(user.getState());
			}
		}else {
			res.setResult(Const.Status.SUCCESS);
			res.setCode(Const.Code.NOFRIENDS);
		}
		return res;
	}
	
	/**
	 * 发送好友请求验证---未解决：对方不在线情况
	 * @param user
	 * @return
	 */
	public Reply addFriend(User user){
		Reply res=new Reply();
		res.setAction(Action.ADDFRIEND);
		//取出验证信息
		String msg=user.getNickname();
		user.setNickname(null);
		//查看是否是好友
		List<User>friends=new FriendsDao().QuerryFriends(this.user.getUsername());
		boolean has=false;
		for (User u : friends) {
			if(u.getUsername().equals(user.getUsername())){
				has=true;
				break;
			}
		}
		if(has){	//是好友了
			res.setResult(Const.Status.FAILURE);
			res.setCode(Const.Code.YETBEFRIEND);
		}else{	
			//检查是否存在该用户
			List<User>users=new UserDao().queryUser(user);
			if(users.size()>0){	//该用户存在
				User u=users.get(0);
				ClientManger manger=ClientsManger.getClientManager(u.getUsername());
				Notification notice=new Notification(Notification.ADDFRIEND, this.user.getUsername()+"|"+msg);
				MsgData m=new MsgData(MsgData.Type.NOTIFICATION,notice);
				if (manger!=null){
					manger.sendMsgToClient(m);
					res.setResult(Const.Status.SUCCESS);
					res.setCode(Const.Code.ADDSUCCESS);
				}
			}else {	//无此用户
				res.setResult(Const.Status.FAILURE);
				res.setCode(Const.Code.NOUSER);
			}
		}
		return res;
	}
	
	public Reply delFriend(User friend){
		Reply res=new Reply();
		res.setAction(Action.DELFRIEND);
		boolean b=new FriendsDao().delFriendTogether(this.user.getUsername(), friend.getUsername());
		if(b){
			res.setResult(Const.Status.SUCCESS);
			res.setRes(friend);
			ClientManger manger=ClientsManger.getClientManager(friend.getUsername());
			if (manger!=null) {
				//向被删用户发送通知
				Notification notice=new Notification(Notification.DELFRIEND, "你与用户|"+this.user.getUsername()+"|的关系已解除!");
				MsgData msg=new MsgData(Type.NOTIFICATION,notice);
				manger.sendMsgToClient(msg);
			}
		}else {
			res.setResult(Const.Status.FAILURE);
		}
		return res;
	}
	
	/**
	 * 接受请求
	 * @param user 
	 * @return
	 */
	public Notification accept(User user){
		//添加好友到彼此好友列表中
		new FriendsDao().addFriendTogether(user.getUsername(),this.user.getUsername());
		//通知请求用户
		ClientManger manger=ClientsManger.getClientManager(user.getUsername());
		Notification notice=new Notification(Notification.ACCEPTFRIEND, this.user.getUsername()+"|同意了你的请求！");
		MsgData msg=new MsgData(Type.NOTIFICATION,notice);
		manger.sendMsgToClient(msg);
		//构造回复
		notice.setMsg(user.getUsername()+"|同意了你的请求！");
		return notice;
	}
	
	/**
	 * 拒绝请求
	 * @param user
	 * @return
	 */
	public Notification refuse(User user){
		//通知请求用户
		ClientManger manger=ClientsManger.getClientManager(user.getUsername());
		Notification notice=new Notification(Notification.OTHER, this.user.getUsername()+"|拒绝了你的请求！");
		MsgData msg=new MsgData(Type.NOTIFICATION,notice);
		manger.sendMsgToClient(msg);
		//构造回复
		notice.setMsg("已拒绝用户"+user.getUsername()+"的请求！");
		return notice;
	}
}
