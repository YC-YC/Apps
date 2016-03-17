package com.zhonghong.cold.service;

import com.zhonghong.cold.service.ItaskCallback;

interface ITaskBinder{
	void registerCallback(ITaskCallback callback);
	void unregisterCallback(ITaskCallback callback);
	long getColdTime();
	void setColdTime(long second);
	List<String> getColdList();
	void removeFromColdList(String pkgName);
	}