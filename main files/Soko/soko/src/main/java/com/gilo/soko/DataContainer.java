package com.gilo.soko;

import java.util.ArrayList;

import android.app.Application;

import com.gilo.soko.models.Category;
import com.gilo.soko.models.Country;
import com.gilo.soko.models.Item;
import com.gilo.soko.models.ProductCategory;
import com.gilo.soko.models.Zone;

public class DataContainer extends Application{

	ArrayList<Item> items = new ArrayList<Item>();
	ArrayList<Category> categories = new ArrayList<Category>();
	ArrayList<ProductCategory> prodcats = new ArrayList<ProductCategory>();
	ArrayList<Country> countries = new ArrayList<Country>();
	ArrayList<Zone> zones = new ArrayList<Zone>();
	
	public ArrayList<Country> getCountries() {
		return countries;
	}

	public void setCountries(ArrayList<Country> countries) {
		this.countries = new ArrayList<Country>();
		this.countries = countries;
	}

	public ArrayList<Zone> getZones() {
		
		return zones;
	}

	public void setZones(ArrayList<Zone> zones) {
		this.zones = new ArrayList<Zone>();
		this.zones = zones;
	}

	public ArrayList<ProductCategory> getProdcats() {
		return prodcats;
	}

	public void setProdcats(ArrayList<ProductCategory> prodcats) {
		this.prodcats = new ArrayList<ProductCategory>();
		this.prodcats = prodcats;
	}

	public void setItems(ArrayList<Item> items){
		this.items = new ArrayList<Item>();
		this.items = items;
	}
	
	public ArrayList<Item> getItems(){
		return this.items;
	}

	public ArrayList<Category> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<Category> categories) {
		this.categories = categories;
	}
}
