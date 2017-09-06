package com.benputao.config;

public class Const {
	
	public class Status{
		public static final int SUCCESS= 1;
		public static final int ERROR=  -1;
		public static final int FAILURE= 0;
	}
	
	public class Code{
		/**
		 * ��¼�ɹ�
		 */
		public static final int LOGINSUCCESS=0x00001;
		/**
		 * �ظ���¼
		 */
		public static final int REPETIT=0x00002;
		/**
		 * �û������������
		 */
		public static final int PASSERROR=0x00003;
		/**
		 * ����ʧ�ܣ����������ݿ����
		 */
		public static final int CHANGEERROR=0x00004;
		/**
		 * �ɹ��˳�
		 */
		public static final int LOGOUT=0x00005;
		/**
		 * �û�����ע��
		 */
		public static final int REPETITNAME=0x00006;
		/**
		 * ע��ɹ�
		 */
		public static final int REGISTSUCCESS=0x00007;
		/**
		 * ���³ɹ�
		 */
		public static final int UPDATESUCCESS=0x00008;
		/**
		 * ���ݿ��ѯʧ��
		 */
		public static final int DATABASEERROR=0x00009;
		/**
		 * �����б�Ϊ��
		 */
		public static final int NOFRIENDS=0x0000a;
		/**
		 * ��ѯ���ѳɹ�
		 */
		public static final int QUERRYFRIEND=0x0000b;
		/**
		 * û�д��û�
		 */
		public static final int NOUSER=0x0000c;
		/**
		 * �û����Ǻ���
		 */
		public static final int YETBEFRIEND=0x0000d;
		/**
		 * �û�������
		 */
		public static final int NOTONLINE=0x0000e;
		/**
		 * ������֤�ɹ�
		 */
		public static final int ADDSUCCESS=0x0000f;
		/**
		 * ������Ϣ�ɹ�
		 */
		public static final int SENDSUCCESS=0x00010;
		/**
		 * �������б�Ϊ��
		 */
		public static final int NOCHATROOM=0x00011;
		/**
		 * ��ȡ�����ҳɹ�
		 */
		public static final int QUERRYCHATROOM=0x00012;
		/**
		 * û�г�Ա
		 */
		public static final int NOMEMBER=0x00013;
		/**
		 * �Ѿ�������
		 */
		public static final int HADJOIN=0x00014;
	}
}
