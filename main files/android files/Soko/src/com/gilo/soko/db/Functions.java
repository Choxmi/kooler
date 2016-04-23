package com.gilo.soko.db;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.gilo.soko.models.Item;
import com.gilo.soko.models.ItemImage;
import com.gilo.soko.models.Option;
import com.gilo.soko.models.OptionGroup;
import com.gilo.soko.models.Product;

public class Functions {


	Context context;
	ArrayList<Product> products;
	ArrayList<ItemImage> images = new ArrayList<ItemImage>();
	ArrayList<Option> options = new ArrayList<Option>();
	ArrayList<OptionGroup> optionGroups = new ArrayList<OptionGroup>();
	
	
	public Functions(Context context){
		this.context = context;
		
		products = new ArrayList<Product>();
		
		NoSQL.with(context).using(Product.class).bucketId("products")
		.retrieve(new RetrievalCallback<Product>() {

			@Override
			public void retrievedResults(
					List<NoSQLEntity<Product>> entities) {
				// TODO Auto-generated method stub
				if (entities.size() > 0) {
					for(NoSQLEntity<Product> entity : entities){
						products.add(entity.getData());
					}
					Log.d("product retrival", "done");
				}
			}
		});
		
		NoSQL.with(context).using(ItemImage.class).bucketId("images")
		.retrieve(new RetrievalCallback<ItemImage>() {

			@Override
			public void retrievedResults(
					List<NoSQLEntity<ItemImage>> entities) {
				// TODO Auto-generated method stub
				if (entities.size() > 0) {
					for(NoSQLEntity<ItemImage> entity : entities){
						images.add(entity.getData());
					}
					Log.d("images retrival", "done");
				}
			}
		});
		
		NoSQL.with(context).using(Option.class).bucketId("options")
		.retrieve(new RetrievalCallback<Option>() {

			@Override
			public void retrievedResults(
					List<NoSQLEntity<Option>> entities) {
				// TODO Auto-generated method stub
				if (entities.size() > 0) {
					for(NoSQLEntity<Option> entity : entities){
						options.add(entity.getData());
					}
					Log.d("options retrival", "done");
				}
			}
		});
	}
	
	public Functions(Context context,
			ArrayList<Product> products, ArrayList<ItemImage> images,
			ArrayList<Option> options,
			ArrayList<OptionGroup> optionGroups
			) {
		this.context = context;
		this.products = products;
		this.images = images;
		this.options = options;
		this.optionGroups = optionGroups;
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Item> getItems() {
		ArrayList<Item> items = new ArrayList<Item>();
		
		for(Product product : products){
			Item item = new Item();
			item.setId(product.getProduct_id());
			item.setCategory(product.getCategory());
			item.setFeatured_image(product.getFeatured_image());
			item.setInfo(product.getInfo());
			
			if(product.getIs_hot() == 0){
				item.setIsHot(false);
			}else{
				item.setIsHot(true);
			}
			if(product.getIs_new() == 0){
				item.setIsNew(false);
			}else{
				item.setIsNew(true);
			}
			item.setName(product.getProduct_name());
			item.setOptions(getOptions(product.getProduct_id()));
			item.setPrice((float) product.getPrice());
			item.setReview((float) product.getReview());
			item.setShop(product.getBrand());
			item.setStock(String.valueOf(product.getStock()) + " ITEMS REMAINING");
			item.setTags(product.getTags());
			item.setImages(getImages(product.getProduct_id()));
			
			items.add(item);
		}
		Log.d("getItems()", "done");
		return items;
	}
	
	public ArrayList<ItemImage> getImages(int product_id){
		ArrayList<ItemImage> productImages = new  ArrayList<ItemImage>();
		
		for(ItemImage image : images){
			for(Option option : options){
				if(option.getOption_id() == image.getOption_id()){
					image.setColor_id(String.valueOf(option.getOption_id()));
				}
			}
			
			if(product_id == image.getProduct_id()){
				productImages.add(image);
			}
		}
		
		return productImages;
	}
	
	public ArrayList<Option> getOptions(int product_id){
		ArrayList<Option> productOptions = new  ArrayList<Option>();
		
		for(OptionGroup optionGroup : optionGroups){
			if(optionGroup.getProduct_id() == product_id){
				Option option = new Option();
				option.setName(optionGroup.getName());
				option.setStyle("text");
				option.setOption_id(optionGroup.getOption_group_id());
				
				for(Option opt : options){
					
					Log.d("option name", opt.getName());
					
					if(opt.getOption_group_id() == optionGroup.getOption_group_id()){
						//option.setImages(images);
						ArrayList<String> arr = option.getOptions();
						if(arr == null){
							arr= new ArrayList<String>();
						}
						arr.add(opt.getName());
						option.setOptions(arr);
						
						ArrayList<String> img = option.getImages();
						if(img == null){
							img= new ArrayList<String>();
						}
						img.add(getOptionImage(opt.getOption_id()));
						option.setImages(img);
						
						ArrayList<String> color_id = option.getColor_id();
						if(color_id == null){
							color_id= new ArrayList<String>();
						}
						color_id.add(String.valueOf(opt.getOption_id()));
						option.setColor_id(color_id);
					}
				}
				
				
				
				productOptions.add(option);
			}
			
		}
		
		
		
		return productOptions;
	}
	
	public String getOptionImage(int id){
		
		for(ItemImage image : images){
			if(image.getOption_id() == id){
				return image.getImage();
			}
		}
		
		return "";
	}
	
	public boolean doesItContainTitle(String option_title, ArrayList<Option> opts){
		for(Option option : opts){
			if(option.getOption_title().equals(option_title)){
				return true;
			}
		}
		
		return false;
	}
	
	public class OptionComparator implements Comparator<Option>{
	    public int compare(Option option1, Option option2){
	        return option1.getOption_title().compareTo(option2.getOption_title());
	    }
	}
}
