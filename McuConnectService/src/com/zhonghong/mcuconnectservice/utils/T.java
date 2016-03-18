package com.zhonghong.mcuconnectservice.utils;


public class T {
	
	public static class TAG {
		public static final String BROADCAST = "BootBroadcastReceiver";
		public static final String SERVICE = "ConnectService";	
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
	
}
