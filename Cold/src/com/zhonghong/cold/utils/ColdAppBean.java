package com.zhonghong.cold.utils;

public class ColdAppBean {
	public String pkgName;	//Ӧ�ð���
	public long millis;	//�ϴ�ִ�е����ڵĺ�����
	
	
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
