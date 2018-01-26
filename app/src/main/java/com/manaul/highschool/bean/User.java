package com.manaul.highschool.bean;

import cn.bmob.v3.BmobUser;

@SuppressWarnings("serial")
public class User extends BmobUser {
	private String accessToken;
	private int isVip; 
	private int privilege;
	private long vipEnd; // 会员起始时间
	private String headPic;
	private String nickName;
	private int gender;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	public int getPrivilege() {
		return privilege;
	}

	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}

	public int getIsVip() {
		return isVip;
	}

	public void setIsVip(int isVip) {
		this.isVip = isVip;
	}

	public long getVipEnd() {
		return vipEnd;
	}

	public void setVipEnd(long vipEnd) {
		this.vipEnd = vipEnd;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}
}
