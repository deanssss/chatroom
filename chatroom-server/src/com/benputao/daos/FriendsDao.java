package com.benputao.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.benputao.model.User;
import com.benputao.utils.DBUtil;

public class FriendsDao {
	public List<User>QuerryFriends(String username){
		List< User>friends=new ArrayList<>();
		String sql="select users.username,users.nickname,users.state,"
					+username+"friend.marks from users,"+username+"friend where users.username="
					+username+"friend.username and kind='user'";
		Connection connection=DBUtil.getDBconnection();
		
		try {
			Statement statement=connection.createStatement();
			System.out.println(sql);
			ResultSet res=statement.executeQuery(sql);
			while(res.next()){
				String user=res.getString("username");
				String nickname=res.getString("nickname");
				String statestr=res.getString("state");
				User u=new User(user,null,nickname);
				u.setState(u.getStateFromStr(statestr));
				friends.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			friends=null;
		}
		return friends;
	}

	/**
	 * 相互添加好友
	 * @param user1
	 * @param user2
	 * @return
	 */
	public boolean addFriendTogether(String user1, String user2) {
		String sql1="insert into "+user1+"friend values("+user2+",'null',now(),'user')";
		String sql2="insert into "+user2+"friend values("+user1+",'null',now(),'user')";
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
	
	/**
	 * 删除好友
	 * @param user1
	 * @param user2
	 * @return
	 */
	public boolean delFriendTogether(String user1,String user2){
		String sql1="delete from "+user1+"friend where username='"+user2+"' and kind='user'";
		String sql2="delete from "+user2+"friend where username='"+user1+"' and kind='user'";
		System.out.println(sql1);
			System.out.println(sql2);
		Connection connection=DBUtil.getDBconnection();
		try {
			connection.setAutoCommit(false);
			Statement statement=connection.createStatement();
			statement.executeUpdate(sql1);
			statement.executeUpdate(sql2);
			
			connection.commit();
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
