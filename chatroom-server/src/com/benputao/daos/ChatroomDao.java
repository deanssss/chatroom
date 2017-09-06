package com.benputao.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.benputao.model.Chatroom;
import com.benputao.utils.DBUtil;
import com.benputao.utils.sqlbulider.SQLBulider;

public class ChatroomDao {
	
	/**
	 * 获取username所加入的聊天室
	 * @param username
	 * @return
	 */
	public List<Chatroom> getChatrooms(String username){
		List<Chatroom>rooms=new ArrayList<>();
		String sql="select chatrooms.id,chatrooms.name,chatrooms.groupuser,chatrooms.maxmember,chatrooms.hadmember,chatrooms.grouptime from "
				  +username+"friend,chatrooms where "+username+"friend.kind='room' and "+username
				  +"friend.username=chatrooms.id";
		Connection connection=DBUtil.getDBconnection();
		try {
			Statement statement=connection.createStatement();
			ResultSet set=statement.executeQuery(sql);
			while(set.next()){
				String id=set.getString("id");
				String name=set.getString("name");
				String groupuser=set.getString("groupuser");
				int maxmember=set.getInt("maxmember");
				int hadmember=set.getInt("hadmember");
				Date grouptime=set.getDate("grouptime");
				Chatroom room=new Chatroom(id,name,groupuser,maxmember,hadmember,grouptime);
				rooms.add(room);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			rooms=null;
		}
		return rooms;
	}
	
	/**
	 * 获取该聊天室所有成员username
	 * @param id
	 * @return
	 */
	public List<String>getMembers(String id){
		List<String>members=new ArrayList<>();
		String sql="select username from "+id+"join";
		Connection connection=DBUtil.getDBconnection();
		try {
			Statement statement=connection.createStatement();
			ResultSet set=statement.executeQuery(sql);
			while(set.next()){
				String member=set.getString("username");
				members.add(member);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			members=null;
		}
		return members;
	}
	
	public List<Chatroom>querryChatrrom(Chatroom model){
		List<Chatroom>rooms=new ArrayList<>();
		String sql=SQLBulider.buildQuery(model, null);
		Connection connection=DBUtil.getDBconnection();
		try {
			Statement statement=connection.createStatement();
			ResultSet set=statement.executeQuery(sql);
			while(set.next()){
				String id=set.getString("id");
				String name=set.getString("name");
				String groupuser=set.getString("groupuser");
				int maxmember=set.getInt("maxmember");
				int hadmember=set.getInt("hadmember");
				Date grouptime=set.getDate("grouptime");
				Chatroom room=new Chatroom(id,name,groupuser,maxmember,hadmember,grouptime);
				rooms.add(room);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			rooms=null;
		}
		return rooms;
	}
	
	public boolean addMember(String username,String roomid){
		String sql1="insert into "+roomid+"join(username) values('"+username+"')";
		String sql2="insert into "+username+"friend(username,kind) values('"+roomid+"','room')";
		Connection connection=DBUtil.getDBconnection();
		try {
			connection.setAutoCommit(false);
			Statement statement=connection.createStatement();
			statement.executeUpdate(sql1);
			statement.executeUpdate(sql2);
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
				return false;
			} catch (SQLException e1) {
				e1.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
