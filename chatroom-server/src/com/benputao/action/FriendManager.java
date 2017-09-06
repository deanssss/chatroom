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
	 * ���ͺ���������֤---δ������Է����������
	 * @param user
	 * @return
	 */
	public Reply addFriend(User user){
		Reply res=new Reply();
		res.setAction(Action.ADDFRIEND);
		//ȡ����֤��Ϣ
		String msg=user.getNickname();
		user.setNickname(null);
		//�鿴�Ƿ��Ǻ���
		List<User>friends=new FriendsDao().QuerryFriends(this.user.getUsername());
		boolean has=false;
		for (User u : friends) {
			if(u.getUsername().equals(user.getUsername())){
				has=true;
				break;
			}
		}
		if(has){	//�Ǻ�����
			res.setResult(Const.Status.FAILURE);
			res.setCode(Const.Code.YETBEFRIEND);
		}else{	
			//����Ƿ���ڸ��û�
			List<User>users=new UserDao().queryUser(user);
			if(users.size()>0){	//���û�����
				User u=users.get(0);
				ClientManger manger=ClientsManger.getClientManager(u.getUsername());
				Notification notice=new Notification(Notification.ADDFRIEND, this.user.getUsername()+"|"+msg);
				MsgData m=new MsgData(MsgData.Type.NOTIFICATION,notice);
				if (manger!=null){
					manger.sendMsgToClient(m);
					res.setResult(Const.Status.SUCCESS);
					res.setCode(Const.Code.ADDSUCCESS);
				}
			}else {	//�޴��û�
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
				//��ɾ�û�����֪ͨ
				Notification notice=new Notification(Notification.DELFRIEND, "�����û�|"+this.user.getUsername()+"|�Ĺ�ϵ�ѽ��!");
				MsgData msg=new MsgData(Type.NOTIFICATION,notice);
				manger.sendMsgToClient(msg);
			}
		}else {
			res.setResult(Const.Status.FAILURE);
		}
		return res;
	}
	
	/**
	 * ��������
	 * @param user 
	 * @return
	 */
	public Notification accept(User user){
		//��Ӻ��ѵ��˴˺����б���
		new FriendsDao().addFriendTogether(user.getUsername(),this.user.getUsername());
		//֪ͨ�����û�
		ClientManger manger=ClientsManger.getClientManager(user.getUsername());
		Notification notice=new Notification(Notification.ACCEPTFRIEND, this.user.getUsername()+"|ͬ�����������");
		MsgData msg=new MsgData(Type.NOTIFICATION,notice);
		manger.sendMsgToClient(msg);
		//����ظ�
		notice.setMsg(user.getUsername()+"|ͬ�����������");
		return notice;
	}
	
	/**
	 * �ܾ�����
	 * @param user
	 * @return
	 */
	public Notification refuse(User user){
		//֪ͨ�����û�
		ClientManger manger=ClientsManger.getClientManager(user.getUsername());
		Notification notice=new Notification(Notification.OTHER, this.user.getUsername()+"|�ܾ����������");
		MsgData msg=new MsgData(Type.NOTIFICATION,notice);
		manger.sendMsgToClient(msg);
		//����ظ�
		notice.setMsg("�Ѿܾ��û�"+user.getUsername()+"������");
		return notice;
	}
}
