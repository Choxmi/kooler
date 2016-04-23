package com.gilo.soko.models;

import java.util.ArrayList;

public class Option {
	
	String name, style;
	ArrayList<String> color_id = new ArrayList<String>();
	ArrayList<String> options;
	ArrayList<String> images;
	
	public int getOption_group_id() {
		return option_group_id;
	}
	public void setOption_group_id(int option_group_id) {
		this.option_group_id = option_group_id;
	}
	int option_id,	product_id, option_group_id;
	String option_title;
	
	public int getOption_id() {
		return option_id;
	}
	public void setOption_id(int option_id) {
		this.option_id = option_id;
	}
	public String getOption_title() {
		return option_title;
	}
	public void setOption_title(String option_title) {
		this.option_title = option_title;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public ArrayList<String> getColor_id() {
		return color_id;
	}
	public void setColor_id(ArrayList<String> color_id) {
		this.color_id = color_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public ArrayList<String> getOptions() {
		return options;
	}
	public void setOptions(ArrayList<String> options) {
		this.options = options;
	}
	public ArrayList<String> getImages() {
		return images;
	}
	public void setImages(ArrayList<String> images) {
		this.images = images;
	}
	
	

}
