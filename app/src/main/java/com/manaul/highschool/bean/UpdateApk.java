package com.manaul.highschool.bean;

import cn.bmob.v3.BmobObject;

public class UpdateApk extends BmobObject{
	private static final long serialVersionUID = 7753807169071712804L;
	private String apkurl;
	private String version;
	private String message;
	private int versionCode;
	public String getApkurl() {
		return apkurl;
	}
	public void setApkurl(String apkurl) {
		this.apkurl = apkurl;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "UpdateApk [apkurl=" + apkurl + ", version=" + version + ", message=" + message + "]";
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	
	
	
	
}
