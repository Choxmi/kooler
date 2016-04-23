package com.gilo.soko.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable{
	
	int id;
	String name, shop, tags, category; 
	float review, price;
	ArrayList<ItemImage> images;
	String featured_image, info;
	Boolean isNew, isHot;
	String stock;
	ArrayList<Option> options;
	
	public ArrayList<Option> getOptions() {
		return options;
	}
	public void setOptions(ArrayList<Option> options) {
		this.options = options;
	}
	public Boolean getIsNew() {
		return isNew;
	}
	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}
	public Boolean getIsHot() {
		return isHot;
	}
	public void setIsHot(Boolean isHot) {
		this.isHot = isHot;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public void setImages(ArrayList<ItemImage> images) {
		this.images = images;
	}
	public String getFeatured_image() {
		return featured_image;
	}
	public ArrayList<ItemImage> getImages() {
		return images;
	}
	public void setFeatured_image(String featured_image) {
		this.featured_image = featured_image;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public float getReview() {
		return review;
	}
	public void setReview(float review) {
		this.review = review;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
}
