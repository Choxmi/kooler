package com.gilo.soko.XMLParsers;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.gilo.soko.models.Category;
import com.gilo.wooApi.soko.R;

public class CategoryGetter {

	Context context; 
	
	public CategoryGetter(Context context){
		this.context = context;
		
	}
	
	
	public ArrayList<Category> getCategories() {
		ArrayList<Category> categories = new ArrayList<Category>();

		Resources res = context.getResources();
		XmlResourceParser xrp = res.getXml(R.xml.categories);

		Category category = new Category();

		try {
			xrp.next();
			String text = "";
			int event = xrp.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = xrp.getName();

				switch (event) {
				case XmlPullParser.START_TAG:
					if (name.equals("category")) {
						category = new Category();	
					}
					
				break;
				case XmlPullParser.TEXT:
					text = xrp.getText();
					break;

				case XmlPullParser.END_TAG:
					if (name.equals("category")) {
						categories.add(category);
					}

					if (name.equals("name")) {
						category.setName(text);
					}
					
					if (name.equals("parent")) {
						category.setParent(text);
					}
					
					if (name.equals("slug")) {
						category.setSlug(text);
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

		return categories;
	}
	
	
}
