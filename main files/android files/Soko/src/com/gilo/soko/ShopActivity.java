package com.gilo.soko;

import it.sephiroth.android.library.widget.HListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import com.gilo.soko.XMLParsers.ItemsInitiator;
import com.gilo.soko.config.Config;
import com.gilo.soko.interfaces.BackPressListener;
import com.gilo.soko.interfaces.HTTPExecutionListener;
import com.gilo.soko.models.Category;
import com.gilo.soko.models.Item;
import com.gilo.soko.models.ProductCategory;
import com.gilo.wooApi.soko.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.picasso.Picasso;

public class ShopActivity extends ActionBarActivity {

	ListView lvShopItems, lvCategorylist;
	ArrayList<ProductCategory> prodCats = new ArrayList<ProductCategory>();
	
ArrayList<Item> products = new ArrayList<Item>();
	ArrayList<Category> categories;
	ArrayList<Item> items;
	static String slug = "0";
	GiloAdapter mAdapter;
	CategoryItemsAdapter catAdapter;
	ArrayList<Category> categories_with_slug = new ArrayList<Category>();
	ShopItemsAdapter shopItemsAdapter;
	ArrayList<Item> items_with_slug = new ArrayList<Item>();
	static ArrayList<Category> path = new ArrayList<Category>();
	HListView hlBreadCrumbs;
	SlidingMenu menuRight, menu ;
	Thread timer;
	BackPressListener backPressListener;
	public static Item curr_item;
	
	BreadCrumbsAdapter breadCrumbsAdapter;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.sliding_menu);
		
		final DataContainer dataContainer = ((DataContainer) getApplicationContext());
	    
		
		menu = (SlidingMenu) findViewById(R.id.slidingmenulayout);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidth(30);
		// menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffset(getResources().getDisplayMetrics().widthPixels/2 - 100);
		menu.setFadeDegree(0.35f);
		//menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		
		menu.setMenu(R.layout.category_sidebar);
		menu.showMenu();
		
		slug = ShopDrawerActivity.slug;
		slug="0";
		
		

		//categories = dataContainer.getCategories();
		//items = dataContainer.getItems();
		
		categories = new ArrayList<Category>();
		for(Category category : dataContainer.getCategories()){
			categories.add(category);
		}
		
		Collections.sort(categories, new SequenceComparator());
		
		items = new ArrayList<Item>();
		for(Item item : dataContainer.getItems()){
			items.add(item);
		}
		products = items;
		
		Log.d("items number", String.valueOf(items.size()));
		
		//prodCats = dataContainer.getProdcats();
		
		prodCats = new ArrayList<ProductCategory>();
		for(ProductCategory prodCat : dataContainer.getProdcats()){
			prodCats.add(prodCat);
		}
		
		getPath(slug);

		lvShopItems = (ListView) findViewById(R.id.lvShopItems);
		getItems(slug);
		
		shopItemsAdapter = new ShopItemsAdapter(ShopActivity.this, items_with_slug);
		lvShopItems.setAdapter(shopItemsAdapter);

		lvCategorylist = (ListView) findViewById(R.id.lvCategoryItems);
		getCategories(slug);
		catAdapter = new CategoryItemsAdapter(this, categories_with_slug);
		lvCategorylist.setAdapter(catAdapter);

		lvCategorylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				if (pos > 1) {

					slug = categories_with_slug.get(pos - 2).getSlug();
					updateLists();
				}
			}
		});
		
		
		
		
		breadCrumbsAdapter = new BreadCrumbsAdapter(this, path);
		
		hlBreadCrumbs = (HListView) findViewById(R.id.hlShop_breadcrumb);
		hlBreadCrumbs.setAdapter(breadCrumbsAdapter);
		
