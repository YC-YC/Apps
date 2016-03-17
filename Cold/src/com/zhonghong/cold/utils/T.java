package com.zhonghong.cold.utils;

import com.zhonghong.cold.ColdHelper;
import com.zhonghong.coldlist.ColdListFragment;
import com.zhonghong.coldsetup.ColdSetupFragment;

public class T {
	
	public static final String DATABASE_FILE_NAME = "cold.db";
	public static final int DATABASE_VER = 1;
	public static final String COLD_TABLE_NAME = "coldlist";
	
	public static ColdHelper mColdHelper = null;
//	public static Context mContext;
	public static ColdListFragment mColdListFragment = null;
	public static ColdSetupFragment mColdSetupFragment = null;
	
	public static final int THREAD_CHECH_TIME = 3*1000;	//3S�̼߳��һ��
	public static final int THREAD_SAVE_DATABASE_TIME = 60*1000;	//60S�̼߳��һ��
	
	public static class TAG {
		public static final String BROADCAST = "BootBroadcastReceiver";
		public static final String COLDHELPER = "ColdHelper";	
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
		public static final String MY_OWN = "com.zhonghong.cold";
					
	}
}
