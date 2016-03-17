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
	
	public static final int THREAD_CHECH_TIME = 1*1000;	//1S线程检测一次
	public static final int THREAD_SAVE_DATABASE_TIME = 60*1000;	//60S线程检测一次
	
	public static class TAG {
		public static final String BROADCAST = "BootBroadcastReceiver";
		public static final String COMMONAPPHELPER = "CommonAppHelper";	
		public static final String MAINACTIVITY = "MainActivity";
	}
	
	public static class Broadcast {
		// 开机广播
		public static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
		// 关机广播
		public static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
		// 安装
		public static final String PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";
		// 卸载
		public static final String PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";
	}
	
	public static class Package {
		public static final String MY_OWN = "com.zhonghong.commonapp";
	}
	
	public static class Time {
		public static final long SECOND = 1000;	//S
		public static final long MINUTE = 60*SECOND;	//分
		public static final long HOUR = 60*MINUTE;	//小时
		public static final long DAY = 24*HOUR;	//天
		public static final long MONTH = 30*DAY;	//月
		public static final long YEAR = 12*MONTH;	//年
	}
	
	/*
	 * action
	 */
	public static class Action{
		//更新应用图标
		public static final String UPDATE_APP = "com.zhonghong.commonappwidget.UPDATE_APP";
	};

	/*
	 *按键Key 
	 */
	public static class Cmd{
		public static final int OPEN_APP_1 = 1;
		public static final int OPEN_APP_2 = 2;
		public static final int OPEN_APP_3 = 3;
		public static final int OPEN_APP_4 = 4;
		public static final int OPEN_SET_TIME = 5;
	};
}
