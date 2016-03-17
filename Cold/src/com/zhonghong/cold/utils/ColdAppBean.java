package com.zhonghong.cold.utils;

public class ColdAppBean {
	public String pkgName;	//应用包名
	public long millis;	//上次执行到现在的毫秒数
	
	
	public ColdAppBean(String pkgName, long millis) {
		super();
		this.pkgName = pkgName;
		this.millis = millis;
	}
	public ColdAppBean() {
	}
	@Override
	public String toString() {
		return "ColdAppBean [pkgName=" + pkgName + ", millis=" + millis + "]";
	}
}
