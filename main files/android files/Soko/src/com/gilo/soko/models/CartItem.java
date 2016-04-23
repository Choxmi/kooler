package com.gilo.soko.models;

import java.io.Serializable;

public class CartItem implements Serializable {

	Item item;
	String options;
	int Qty;
	
	public int getQty() {
		return Qty;
	}
	public void setQty(int qty) {
		Qty = qty;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	
	
}
