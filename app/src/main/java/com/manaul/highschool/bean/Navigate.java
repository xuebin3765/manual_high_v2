package com.manaul.highschool.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Navigate implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 22839173925927051L;
	private Integer navigateId;
    private String intro;
    private String name;
    private Integer parentId;
    private String imgUrl;
    private int status;
    private String type;
    private Long created; 
    
    List<ItemContent> itemContents = new ArrayList<ItemContent>();

	public Integer getNavigateId() {
		return navigateId;
	}

	public void setNavigateId(Integer navigateId) {
		this.navigateId = navigateId;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public List<ItemContent> getItemContents() {
		return itemContents;
	}

	public void setItemContents(List<ItemContent> itemContents) {
		this.itemContents = itemContents;
	}
    
	
}
