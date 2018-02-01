package com.manaul.highschool.bean;

import cn.bmob.v3.BmobObject;

public class AdPicture extends BmobObject{
	private String url;
	private int type ; // 0：默认 ；1：web 
	private String message;
	private String name;
	private String imageUtl;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getImageUtl() {
		return imageUtl;
	}

	public void setImageUtl(String imageUtl) {
		this.imageUtl = imageUtl;
	}
}
