package com.zhonghong.commonapp.utils;

/*
 * 记录每个应用一定时间内执行的次数及最新执行的时间
 */
public class AppInfoBean {
	public String pkgName;	//应用名
	public int count;	//一定时间内执行的次数
	public long lastTime;	//最新执行的时间
	public String lastClassName;	//最新类名
	
	public AppInfoBean(int count, long lastTime, String pkgName, String lastClassName) {
		super();
		this.count = count;
		this.lastTime = lastTime;
		this.pkgName = pkgName;
		this.lastClassName = lastClassName;
	}
	
	public AppInfoBean(AppInfoBean bean) {
		super();
		this.count = bean.count;
		this.lastTime = bean.lastTime;
		this.pkgName = bean.pkgName;
		this.lastClassName = bean.lastClassName;
	}

	@Override
	public String toString() {
		return "AppInfoBean [pkgName=" + pkgName + ", count=" + count
				+ ", lastTime=" + lastTime + ", lastClassName=" + lastClassName
				+ "]";
	}
	
	
}
