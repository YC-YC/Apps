package com.zhonghong.coldlist;

import android.graphics.drawable.Drawable;

public class ColdListBean {
	private Drawable apkIcon;
	private String apkName;
	private String apkLabel;
	
	public ColdListBean(Drawable apkIcon, String apkName, String apkInfo) {
		super();
		this.apkIcon = apkIcon;
		this.apkName = apkName;
		this.apkLabel = apkInfo;
	}

	public Drawable getApkIcon() {
		return apkIcon;
	}

	public void setApkIcon(Drawable apkIcon) {
		this.apkIcon = apkIcon;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public String getApkLabel() {
		return apkLabel;
	}

	public void setApkLabel(String apkLabel) {
		this.apkLabel = apkLabel;
	}
	
	
}
