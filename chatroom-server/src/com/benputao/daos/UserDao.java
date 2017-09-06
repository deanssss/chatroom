package com.benputao.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.benputao.model.User;
import com.benputao.utils.DBUtil;
import com.benputao.utils.sqlbulider.SQLBulider;
import com.mysql.jdbc.Statement;

public class UserDao {
	public UserDao() {	}
	
	public List<User> queryUser(User model){
		List<User>users=new ArrayList<>();
		Connection connection=DBUtil.getDBconnection();
		try {
			Statement statement=(Statement) connection.createStatement();
			String sql=SQLBulider.buildQuery(model, null);
			System.out.println(sql);
			ResultSet set=statement.executeQuery(sql);
			while (set.next()) {
				String username=set.getString("username");
				CharSequence password=set.getString("password");
				String nickname=set.getString("nickname");
				Date registtime=set.getDate("registtime");
				Date lastlogin=set.getDate("lastlogin");
				User user=new User(username,password,nickname,registtime,lastlogin,null);
				String statestr=set.getString("state");
				User.State state=user.getStateFromStr(statestr);
				user.setState(state);
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	public boolean updateUser(User model,Map<String, Object> values) {
		boolean res=false;
		Connection connection=DBUtil.getDBconnection();
		try {
			Statement statement=(Statement) connection.createStatement();
			String sql=SQLBulider.buildUpdate(model, values);
			System.out.println(sql);
			res=statement.executeUpdate(sql)>=0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public boolean addUser(User model){
		model.setRegisttime(new Date());
		model.setLastlogin(new Date());
		String sql1=SQLBulider.buildInsert(model);
		String sql2="CREATE TABLE `"+model.getUsername()
				   +"friend`(`username`  varchar(10) NOT NULL ,"
				   +"`marks`  varchar(20) NULL ,"
				   +"`betime`  datetime NULL ON UPDATE CURRENT_TIMESTAMP ,"
				   +"`kind`  varchar(10) NULL DEFAULT 'user' ,"
				   +"FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE)";
		String sql3="CREATE TABLE `"+model.getUsername()
				   +"message`(`senduser` varchar(10) NOT NULL,"
				   +"`recvuser` varchar(10) NOT NULL,"
				   +"`sendtime` datetime NULL ON UPDATE CURRENT_TIMESTAMP ,"
				   +"`message` varchar(255) NULL,"
				   +"`isread` bit(1) NULL DEFAULT b'0',"
				   +"FOREIGN KEY (`senduser`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,"
				   +"FOREIGN KEY (`recvuser`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE)";
		boolean res=false;
		Connection connection=DBUtil.getDBconnection();
		try {
			connection.setAutoCommit(false);
			Statement statement=(Statement) connection.createStatement();
			statement.executeUpdate(sql1);
			statement.executeUpdate(sql2);
			statement.executeUpdate(sql3);
			connection.commit();
			connection.setAutoCommit(true);
			res=true;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return res;
	}
}
