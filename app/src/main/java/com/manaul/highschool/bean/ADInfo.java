package com.manaul.highschool.bean;

import cn.bmob.v3.BmobObject;

/**
 * 描述：广告信息</br>
 */
public class ADInfo extends BmobObject{

	private String title;
	private String url ;
	private String imageUtl;
	private String content ;
	private int sort;
	private int type;


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getImageUtl() {
		return imageUtl;
	}

	public void setImageUtl(String imageUtl) {
		this.imageUtl = imageUtl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
}
