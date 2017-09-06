package com.benputao.config;

public class Const {
	
	public class Status{
		public static final int SUCCESS= 1;
		public static final int ERROR=  -1;
		public static final int FAILURE= 0;
	}
	
	public class Code{
		/**
		 * 登录成功
		 */
		public static final int LOGINSUCCESS=0x00001;
		/**
		 * 重复登录
		 */
		public static final int REPETIT=0x00002;
		/**
		 * 用户名或密码错误
		 */
		public static final int PASSERROR=0x00003;
		/**
		 * 更新失败，服务器数据库故障
		 */
		public static final int CHANGEERROR=0x00004;
		/**
		 * 成功退出
		 */
		public static final int LOGOUT=0x00005;
		/**
		 * 用户名已注册
		 */
		public static final int REPETITNAME=0x00006;
		/**
		 * 注册成功
		 */
		public static final int REGISTSUCCESS=0x00007;
		/**
		 * 更新成功
		 */
		public static final int UPDATESUCCESS=0x00008;
		/**
		 * 数据库查询失败
		 */
		public static final int DATABASEERROR=0x00009;
		/**
		 * 朋友列表为空
		 */
		public static final int NOFRIENDS=0x0000a;
		/**
		 * 查询朋友成功
		 */
		public static final int QUERRYFRIEND=0x0000b;
		/**
		 * 没有此用户
		 */
		public static final int NOUSER=0x0000c;
		/**
		 * 用户已是好友
		 */
		public static final int YETBEFRIEND=0x0000d;
		/**
		 * 用户不在线
		 */
		public static final int NOTONLINE=0x0000e;
		/**
		 * 发送验证成功
		 */
		public static final int ADDSUCCESS=0x0000f;
		/**
		 * 发送消息成功
		 */
		public static final int SENDSUCCESS=0x00010;
		/**
		 * 聊天室列表为空
		 */
		public static final int NOCHATROOM=0x00011;
		/**
		 * 获取聊天室成功
		 */
		public static final int QUERRYCHATROOM=0x00012;
		/**
		 * 没有成员
		 */
		public static final int NOMEMBER=0x00013;
		/**
		 * 已经加入了
		 */
		public static final int HADJOIN=0x00014;
	}
}
