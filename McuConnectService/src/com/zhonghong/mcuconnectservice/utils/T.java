package com.zhonghong.mcuconnectservice.utils;


public class T {
	
	public static class TAG {
		public static final String BROADCAST = "BootBroadcastReceiver";
		public static final String SERVICE = "ConnectService";	
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
	
}
