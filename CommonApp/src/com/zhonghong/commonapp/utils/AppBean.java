package com.zhonghong.commonapp.utils;

public class AppBean {
	public String pkgName;
	public String className;
	public AppBean(String pkgName, String className) {
		super();
		this.pkgName = pkgName;
		this.className = className;
	}
	@Override
	public String toString() {
		return "AppBean [pkgName=" + pkgName + ", className=" + className + "]";
	}
	
	
}
