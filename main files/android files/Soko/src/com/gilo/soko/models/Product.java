package com.gilo.soko.models;

import java.io.Serializable;

public class Product implements Serializable{

	/* 
	product_id	
	category	
	image_location	
	price	
	product_option	
	product_name	
	featured_image	
	brand	
	tags	
	review	
	stock	
	new 
	hot
	info
	*/
	String category, image_location, product_name, featured_image, brand, tags, info;
	int product_id, stock, is_new, is_hot;
	double price;
	double review;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getImage_location() {
		return image_location;
	}
	public void setImage_location(String image_location) {
		this.image_location = image_location;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getFeatured_image() {
		return featured_image;
	}
	public void setFeatured_image(String featured_image) {
		this.featured_image = featured_image;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getIs_new() {
		return is_new;
	}
	public void setIs_new(int is_new) {
		this.is_new = is_new;
	}
	public int getIs_hot() {
		return is_hot;
	}
	public void setIs_hot(int is_hot) {
		this.is_hot = is_hot;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getReview() {
		return review;
	}
	public void setReview(double review) {
		this.review = review;
	}
	
	
}
