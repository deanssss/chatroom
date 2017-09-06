package com.benputao.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
	private static Connection connection;
	
	static{
		Properties properties=new Properties();
		try {	//获取配置文件
			InputStream inputStream=DBUtil.class.getResourceAsStream("/database.properties");
			properties.load(inputStream);
		} catch (IOException e) {
			System.out.println("数据库配置文件丢失，请检查！");
		}
		String driver=properties.getProperty("jdbc.driver");
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			System.out.println("数据库链接驱动丢失，请检查！");
		}
		
		String url=properties.getProperty("jdbc.url");
		String username=properties.getProperty("jdbc.username");
		String password=properties.getProperty("jdbc.password");
		try {
			connection=DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getDBconnection(){
		return connection;
	}
}
