package com.zhonghong.commonapp.utils;

/*
 * ��¼ÿ��Ӧ��һ��ʱ����ִ�еĴ���������ִ�е�ʱ��
 */
public class AppInfoBean {
	public String pkgName;	//Ӧ����
	public int count;	//һ��ʱ����ִ�еĴ���
	public long lastTime;	//����ִ�е�ʱ��
	public String lastClassName;	//��������
	
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
