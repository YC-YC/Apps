package com.zhonghong.mcuconnectservice;

import com.zhonghong.mcuconnectservice.ItaskCallback;

/*
 * Binder interface which Clients bind Service
 */
interface ITaskBinder{
	void registerCallback(ITaskCallback callback);	//registerCallback that service callback client
	void unregisterCallback(ITaskCallback callback);//unregister
	int getStatus();
	int getKeyStat();	
	int getLEDStat();
	int getIOStat();
	String getVersion();
	void postCmd(int cmd, String arg);
}