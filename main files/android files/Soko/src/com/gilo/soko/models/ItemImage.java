package com.gilo.soko.models;

public class ItemImage {

	String color_id, image, variation_value;
	int image_id, product_id, option_id, position;
	
	//	image_id	product_id	option_id	image_name

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getImage_id() {
		return image_id;
	}

	public String getVariation_value() {
		return variation_value;
	}

	public void setVariation_value(String variation_value) {
		this.variation_value = variation_value;
	}

	public void setImage_id(int image_id) {
		this.image_id = image_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getOption_id() {
		return option_id;
	}

	public void setOption_id(int option_id) {
		this.option_id = option_id;
	}

	public String getColor_id() {
		return color_id;
	}

	public void setColor_id(String color_id) {
		this.color_id = color_id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
}
