package com.zhonghong.gesture.utils;

import android.content.Context;

public class T {
	
	public static Context mContext = null;
	
	public static class Broadcast {
		// 开机广播
		public static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
		// 关机广播
		public static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";

	}
}
