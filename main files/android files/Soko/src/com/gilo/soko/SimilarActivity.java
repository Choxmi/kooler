package com.gilo.soko;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.gilo.soko.XMLParsers.ItemsInitiator;
import com.gilo.soko.config.Config;
import com.gilo.soko.models.Item;
import com.gilo.wooApi.soko.R;
import com.squareup.picasso.Picasso;

public class SimilarActivity extends ActionBarActivity{

	ListView lvShopItems;
	
	ImageView ivThumbnail;
	TextView  tvName, tvPrice, tvBrand;
	RatingBar rbRating;
	int id= 0;
	Item item;
	
	protected void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.similar);
		
		Intent intent = getIntent();
		id = intent.getIntExtra("item_id", 1);
		item = new ItemsInitiator(this).getItem(id);
		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTextColor(Color.parseColor("#4E6A9F"));
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		//getActionBar().setIcon(R.drawable.ic_ab_logo);
		getActionBar().setTitle("Similar to "+ new ItemsInitiator(this).getItem(id).getName());
		
		tvName = (TextView) findViewById(R.id.tvSimilar_name);
		tvBrand = (TextView) findViewById(R.id.tvSimilar_brand);
		tvPrice = (TextView) findViewById(R.id.tvSimilar_product_price);
		rbRating = (RatingBar) findViewById(R.id.rbSimilar_rating);
		ivThumbnail = (ImageView) findViewById(R.id.ivSimilar_thumbnail);
		
		
		Picasso.with(this).load(Config.IMAGE_THUMBNAIL+ item.getFeatured_image()).into(ivThumbnail);

		
		rbRating.setRating(item.getReview());
		
		
		tvName.setText(item.getName());
		tvBrand.setText(item.getShop());
		tvPrice.setText(getResources().getString(R.string.currency) + String.format("%.2f", item.getPrice()));
		
		lvShopItems = (ListView) findViewById(R.id.lvShopItems);
		lvShopItems.setAdapter(new ShopItemsAdapter(SimilarActivity.this, new ItemsInitiator(SimilarActivity.this).getSimilarItem(id)));
		

	}
	
	
	public class ShopItemsAdapter extends BaseAdapter {

		Context context;
		ArrayList<Item> items;
		Typeface font, font_light;
		
		
		public ShopItemsAdapter(Context c, ArrayList<Item> items) {
			this.context = c;
			
			this.items = items;
			font = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Roman.otf");
			font_light = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Lt.otf");
		}

		public int getCount() {
			// TODO Auto-generated method stub
			if(items.size() % 2 == 1)
				return items.size()/2 + 1;
			
			return items.size()/2;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.shop_single_item, null, false);
			
			
			
			LinearLayout llSingleItemCol1 = (LinearLayout) v.findViewById(R.id.llsingle_item_col1);
			ImageView ivImage1 = (ImageView) v.findViewById(R.id.ivshop_single_image1);
			ImageView ivBannerNew1 = (ImageView) v.findViewById(R.id.ivshop_single_banner_new1);
			ImageView ivBannerHot1 = (ImageView) v.findViewById(R.id.ivshop_single_banner_hot1);
			TextView tvName1 = (TextView) v.findViewById(R.id.tvshop_single_name1);
			TextView tvBrand1 = (TextView) v.findViewById(R.id.tvshop_single_brand1);
			TextView tvPrice1 = (TextView) v.findViewById(R.id.tvshop_single_price1);
			TextView tvStock1 = (TextView) v.findViewById(R.id.tvshop_single_stock1);
			RatingBar rbRate1 = (RatingBar) v.findViewById(R.id.rbshop_single_rate1);
			
			
			LinearLayout llSingleItemCol2 = (LinearLayout) v.findViewById(R.id.llsingle_item_col2);
			ImageView ivImage2 = (ImageView) v.findViewById(R.id.ivshop_single_image2);
			ImageView ivBannerNew2 = (ImageView) v.findViewById(R.id.ivshop_single_banner_new2);
			ImageView ivBannerHot2 = (ImageView) v.findViewById(R.id.ivshop_single_banner_hot2);
			TextView tvName2 = (TextView) v.findViewById(R.id.tvshop_single_name2);
			TextView tvBrand2 = (TextView) v.findViewById(R.id.tvshop_single_brand2);
			TextView tvPrice2 = (TextView) v.findViewById(R.id.tvshop_single_price2);
			TextView tvStock2 = (TextView) v.findViewById(R.id.tvshop_single_stock2);
			RatingBar rbRate2 = (RatingBar) v.findViewById(R.id.rbshop_single_rate2);
			
			tvName1.setTypeface(font);
			tvName2.setTypeface(font);
			tvBrand1.setTypeface(font_light);
			tvBrand2.setTypeface(font_light);
			tvPrice1.setTypeface(font);
			tvPrice2.setTypeface(font);
			llSingleItemCol1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent =  new Intent(SimilarActivity.this, SingleProductDrawerActivity.class);
					intent.putExtra("item_id", items.get(position * 2).getId());
					startActivity(intent);
				}
			});
			
			String image_name1 = items.get(position * 2).getFeatured_image(); 
			Uri path1 = Uri.parse("android.resource://"+ context.getResources().getString(R.string.package_name) + "/drawable/" + image_name1);
			ivImage1.setImageURI(path1);
			
			Picasso.with(context).load(Config.IMAGE_MEDIUM+ items.get(position * 2).getFeatured_image()).into(ivImage1);
			
			if(items.get(position * 2).getIsHot()){
				ivBannerHot1.setVisibility(ImageView.VISIBLE);
			}
			if(items.get(position * 2).getIsNew()){
				ivBannerNew1.setVisibility(ImageView.VISIBLE);
			}
			
			String name = items.get(position * 2).getName();
			if(name.length() < 9 ){
				tvName1.setText(name);
			}else{
				tvName1.setText(name.substring(0, 9) + "...");
			}
			
			String brand = items.get(position * 2).getShop();
			if(brand.length() < 27 ){
				tvBrand1.setText(brand);
			}else{
				tvBrand1.setText(brand.substring(0, 27) + "...");
			}
			
			tvStock1.setText(items.get(position * 2).getStock());
			tvPrice1.setText(context.getResources().getString(R.string.currency) + String.format("%.2f", items.get(position * 2).getPrice()));
			rbRate1.setRating(items.get(position * 2).getReview());
			
			if(position * 2 + 1 != items.size()){
				name = items.get(position * 2 + 1).getName();
				if(name.length() < 9 ){
					tvName2.setText(name);
				}else{
					tvName2.setText(name.substring(0, 9) + "...");
				}
				
				brand = items.get(position * 2 + 1).getShop();
				if(brand.length() < 27 ){
					tvBrand2.setText(brand);
				}else{
					tvBrand2.setText(brand.substring(0, 27) + "...");
				}
				
				tvStock2.setText(items.get(position * 2 + 1).getStock());
				tvPrice2.setText(context.getResources().getString(R.string.currency) + String.format("%.2f", items.get(position * 2 + 1).getPrice()));
				rbRate2.setRating(items.get(position * 2 + 1).getReview());
				
				String image_name2 = items.get(position * 2 + 1).getFeatured_image(); 
				Uri path2 = Uri.parse("android.resource://"+ context.getResources().getString(R.string.package_name) + "/drawable/" + image_name2);
				ivImage2.setImageURI(path2);
				
				Picasso.with(context).load(Config.IMAGE_MEDIUM + items.get(position * 2 + 1).getFeatured_image()).into(ivImage2);

				
				if(items.get(position * 2 + 1).getIsHot()){
					ivBannerHot2.setVisibility(ImageView.VISIBLE);
				}
				if(items.get(position * 2 + 1).getIsNew()){
					ivBannerNew2.setVisibility(ImageView.VISIBLE);
				}
				
				llSingleItemCol2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent =  new Intent(SimilarActivity.this, SingleProductDrawerActivity.class);
						intent.putExtra("item_id", items.get(position * 2 + 1).getId());
						startActivity(intent);	
					}
				});
			}
			
			if(position==items.size()/2 && items.size() % 2 == 1){
				llSingleItemCol2.setVisibility(LinearLayout.INVISIBLE);
			}
			
			return v;
		}
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	
        	Intent intent = new Intent(SimilarActivity.this,
					SingleProductDrawerActivity.class);
			intent.putExtra("item_id", id);
			startActivity(intent);
            
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	public class CategoryItemsAdapter extends BaseAdapter {

		Context context;
		String categories[] = {
				"Title", "Gender", "Women", "Men", "Boys", "Girls"
		};
		String results[] = {
				"", "", "1238 results", "456 results", "247 results", "998 results"
		};
		Typeface font, font_light;
		
		public CategoryItemsAdapter(Context c) {
			this.context = c;
			font = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Roman.otf");
			font_light = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Lt.otf");
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return categories.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.shop_single_category_indented, null, false);
			
			if(position == 0){
				v = inflater.inflate(R.layout.shop_categories_header, null, false);
				
				return v;
			}else if(position == 1){
				v = inflater.inflate(R.layout.shop_single_category, null, false);
				TextView name = (TextView) v.findViewById(R.id.tvshop_single_category_name);
				name.setText(categories[position]);
				
				return v;
			}
			
			TextView name = (TextView) v.findViewById(R.id.tvshop_single_category_name);
			name.setText(categories[position]);
			name.setTypeface(font);
			
			TextView tvResults = (TextView) v.findViewById(R.id.tvshop_single_category_results);
			tvResults.setText(results[position]);
			tvResults.setTypeface(font_light);
			
			return v;
		}
	}
}
