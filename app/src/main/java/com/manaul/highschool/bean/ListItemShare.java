package com.manaul.highschool.bean;

import java.io.Serializable;

public class ListItemShare implements Serializable{
	private static final long serialVersionUID = -1689210909678760558L;
	private int shareId;
	private int parentId;
	private int itemId;

	public ListItemShare(int shareId, int parentId, int itemId) {
		super();
		this.shareId = shareId;
		this.parentId = parentId;
		this.itemId = itemId;
	}



	public int getShareId() {
		return shareId;
	}



	public void setShareId(int shareId) {
		this.shareId = shareId;
	}



	public int getParentId() {
		return parentId;
	}



	public void setParentId(int parentId) {
		this.parentId = parentId;
	}



	public int getItemId() {
		return itemId;
	}



	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public ListItemShare() {}
	
}
