package com.gilo.soko.models;

import java.io.Serializable;

public class OptionGroup implements Serializable{
		
	int option_group_id, product_id, has_image;
	String name;
	public int getOption_group_id() {
		return option_group_id;
	}
	public void setOption_group_id(int option_group_id) {
		this.option_group_id = option_group_id;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public int getHas_image() {
		return has_image;
	}
	public void setHas_image(int has_image) {
		this.has_image = has_image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
