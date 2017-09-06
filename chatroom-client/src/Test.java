import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.benputao.dtos.ActionData.Action;
import com.benputao.dtos.MsgData;
import com.benputao.dtos.MsgData.Type;
import com.benputao.dtos.Reply;
import com.benputao.model.Chatroom;

public class Test {
	public static void main(String args[]) {
//		List<Chatroom>room=new ArrayList<Chatroom>();
//		room.add(new Chatroom("111111", "aaaaa", "100000"));
//		room.add(new Chatroom("222222", "bbbbb", "100001"));
//		Reply reply=new Reply(Action.GETCHATROOMS,1,11,room);
//		MsgData msg=new MsgData(Type.REPLY,reply);
//		System.out.println(msg.getJson());
//		InetAddress id=null;
//		try {
//			id=InetAddress.getLocalHost();
//			System.out.println(id.getHostAddress());
//			System.out.println(id.getHostName());
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
		double b=769.15;
		int nt=(int) (b/100);
		System.out.println(b-nt*100);
	}
}
