package com.gilo.soko.XMLParsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.gilo.soko.DataContainer;
import com.gilo.soko.models.Item;
import com.gilo.soko.models.ItemImage;
import com.gilo.soko.models.Option;
import com.gilo.wooApi.soko.R;

public class ItemsInitiator {

	ArrayList<Item> items = new ArrayList<Item>();
	Context context;

	public ItemsInitiator(Context context) {
		this.context = context;
		DataContainer dataContainer = ((DataContainer) context.getApplicationContext());
	    items = dataContainer.getItems();
	}

	public ArrayList<Item> getItems() {
		ArrayList<Item> items = new ArrayList<Item>();
		ArrayList<ItemImage> images = new ArrayList<ItemImage>();

		ArrayList<Option> options = new ArrayList<Option>();
		ArrayList<String> actual_options = new ArrayList<String>();
		ArrayList<String> option_images = new ArrayList<String>();

		ItemImage itemImage = new ItemImage();

		Option option = new Option();
		String attr_value = "";

		Resources res = context.getResources();
		XmlResourceParser xrp = res.getXml(R.xml.items);

		Item item = new Item();

		try {
			xrp.next();
			String text = "";
			int event = xrp.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = xrp.getName();

				switch (event) {
				case XmlPullParser.START_TAG:
					if (name.equals("item")) {
						item = new Item();
						images = new ArrayList<ItemImage>();
					}
					if (name.equals("options")) {
						options = new ArrayList<Option>();
					}
					if (name.equals("option")) {
						actual_options = new ArrayList<String>();
						option_images = new ArrayList<String>();
						option = new Option();
						option.setStyle(xrp.getAttributeValue(null, "style"));
						option.setName(xrp.getAttributeValue(null, "name"));

					}

					if (name.equals(option.getName())
							&& option.getStyle().equals("images")) {
						attr_value = xrp.getAttributeValue(null, "image");

						ArrayList<String> color_ids = option.getColor_id();
						color_ids.add(xrp.getAttributeValue(null, "color_id"));
						option.setColor_id(color_ids);
					}

					if (name.equals("image")) {
						itemImage = new ItemImage();
						itemImage.setColor_id(xrp.getAttributeValue(null,
								"color_id"));

						// Log.d("color_id", itemImage.getColor_id());
					}

					break;
				case XmlPullParser.TEXT:
					text = xrp.getText();
					break;

				case XmlPullParser.END_TAG:
					if (name.equals("item")) {
						items.add(item);
					}

					if (name.equals("id")) {
						item.setId(Integer.parseInt(text));
					}
					if (name.equals("name")) {
						item.setName(text);
					}
					if (name.equals("shop")) {
						item.setShop(text);
					}
					if (name.equals("tags")) {
						item.setTags(text);
					}
					if (name.equals("category")) {
						item.setCategory(text);
					}
					if (name.equals("review")) {
						item.setReview(Float.parseFloat(text));
					}
					if (name.equals("price")) {
						item.setPrice(Float.parseFloat(text));
					}
					if (name.equals("featured-image")) {
						item.setFeatured_image(text);
					}
					if (name.equals("info")) {
						item.setInfo(text);
					}
					if (name.equals("new")) {
						item.setIsNew(Boolean.valueOf(text));
					}
					if (name.equals("hot")) {
						item.setIsHot(Boolean.valueOf(text));
					}
					if (name.equals("stock")) {
						item.setStock(text);
					}

					if (name.equals("image")) {
						itemImage.setImage(text);
						images.add(itemImage);
						item.setImages(images);
					}

					if (name.equals("option")) {
						options.add(option);
					}
					if (name.equals("options")) {
						item.setOptions(options);
					}

					if (name.equals(option.getName())
							&& option.getStyle().equals("images")) {
						actual_options.add(text);
						option.setOptions(actual_options);

						option_images.add(attr_value);
						option.setImages(option_images);
						Log.d("image", attr_value);
					} else if (name.equals(option.getName())) {
						actual_options.add(text);
						option.setOptions(actual_options);
					}

					break;
				}
				event = xrp.next();

			}

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return items;
	}

	public int itemCount() {
		return getItems().size();
	}

	public ArrayList<Item> getSimilarItem(int id) {
		ArrayList<Item> search_results = new ArrayList<Item>();
		ArrayList<Item> all_items = items;

		Item item = getItem(id);
		List<String> tags = Arrays.asList(item.getTags().split(","));

		for (String tag : tags) {
			for (Item an_item : all_items) {
				if (an_item.getTags().toLowerCase().trim()
						.contains(tag.toLowerCase().trim())) {
					if (!search_results.contains(an_item)
							&& an_item.getId() != id)
						search_results.add(an_item);
				}

			}
		}

		return search_results;
	}

	public ArrayList<Item> searchItem(String querry) {
		ArrayList<Item> search_results = new ArrayList<Item>();
		ArrayList<Item> all_items = items;

		for (Item an_item : all_items) {
			if (an_item.getTags().toLowerCase().trim().contains(querry.toLowerCase().trim())) {
				search_results.add(an_item);
				continue;
			}
			
			if (an_item.getName().toLowerCase().trim().contains(querry.toLowerCase().trim())) {
				search_results.add(an_item);
				continue;
			}
			
			if (an_item.getShop().toLowerCase().trim().contains(querry.toLowerCase().trim())) {
				search_results.add(an_item);
				continue;
			}
			
			if (an_item.getInfo().toLowerCase().trim().contains(querry.toLowerCase().trim())) {
				search_results.add(an_item);
				continue;
			}

		}

		return search_results;
	}

	public Item getItem(int id) {
		Item item = new Item();

		for (Item it : items) {
			if (it.getId() == id) {
				return it;
			}
		}

		return item;
	}
}
