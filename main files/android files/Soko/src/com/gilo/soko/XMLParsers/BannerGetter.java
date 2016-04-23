package com.gilo.soko.XMLParsers;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.gilo.soko.models.Banner;
import com.gilo.wooApi.soko.R;

public class BannerGetter {

	Context context;
	
	public BannerGetter(Context context){
		this.context = context;
	}
	
	public ArrayList<Banner> getBanners() {
		ArrayList<Banner> banners = new ArrayList<Banner>();

		Resources res = context.getResources();
		XmlResourceParser xrp = res.getXml(R.xml.banners);

		Banner banner = new Banner();

		try {
			xrp.next();
			String text = "";
			int event = xrp.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = xrp.getName();

				switch (event) {
				case XmlPullParser.START_TAG:
					if (name.equals("banner")) {
						banner = new Banner();
					}
					
				break;
				case XmlPullParser.TEXT:
					text = xrp.getText();
					break;

				case XmlPullParser.END_TAG:
					if (name.equals("banner")) {
						banners.add(banner);
					}

					if (name.equals("image")) {
						banner.setImage(text);
					}
					
					if (name.equals("category-slug")) {
						banner.setCategory_slug(text);
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

		return banners;
	}
}
