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
		try {	//��ȡ�����ļ�
			InputStream inputStream=DBUtil.class.getResourceAsStream("/database.properties");
			properties.load(inputStream);
		} catch (IOException e) {
			System.out.println("���ݿ������ļ���ʧ�����飡");
		}
		String driver=properties.getProperty("jdbc.driver");
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			System.out.println("���ݿ�����������ʧ�����飡");
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
