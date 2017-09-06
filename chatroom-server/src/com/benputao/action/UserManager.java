package com.benputao.action;

import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.benputao.config.Const;
import com.benputao.daos.UserDao;
import com.benputao.dtos.ActionData;
import com.benputao.dtos.Reply;
import com.benputao.listen.ClientManger;
import com.benputao.model.User;
import com.benputao.utils.ClientsManger;

public class UserManager{
	private User user;
	private ClientManger clientmanger;
	private FriendManager friendManager=null;
	private MessageManger messageManger=null;
	private ChatroomManger chatroomManger=null;
	
	public UserManager() {	}
	public UserManager(User user, ClientManger clientmanger) {
		this.user=user;
		this.clientmanger=clientmanger;
	}
	
	public User getUser() 				 { return user;      }
	public void setUser(User user) 		 { this.user = user; }
	public void setClientManger(ClientManger clientmanger) {this.clientmanger = clientmanger; }
	public ClientManger getClientManger() {	return clientmanger;	}
	/**
	 * 用户登录，需传入登录用户对象，客户端socket，用于管理该用户的UserManager对象引用
	 * @param user
	 * @param socket
	 * @param manager
	 * @return
	 */
	public static Reply login(User user,Socket socket,UserManager manager){
		Reply res=new Reply();
		res.setAction(ActionData.Action.LOGIN);
		//检测是否已登录
		ClientManger s=ClientsManger.getClientManager(user.getUsername());
		if (s!=null) {	//重复登录
			res.setResult(Const.Status.FAILURE);
			res.setCode(Const.Code.REPETIT);
			return res;
		}
		UserDao dao=new UserDao();
		List<User>users=dao.queryUser(user);
		if (users.size()>0) {
			user=users.get(0);
			manager.setUser(user);
		    //更新数据库信息
		    user.setLastlogin(new Date()); 
		    user.setState(User.State.ONLINE);
		    User model=new User(user.getUsername(),null,null);
		    Map<String, Object>values=new HashMap<>();
		    values.put("lastlogin", new Date());
		    values.put("state", user.getState());
		    boolean r=dao.updateUser(model,values);
		    if(r){	//登录成功
		    	res.setResult(Const.Status.SUCCESS);
		    	res.setCode(Const.Code.LOGINSUCCESS);
		    	res.setRes(user);
		    }else {	    //登陆失败
		    	res.setResult(Const.Status.ERROR);
		    	res.setCode(Const.Code.CHANGEERROR);
			}
		}else {
			res.setResult(Const.Status.FAILURE);
			res.setCode(Const.Code.PASSERROR);
		}
		return res;
	}
	
	public static Reply regist(User user){
		Reply res=new Reply();
		res.setAction(ActionData.Action.REGIST);
		res.setRes(user);
		UserDao dao=new UserDao();
		List<User>rel=dao.queryUser(new User(user.getUsername(),null,null));
		if(!rel.isEmpty()){	//用户名已注册
			res.setResult(Const.Status.FAILURE);
			res.setCode(Const.Code.REPETITNAME);
			return res;
		}
		boolean r=dao.addUser(user);
		if (r) {
			res.setResult(Const.Status.SUCCESS);
			res.setCode(Const.Code.REGISTSUCCESS);
		}else {
			res.setResult(Const.Status.ERROR);
			res.setCode(Const.Code.CHANGEERROR);
		}
		return res;
	}
	
	/**
	 * 用户注销，修改数据库信息，SocketManger移除相关socket
	 * @param model
	 * @return
	 */
	public Reply logout(User model){
		Reply res=new Reply();
		res.setRes(model);
		res.setAction(ActionData.Action.LOGOUT);
		UserDao dao=new UserDao();
		//更新数据库信息
		 Map<String, Object>values=new HashMap<>();
		 values.put("lastlogin", new Date());
		 values.put("state", User.State.OFFLINE);
		boolean r=dao.updateUser(model, values);
		if (r) {
			res.setResult(Const.Status.SUCCESS);
			res.setCode(Const.Code.LOGOUT);
		}else {
			res.setResult(Const.Status.ERROR);
			res.setCode(Const.Code.CHANGEERROR);
		}
		return res;
	}
	
	public Reply update(User model){
		Reply res=new Reply();
		res.setAction(ActionData.Action.UPDATE);
		UserDao dao=new UserDao();
		Map<String, Object>values=new HashMap<>();
		if(!user.getPassword().equals(model.getPassword())){
			values.put("password", model.getPassword());
			user.setPassword(model.getPassword());
		}
		if(!user.getNickname().equals(model.getNickname())){
			values.put("nickname", model.getNickname());
			user.setNickname(model.getNickname());
		}
		if (user.getState()!=model.getState()&&model.getState()!=null) {
			values.put("state", user.getStateStr(model.getState()));
			user.setState(model.getState());
		}
		User u=new User(user.getUsername(),null,null);
		boolean r=dao.updateUser(u, values);
		if(r){
			res.setResult(Const.Status.SUCCESS);
			res.setCode(Const.Code.UPDATESUCCESS);
		}else {
			res.setResult(Const.Status.ERROR);
			res.setCode(Const.Code.CHANGEERROR);
		}
		return res;
	}
	
	public MessageManger getMessageManger(){
		if (messageManger==null) {
			messageManger=new MessageManger(user);
		}
		return messageManger;
	}
	
	public FriendManager getFriendManger(){
		if(friendManager==null){
			friendManager=new FriendManager(user);
		}
		return friendManager;
	}
	
	public ChatroomManger getChatroomManger(){
		if (chatroomManger==null) {
			chatroomManger=new ChatroomManger(user);
		}
		return chatroomManger;
	}
}
