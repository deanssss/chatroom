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
	 * ��recvuser����������Ϣ
	 * @param message
	 * @return
	 */
	public Reply sendPrivate(Message message){
		Reply res=new Reply(Action.SENDMSG,0,0,null);
		String recvuser=message.getRecvuser();
		ClientManger manger=ClientsManger.getClientManager(recvuser);
		if(manger!=null){	//�û�����
			message.setSenduser(user.getUsername());
			message.setNickname(user.getNickname());
			MsgData msg=new MsgData(Type.MESSAGE, message);
			manger.sendMsgToClient(msg);
			res.setResult(Const.Status.SUCCESS);
			res.setCode(Const.Code.SENDSUCCESS);
			res.setRes("���ͳɹ���");
		}else {	//�û�����
			res.setResult(Const.Status.FAILURE);
			res.setCode(Const.Code.NOTONLINE);
			res.setRes("�û������ߣ�");
		}
		return res;
	}
	
	/**
	 * ����Ⱥ����Ϣ
	 * @param message
	 * @return
	 */
	public Reply sendGroup(Message message){
		Reply res=new Reply(Action.SENDMSG,0,0,null);
		String roomid=message.getRecvuser();		//���ͳ�ȥ��Ⱥ����Ϣ��recvuserΪȺid
		//��ȡȺ�������û�id
		List<String>members=new ChatroomDao().getMembers(roomid);
		if(members!=null){
			if(!members.isEmpty()){
				//��ȡ���������û���Clientmanger
				List<ClientManger>mangers=new ArrayList<>();
				for (String string : members) {
					//���������û�
					if(string.equals(user.getUsername()))continue;
					ClientManger m=ClientsManger.getClientManager(string);
					if (m!=null) {	mangers.add(m);	}
				}
				//�������û�ת����Ϣ
				message.setSenduser(user.getUsername());
				message.setNickname(user.getNickname());
				MsgData msg=new MsgData(Type.MESSAGE,message);
				for (ClientManger clientManger : mangers) {
					clientManger.sendMsgToClient(msg);
				}
				//���ؽ��
				res.setResult(Const.Status.SUCCESS);
				res.setCode(Const.Code.SENDSUCCESS);
				res.setRes("����Ⱥ����Ϣ�ɹ���");
			}else {
				res.setResult(Const.Status.SUCCESS);
				res.setCode(Const.Code.NOMEMBER);
				res.setRes("û�г�Ա");
			}
		}else {
			res.setResult(Const.Status.ERROR);
			res.setCode(Const.Code.DATABASEERROR);
			res.setRes("���ݿⷢ������");
		}
		return res;
	}
}
