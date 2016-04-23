package com.gilo.soko.models;

import java.io.Serializable;

public class Comment implements Serializable{

	//	id	product_id	comment	rating	comfort	style	customer_name	date
	
	String name, date, comment ;
	double rating, comfort, style;
	int product_id, id;
	
	public double getComfort() {
		return comfort;
	}
	public void setComfort(double comfort) {
		this.comfort = comfort;
	}
	public double getStyle() {
		return style;
	}
	public void setStyle(double style) {
		this.style = style;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	
}
