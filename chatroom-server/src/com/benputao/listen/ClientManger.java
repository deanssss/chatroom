package com.benputao.listen;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.benputao.action.UserManager;
import com.benputao.config.Const;
import com.benputao.dtos.ActionData;
import com.benputao.dtos.Message;
import com.benputao.dtos.MsgData;
import com.benputao.dtos.Notification;
import com.benputao.dtos.MsgData.Type;
import com.benputao.dtos.Reply;
import com.benputao.listen.Server.ServerListener;
import com.benputao.utils.ClientsManger;

/**
 * ����ͻ������ӵ�Socket���й���
 * @author benputao
 *
 */
public class ClientManger implements Runnable{
	private Socket incoming;
	private UserManager usermanager;
	private Scanner in;
	private PrintWriter out;
	private ServerListener listener;
	
	public ClientManger(Socket incoming,ServerListener listener) {
		this.incoming=incoming;
		this.listener=listener;
	}
	
	public Socket getIncoming() 						{	return incoming;				}
	public void setIncoming(Socket incoming) 			{	this.incoming = incoming;		}
	public UserManager getUsermanager() 				{	return usermanager;				}
	public void setUsermanager(UserManager usermanager) {	this.usermanager = usermanager; }
	public Scanner getIn() 								{	return in;						}
	public void setIn(Scanner in) 						{	this.in = in;					}
	public PrintWriter getOut() 						{	return out;						}
	public void setOut(PrintWriter out) 				{	this.out = out;					}

	@Override
	public void run() {
		try {
			in=new Scanner(incoming.getInputStream());
			out=new PrintWriter(incoming.getOutputStream(),true);
			while(!incoming.isClosed()&&in.hasNextLine()){
				//��ȡ��Ϣ
				String json=in.nextLine();
				System.out.println(json);
				//������������
				MsgData msg=(MsgData) MsgData.getDataFromJson(json);
				//������Ϣ����
				dealMsgData(msg);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				incoming.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void dealMsgData(MsgData msg) throws IOException{
		switch(msg.getType()){
			case ACTION:
				ActionData data=(ActionData) msg.getData();
				dealActionData(data, msg);
				break;
			case MESSAGE:
				Message message=(Message) msg.getData();
				dealMessage(message);
				break;
			default:
				break;
		}
	}
	
	/**
	 * 
	 * @param message
	 */
	public void dealMessage(Message message){
		if (message==null) {	return;		}
		switch (message.getType()) {
		case Message.PRIVATE:
			Reply reply=usermanager.getMessageManger().sendPrivate(message);
			MsgData msg=new MsgData(Type.REPLY, reply);
			sendMsgToClient(msg);
			break;
		case Message.GROUP:
			Reply reply2=usermanager.getMessageManger().sendGroup(message);
			MsgData msg2=new MsgData(Type.REPLY, reply2);
			sendMsgToClient(msg2);
			break;
		default:
			break;
		}
	}
	
	public void dealActionData(ActionData data,MsgData msg) throws IOException{
		switch (data.getAction()) {
			case LOGIN:
				usermanager=new UserManager();
				String username=data.getUser().getUsername();
				Reply r=UserManager.login(data.getUser(), incoming,usermanager);
				//����½������͵�
				msg.setType(Type.REPLY);
				msg.setData(r);
				sendMsgToClient(msg);
				if(r.getResult()!=Const.Status.SUCCESS){	//ע�᲻�ɹ��ر�����
					incoming.close();
				}else {	//��½�ɹ���Client�������ע���Client
					ClientsManger.putClientManager(username, this);
				}
				break;
			case REGIST:System.out.println("regist");
				Reply r1=UserManager.regist(data.getUser());
				msg.setType(Type.REPLY);
				msg.setData(r1);
				sendMsgToClient(msg);
				incoming.close();
				break;
			case LOGOUT:
				Reply r2=usermanager.logout(data.getUser());
				msg.setType(Type.REPLY);
				msg.setData(r2);
				sendMsgToClient(msg);
				if(r2.getResult()==Const.Status.SUCCESS){
					//ע���ɹ���Client�������ע����Client���ҹر�����
					ClientsManger.removeClientManager(usermanager.getUser().getUsername());
					incoming.close();
				}
				break;
			case UPDATE:System.out.println("update");
				Reply r3=usermanager.update(data.getUser());
				msg.setType(Type.REPLY);
				msg.setData(r3);
				sendMsgToClient(msg);
				break;
			case GETFRIENDS:
				Reply r4=usermanager.getFriendManger().getFriends();
				msg.setType(Type.REPLY);
				msg.setData(r4);
				sendMsgToClient(msg);
				break;
			case DELFRIEND:
				Reply r5=usermanager.getFriendManger().delFriend(data.getUser());
				msg.setType(Type.REPLY);
				msg.setData(r5);
				sendMsgToClient(msg);
				break;
			case ADDFRIEND:
				Reply r6=usermanager.getFriendManger().addFriend(data.getUser());
				msg.setType(Type.REPLY);
				msg.setData(r6);
				sendMsgToClient(msg);
				break;
			case ACCEPT:
				//����nickname�洢����id
				String roomid=data.getUser().getNickname();
				Notification notice;
				if(roomid==null){	//��������
					notice=usermanager.getFriendManger().accept(data.getUser());
				}else {
					notice=usermanager.getChatroomManger().accept(data.getUser().getUsername(),roomid);
				}
				msg.setType(Type.NOTIFICATION);
				msg.setData(notice);
				sendMsgToClient(msg);
				break;
			case REFUSE:
				//����nickname�洢����id
				String roomid1=data.getUser().getNickname();
				Notification notice1;
				if(roomid1==null){	//��������
					notice1=usermanager.getFriendManger().refuse(data.getUser());
				}else {
					notice1=usermanager.getChatroomManger().refuse(data.getUser().getUsername(),roomid1);
				}
				msg.setType(Type.NOTIFICATION);
				msg.setData(notice1);
				sendMsgToClient(msg);
				break;
			case GETCHATROOMS:
				Reply r7=usermanager.getChatroomManger().getChatrooms();
				msg.setType(Type.REPLY);
				msg.setData(r7);
				sendMsgToClient(msg);
				break;
			case JOINCHATROOM:
				Reply r8=usermanager.getChatroomManger()
									.joinChatroom(data.getUser().getUsername(), data.getUser().getNickname());
				msg.setType(Type.REPLY);
				msg.setData(r8);
				sendMsgToClient(msg);
				break;
			default:
				break;
		}
	}
	
	public void sendMsgToClient(MsgData msg){
		out.println(msg.getJson());
		listener.serverNotify(msg);
	}
}