//		hlBreadCrumbs.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
//					long arg3) {
//				// TODO Auto-generated method stub
//				
//				if(pos == 0){
//					slug = "none";
//				}else{	
//					slug = path.get(path.size() - pos).getSlug();
//				}
//				updateLists();
//				
//			}
//		});
		
		
		
		
		final Handler myHandler = new Handler() {
		    public void handleMessage(Message msg) {
		    	onDrawerBackPressed();
		    }
		};
		
		ShopDrawerActivity.setBackPressListener(new BackPressListener() {
			
			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Boolean back_pressed) {
				// TODO Auto-generated method stub
				onDrawerBackPressed();
			}
		});
		
		Log.d("Category with slug 0", getCategoryWithSlug("0").getName());
		
	}
	

	public void onDrawerBackPressed() {
	    if(path.size() == 0){
	    	finish();
	    	
	    }else{
	    	path.remove(0);
	    	if(path.size() != 0){
	    	slug = path.get(0).getSlug();
	    	
	    	updateLists();
	    	}else{
	    		slug = "0";
	    		updateLists();
	    	}
	    }
	    
	    Log.d("Back Pressed", "back pressed");
	}

	public void updateLists() {
		while (categories_with_slug.size() != 0) {
			categories_with_slug.remove(0);
		}

		while (items_with_slug.size() != 0) {
			items_with_slug.remove(0);
		}

		while (path.size() != 0) {
			path.remove(0);
		}
		
		getPath(slug);
		getCategories(slug);
		getItems(slug);
		
		
		catAdapter.notifyDataSetChanged();
		shopItemsAdapter.notifyDataSetChanged();
		breadCrumbsAdapter.notifyDataSetChanged();
		
		hlBreadCrumbs.smoothScrollToPosition(breadCrumbsAdapter.getCount() - 1);
		
	}

	public void getCategories(String slug) {
		
		for (Category category : categories) {
			Log.d("category parent id", String.valueOf(category.getParent_id()));
			if (category.getParent_id() == Integer.parseInt(slug)) {
				categories_with_slug.add(category);
				Log.d("slug", category.getSlug());
			}
		}
		
		Log.d("categories with slug size", String.valueOf(categories_with_slug.size()));

	}
	
	
	
	


	public void getPath(String slug){
		String parent = slug;
		String cats = "";
		
		int i = 50;
		
		while (!parent.equals("0") && i > 0) {
			cats+= parent;
			
			Log.d("path", cats);
			Category category = getCategoryWithSlug(parent);
			if(!path.contains(category))
				path.add(category);
			
			parent = category.getParent();
			
			i--;
			
		}
		
		Log.d("path", cats);
		
		
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		MenuItem searchViewMenuItem = menu.findItem(R.id.mnSearch);    
		SearchView mSearchView = (SearchView) searchViewMenuItem.getActionView();
	    int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
	    ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
	    v.setImageResource(R.drawable.search); 
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_shopping, menu);
		return true;
	}

	public Category getCategoryWithSlug(String slug) {
		Category result = new Category();

		for (Category category : categories) {
			if (Integer.parseInt(category.getSlug()) == Integer.parseInt(slug)) {

				return category;
			}
		}

		return result;
	}

	public int getItemsUnderCategory(String slug) {
		int result = 0;
		for (ProductCategory prodcat : prodCats) {
			if (prodcat.getCategory_id() == Integer.parseInt(slug)) {
				result++;
			}
		}

		return result;
	}

	public void getItems(String slug) {

		ArrayList<Item> items_to_remove = new ArrayList<Item>();
		for (Item item : items) {
			if(!items_with_slug.contains(item)){
				items_with_slug.add(item);
			}
		}
		
		for (Item item : items_with_slug) {
			  for(Category category : path){
				  if(!item.getCategory().toLowerCase().contains(category.getSlug().toLowerCase())){
					  items_to_remove.add(item);
				  }
			  }
		}
		
		items_with_slug.removeAll(items_to_remove);
		
		while(items_with_slug.size() != 0){
			items_with_slug.remove(0);
		}
		
		for (ProductCategory prodcat : prodCats) {
			if (prodcat.getCategory_id() == Integer.parseInt(slug) ) {
				items_with_slug.add(getItemWithId(prodcat.getProduct_id()));
			}
		}
		
		if(Integer.parseInt(slug) == 0){
			for(Item item : products){
				items_with_slug.add(item);
			}
		}

	}
	
	
	public class SequenceComparator implements Comparator<Category>
	{
	    public int compare(Category left, Category right) {
	        return left.getSequence() - right.getSequence();
	    }
	}

	private Item getItemWithId(int product_id) {
		// TODO Auto-generated method stub
		for(Item item : items){
			if(item.getId() == product_id){
				return item;
			}
		}
		return null;
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
		
		

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return super.isEmpty();
		}



		public int getCount() {
			// TODO Auto-generated method stub
			if (items.size() % 2 == 1)
				return items.size() / 2 + 1;

			return items.size() / 2;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.shop_single_item, null, false);

			LinearLayout llSingleItemCol1 = (LinearLayout) v
					.findViewById(R.id.llsingle_item_col1);
			ImageView ivImage1 = (ImageView) v
					.findViewById(R.id.ivshop_single_image1);
			ImageView ivBannerNew1 = (ImageView) v
					.findViewById(R.id.ivshop_single_banner_new1);
			ImageView ivBannerHot1 = (ImageView) v
					.findViewById(R.id.ivshop_single_banner_hot1);
			TextView tvName1 = (TextView) v
					.findViewById(R.id.tvshop_single_name1);
			TextView tvBrand1 = (TextView) v
					.findViewById(R.id.tvshop_single_brand1);
			TextView tvPrice1 = (TextView) v
					.findViewById(R.id.tvshop_single_price1);
			TextView tvStock1 = (TextView) v
					.findViewById(R.id.tvshop_single_stock1);
			RatingBar rbRate1 = (RatingBar) v
					.findViewById(R.id.rbshop_single_rate1);

			LinearLayout llSingleItemCol2 = (LinearLayout) v
					.findViewById(R.id.llsingle_item_col2);
			ImageView ivImage2 = (ImageView) v
					.findViewById(R.id.ivshop_single_image2);
			ImageView ivBannerNew2 = (ImageView) v
					.findViewById(R.id.ivshop_single_banner_new2);
			ImageView ivBannerHot2 = (ImageView) v
					.findViewById(R.id.ivshop_single_banner_hot2);
			TextView tvName2 = (TextView) v
					.findViewById(R.id.tvshop_single_name2);
			TextView tvBrand2 = (TextView) v
					.findViewById(R.id.tvshop_single_brand2);
			TextView tvPrice2 = (TextView) v
					.findViewById(R.id.tvshop_single_price2);
			TextView tvStock2 = (TextView) v
					.findViewById(R.id.tvshop_single_stock2);
			RatingBar rbRate2 = (RatingBar) v
					.findViewById(R.id.rbshop_single_rate2);

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
					curr_item = items.get(position * 2 );
					Intent intent = new Intent(ShopActivity.this,
							SingleProductDrawerActivity.class);
					intent.putExtra("item_id", items.get(position * 2).getId());
					startActivity(intent);
					

				}
			});

			 Picasso.with(context).load(Config.IMAGE_THUMBNAIL+ items.get(position * 2).getFeatured_image()).placeholder(R.drawable.placeholder).into(ivImage1);

			if (items.get(position * 2).getIsHot()) {
				ivBannerHot1.setVisibility(ImageView.VISIBLE);
			}
			if (items.get(position * 2).getIsNew()) {
				ivBannerNew1.setVisibility(ImageView.VISIBLE);
			}

			String name = items.get(position * 2).getName();
			tvName1.setText(name);
			

			String brand = items.get(position * 2).getShop();
			tvBrand1.setText(brand);
			
			tvStock1.setText(items.get(position * 2).getStock());
			tvPrice1.setText(context.getResources().getString(R.string.currency)
					+ String.format("%.2f", items.get(position * 2).getPrice()));
			rbRate1.setRating(items.get(position * 2).getReview());

			if (position * 2 + 1 != items.size()) {
				name = items.get(position * 2 + 1).getName();
				tvName2.setText(name);
				

				brand = items.get(position * 2 + 1).getShop();
				tvBrand2.setText(brand);
				

				tvStock2.setText(items.get(position * 2 + 1).getStock());
				tvPrice2.setText(context.getResources().getString(R.string.currency)
						+ String.format("%.2f", items.get(position * 2 + 1)
								.getPrice()));
				rbRate2.setRating(items.get(position * 2 + 1).getReview());

				Picasso.with(context).load(Config.IMAGE_THUMBNAIL + items.get(position * 2 + 1).getFeatured_image()).placeholder(R.drawable.placeholder).into(ivImage2);

				if (items.get(position * 2 + 1).getIsHot()) {
					ivBannerHot2.setVisibility(ImageView.VISIBLE);
				}
				if (items.get(position * 2 + 1).getIsNew()) {
					ivBannerNew2.setVisibility(ImageView.VISIBLE);
				}

				llSingleItemCol2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						curr_item = items.get(position * 2 + 1);
						Intent intent = new Intent(ShopActivity.this,
								SingleProductDrawerActivity.class);
						intent.putExtra("item_id", items.get(position * 2 + 1)
								.getId());
						startActivity(intent);
						
					}
				});
			}

			if (position == items.size() / 2 && items.size() % 2 == 1) {
				llSingleItemCol2.setVisibility(LinearLayout.INVISIBLE);
			}

			return v;
		}
	}

	public class CategoryItemsAdapter extends BaseAdapter {

		Context context;

		Typeface font, font_light;
		ArrayList<Category> categories;

		public CategoryItemsAdapter(Context c, ArrayList<Category> categories) {
			this.context = c;
			font = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Roman.otf");
			font_light = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Lt.otf");

			this.categories = categories;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return categories.size() + 2;
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
			View v = inflater.inflate(R.layout.shop_single_category_indented,
					null, false);

			if (position == 0) {
				v = inflater.inflate(R.layout.shop_categories_header, null,
						false);

				return v;
			} else if (position == 1) {
				v = inflater
						.inflate(R.layout.shop_single_category, null, false);
				TextView name = (TextView) v
						.findViewById(R.id.tvshop_single_category_name);
				name.setText(getCategoryWithSlug(slug).getName());

				return v;
			}

			TextView name = (TextView) v
					.findViewById(R.id.tvshop_single_category_name);
			name.setText(categories.get(position - 2).getName());
			name.setTypeface(font);

			TextView tvResults = (TextView) v
					.findViewById(R.id.tvshop_single_category_results);
			tvResults.setText(String.format("%d results",
					getItemsUnderCategory(categories.get(position - 2)
							.getSlug())));
			tvResults.setTypeface(font_light);

			return v;
		}
	}
	
	public class BreadCrumbsAdapter extends BaseAdapter {

		Context context;

		Typeface font, font_light;
		ArrayList<Category> categories;

		public BreadCrumbsAdapter(Context c, ArrayList<Category> categories) {
			this.context = c;
			font = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Roman.otf");
			font_light = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Lt.otf");

			this.categories = categories;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return categories.size() + 1;
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
			View v = inflater.inflate(R.layout.single_breadcrumb,
					null, false);

			TextView tvTitle = (TextView) v.findViewById(R.id.tvSingleBreadCrumb_text);
			
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(position == 0){
						slug = "0";
					}else{	
						slug = path.get(path.size() - position).getSlug();
					}
					updateLists();
				}
			});
			
			
			if(position == categories.size() ){
				tvTitle.setTypeface(font);
				
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				params.setMargins(0,0,150,0);
				//tvTitle.setLayoutParams(params);
				
				ImageView image = (ImageView) v.findViewById(R.id.ivSingleBreadCrumb_image_divider);
				image.setLayoutParams(params);
				
			}else{
				tvTitle.setTypeface(font_light);
			}
			
			if(position == 0){
				//tvTitle.setTypeface(font);
				tvTitle.setText(getString(R.string.categories));
				
				
				return v;
			}
			
			tvTitle.setText(categories.get(categories.size() - position).getName());

			return v;
		}
	}

	private class GiloAdapter extends PagerAdapter {

		Context context;
		CategoryItemsAdapter adapter;
		ArrayList<Category> categories_with_slug;

		public GiloAdapter(Context c) {
			this.context = c;
		}

		// This is the number of pages -- 5
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public float getPageWidth(int position) {
			if (position == 0)
				return 0.5f;

			return 1.0f;
		}

		@Override
		public boolean isViewFromObject(View v, Object o) {
			// TODO Auto-generated method stub
			return v.equals(o);
		}

		// This is the title of the page that will apppear on the "tab"
		public CharSequence getPageTitle(int position) {
			return "";
		}

		// This is where all the magic happen
		public Object instantiateItem(View pager, int position) {
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.shop, null, false);

			if (position == 1) {
				lvShopItems = (ListView) v.findViewById(R.id.lvShopItems);

				lvShopItems.setAdapter(new ShopItemsAdapter(ShopActivity.this,
						new ItemsInitiator(ShopActivity.this).getItems()));
				

			} else {
				v = inflater.inflate(R.layout.shop_categories, null, false);
				lvShopItems = (ListView) v.findViewById(R.id.lvShopItems);

				for (Category cat : categories_with_slug) {
					Log.d("cat_with_slu", cat.getSlug());
				}

				adapter = new CategoryItemsAdapter(ShopActivity.this,
						categories_with_slug);

				lvShopItems.setAdapter(adapter);

				lvShopItems.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						// TODO Auto-generated method stub
						if (pos > 1) {
							Intent intent = new Intent(ShopActivity.this,
									ShopActivity.class);
							intent.putExtra("slug",
									categories_with_slug.get(pos - 2).getSlug());
							startActivity(intent);

						}
					}
				});
			}
			// This is very important
			((ViewPager) pager).addView(v, 0);

			return v;
		}

		@Override
		public void destroyItem(View pager, int position, Object view) {
			((ViewPager) pager).removeView((View) view);
		}

		@Override
		public void finishUpdate(View view) {
		}

		@Override
		public void restoreState(Parcelable p, ClassLoader c) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View view) {
		}

	}
	
	
}
