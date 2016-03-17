package com.zhonghong.commonapp.utils;

import com.zhonghong.commonapp.CommonAppHelper;


public class T {
	
	public static CommonAppHelper mCommonAppHelper = null;
	
	public static final String DATABASE_FILE_NAME = "commonapp.db";
	public static final int DATABASE_VER = 1;
	public static final String COMMON_APP_TABLE_NAME = "commonapplist";
	
	public static final String COMMONTIME_FILE = "commontimepref";
	public static final String KEY_COMMONTIME = "commontime";
	public static final long DEFAULT_COMMONAPP_TIME = T.Time.DAY;
	
	public static final String REPLACE_STRING = "ace";
	
	public static final int THREAD_CHECH_TIME = 1*1000;	//1S�̼߳��һ��
	public static final int THREAD_SAVE_DATABASE_TIME = 60*1000;	//60S�̼߳��һ��
	
	public static class TAG {
		public static final String BROADCAST = "BootBroadcastReceiver";
		public static final String COMMONAPPHELPER = "CommonAppHelper";	
		public static final String MAINACTIVITY = "MainActivity";
	}
	
	public static class Broadcast {
		// �����㲥
		public static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
		// �ػ��㲥
		public static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
		// ��װ
		public static final String PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";
		// ж��
		public static final String PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";
	}
	
	public static class Package {
		public static final String MY_OWN = "com.zhonghong.commonapp";
	}
	
	public static class Time {
		public static final long SECOND = 1000;	//S
		public static final long MINUTE = 60*SECOND;	//��
		public static final long HOUR = 60*MINUTE;	//Сʱ
		public static final long DAY = 24*HOUR;	//��
		public static final long MONTH = 30*DAY;	//��
		public static final long YEAR = 12*MONTH;	//��
	}
	
	/*
	 * action
	 */
	public static class Action{
		//����Ӧ��ͼ��
		public static final String UPDATE_APP = "com.zhonghong.commonappwidget.UPDATE_APP";
	};

	/*
	 *����Key 
	 */
	public static class Cmd{
		public static final int OPEN_APP_1 = 1;
		public static final int OPEN_APP_2 = 2;
		public static final int OPEN_APP_3 = 3;
		public static final int OPEN_APP_4 = 4;
		public static final int OPEN_SET_TIME = 5;
	};
}
