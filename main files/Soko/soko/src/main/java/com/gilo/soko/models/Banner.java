package com.gilo.soko.models;

import java.io.Serializable;

public class Banner implements Serializable{

	String image, category_slug;
	int bannerId, categoryId, isShowing, order;

	public int getIsShowing() {
		return isShowing;
	}

	public void setIsShowing(int isShowing) {
		this.isShowing = isShowing;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getBannerId() {
		return bannerId;
	}

	public void setBannerId(int bannerId) {
		this.bannerId = bannerId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCategory_slug() {
		return category_slug;
	}

	public void setCategory_slug(String category_slug) {
		this.category_slug = category_slug;
	}
	
}
