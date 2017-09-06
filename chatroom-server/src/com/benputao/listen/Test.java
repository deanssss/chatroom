package com.benputao.listen;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.benputao.dtos.ActionData;
import com.benputao.dtos.ActionData.Action;
import com.benputao.dtos.MsgData;
import com.benputao.model.User;
import com.benputao.model.User.State;
import com.benputao.utils.sqlbulider.SQLBulider;

public class Test {
	public static void main(String[] args) {
		Socket socket = null;
		try {
			socket=new Socket("127.0.0.1", 612);
			OutputStream outputStream=socket.getOutputStream();
			InputStream inputStream=socket.getInputStream();
			PrintWriter out=new PrintWriter(outputStream,true);
			Scanner in=new Scanner(inputStream);
			
			MsgData msgData=new MsgData();
			msgData.setType(MsgData.Type.ACTION);
			ActionData actionData=new ActionData();
			actionData.setAction(Action.LOGIN);
			//User user=new User("10000","deanssss",null,null,null,null);
			//User user=new User("10001","benputao",null,null,null,null);
			User user=new User("10003","111112",null,null,null,null);
			actionData.setUser(user);
			msgData.setData(actionData);
			//发送数据
			out.println(msgData.getJson());
			System.out.println(msgData.getJson());
			//接收数据
			Scanner iin=new Scanner(System.in);
			iin.nextLine();
			actionData.setAction(Action.LOGOUT);
			out.println(msgData.getJson());
			
			while(in.hasNextLine()){
				String json=in.nextLine();
				System.out.println("server return:"+json);
			}
			iin.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (socket!=null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		User user=new User("10000",null,null,null,null,State.OFFLINE);
//		Map<String, Object>values=new HashMap<>();
//		values.put("username", "10001");
//		values.put("nickname", "unknown");
//		String string=SQLBulider.buildUpdate(user, values);
//		System.out.println(string);
	}
}
