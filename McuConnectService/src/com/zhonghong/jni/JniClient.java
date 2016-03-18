package com.zhonghong.jni;


import android.util.Log;

/*
 * Jni交互函数
 */
public class JniClient {
	
	private static final String TAG = "JniClient";
	
	static public native void looper();	//Looper
	
	static public native void HelloBlueTooth();	//测试函数
	static public native String GetBlueToothVersion();	//
	
	static public native int getStatus();
	static public native int getKeyStat();	//获取按键状态
	static public native int getLEDStat();	//获取LED状态
	static public native int getIOStat();	//获取IO口状态
	static public native String getVersion();	//获取版本号
	
	static public native void postCmd(int cmd, String arg);
	
	public static String CCallJavaForString(int cmd, String val)
	{
		Log.i(TAG, "CCallJavaForString cmd=" + cmd + ", val=" + val);
		return "Java CCallJavaForString";
	}
	
	private void LOG(String str)
	{
		Log.i(TAG, str);
	}
}
