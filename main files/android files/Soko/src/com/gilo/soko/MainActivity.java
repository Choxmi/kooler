package com.gilo.soko;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.gilo.soko.XMLParsers.ItemsInitiator;
import com.gilo.soko.config.Config;
import com.gilo.soko.db.Functions;
import com.gilo.soko.interfaces.BackPressListener;
import com.gilo.soko.interfaces.HTTPExecutionListener;
import com.gilo.soko.models.Banner;
import com.gilo.soko.models.Category;
import com.gilo.soko.models.Country;
import com.gilo.soko.models.Item;
import com.gilo.soko.models.ItemImage;
import com.gilo.soko.models.Option;
import com.gilo.soko.models.OptionGroup;
import com.gilo.soko.models.Product;
import com.gilo.soko.models.ProductCategory;
import com.gilo.soko.models.Zone;
import com.gilo.soko.utils.ConnectionDetector;
import com.gilo.soko.utils.JSONParser;
import com.gilo.wooApi.soko.R;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.UnderlinePageIndicator;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends Activity {

	ProgressDialog pDialog;

	public static final int NUM_PAGES = 3;

	ViewPager mPager, vpMarqueeText;

	ArrayList<Banner> banners = new ArrayList<Banner>();
	TextView tvCategories;
	GiloAdapter mAdapter;
	ArrayList<Product> products = new ArrayList<Product>();
	ArrayList<ItemImage> images = new ArrayList<ItemImage>();
	ArrayList<Option> options = new ArrayList<Option>();
	ArrayList<OptionGroup> optionGroups = new ArrayList<OptionGroup>();

	ArrayList<Item> items = new ArrayList<Item>();
	ArrayList<Category> categories = new ArrayList<Category>();
	ArrayList<ProductCategory> prodcats = new ArrayList<ProductCategory>();
	ArrayList<Country> countries = new ArrayList<Country>();
	ArrayList<Zone> zones = new ArrayList<Zone>();
	
	static HTTPExecutionListener httpExecutionListener = new HTTPExecutionListener() {
		
		@Override
		public void onPreExecute() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onComplete(int task_number) {
			// TODO Auto-generated method stub
			
		}
	};

	DataContainer dataContainer;
	
	Time now;
	
	static int task_number = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		
		now = new Time();
		now.setToNow();
		
		pDialog = new ProgressDialog(MainActivity.this);
		
		Log.d("start time" , String.valueOf(now.minute) + ":" + String.valueOf(now.second));
		
		dataContainer = ((DataContainer) getApplicationContext());
		tvCategories = (TextView) findViewById(R.id.tvIntro_categoriess);
		
		banners = new ArrayList<Banner>();

		// Instantiating the adapter
		mAdapter = new GiloAdapter(this, banners);

		// instantiate the Views
		mPager = (ViewPager) findViewById(R.id.pager);
		vpMarqueeText = (ViewPager) findViewById(R.id.vpMarqueeText);
		vpMarqueeText.setAdapter(new MarqueeTextAdapter(this));

		mPager.setAdapter(mAdapter);
		mPager.setOffscreenPageLimit(banners.size());
		vpMarqueeText.setOffscreenPageLimit(5);

		UnderlinePageIndicator mIndicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

		startTimerForSlides();
		startMarqueeing();

		String number = String.valueOf(new ItemsInitiator(this).itemCount());

		Date now = new Date();
		
		
		new DataGetterLocal().execute();
		
		// doStuff();
	}
	
	

	public static void setHttpExecutionListener(
			HTTPExecutionListener httpExecutionListener) {
		MainActivity.httpExecutionListener = httpExecutionListener;
	}



	public static HTTPExecutionListener getHttpExecutionListener() {
		return httpExecutionListener;
	}



	public class DataGetter extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			JSONParser jsonParser = new JSONParser();
			JSONObject json;

			JSONObject categoriesObject = jsonParser.makeHttpRequest(
					Config.CATEGORIES_URL, "GET",
					new ArrayList<NameValuePair>());
			
			JSONArray categoriesArray = null;
			try {
				categoriesArray = categoriesObject.getJSONArray("product_categories");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(categoriesArray == null){
				return null;
			}
			
			try {

				JSONObject parentCategory = new JSONObject();
				parentCategory.put("id", 0);
				parentCategory.put("name", "Categories");
				parentCategory.put("parent", -1);
				parentCategory.put("sequence", 0);

				categoriesArray.put(parentCategory);
				categories = new ArrayList<Category>();

				for (int i = 0; i < categoriesArray.length(); i++) {
					JSONObject categoryObject = categoriesArray
							.getJSONObject(i);
					Category category = new Category();

					category.setCategory_id(categoryObject.getInt("id"));
					category.setSequence(i + 1);
					category.setName(categoryObject.getString("name"));
					category.setParent_id(categoryObject.getInt("parent"));
					category.setParent(String.valueOf(category.getParent_id()));
					category.setSlug(String.valueOf(category.getCategory_id()));
					
					
					Log.d("slug", category.getSlug());
					

					if(i == 0){
						NoSQL.with(getBaseContext()).using(ProductCategory.class)
					    .bucketId("categories")
					    .delete();
					}
					

					NoSQLEntity<Category> entity = new NoSQLEntity<Category>(
							"categories", String.valueOf(category
									.getCategory_id()));
					entity.setData(category);
					NoSQL.with(MainActivity.this).using(Category.class)
							.save(entity);
					Log.d("category id",
							String.valueOf(category.getCategory_id()));

					categories.add(category);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * products
			 */
			JSONObject productsObject = jsonParser.makeHttpRequest(
					Config.PRODUCTS_URL, "GET", new ArrayList<NameValuePair>());
			
			JSONArray productsArray = null;
			try {
				productsArray = productsObject.getJSONArray("products");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}

			

				for (int i = 0; i < productsArray.length(); i++) {
					try{
					JSONObject productObject = productsArray.getJSONObject(i);
					Product product = new Product();

					product.setProduct_id(productObject.getInt("id"));
					product.setCategory("1");
					product.setPrice(productObject.getDouble("price"));
					product.setProduct_name(productObject.getString("title"));

					product.setBrand("");
					product.setTags("tags");
					product.setReview(5);
					product.setStock(100);
					
					if(productObject.getBoolean("featured")){
						product.setIs_hot(1);
					}else{
						product.setIs_hot(0);
					}
					
					if(productsArray.length() - i < 3){
						product.setIs_new(1);
					}else{
						product.setIs_new(0);
					}
					
					product.setInfo(productObject.getString("description"));
					
					product.setFeatured_image(productObject.getString("featured_src"));
					
					JSONArray imagesArray = productObject.getJSONArray("images");
					for(int x= 0; x < imagesArray.length(); x++){
						JSONObject singleImage = imagesArray.getJSONObject(x);

						ItemImage image = new ItemImage();
						image.setImage_id(singleImage.getInt("id"));
						image.setProduct_id((product.getProduct_id()));
						image.setImage(singleImage.getString("src"));
						
						image.setPosition(singleImage.getInt("position"));
						
						image.setColor_id("Variation");
						image.setVariation_value("Default");

						NoSQLEntity<ItemImage> entity = new NoSQLEntity<ItemImage>(
								"images", String.valueOf(image.getImage()));
						entity.setData(image);
						NoSQL.with(MainActivity.this)
								.using(ItemImage.class).save(entity);
						Log.d("image id",
								String.valueOf(image.getImage_id()));
					}

					NoSQLEntity<Product> entity = new NoSQLEntity<Product>(
							"products", String.valueOf(product.getProduct_id()));
					entity.setData(product);
					NoSQL.with(MainActivity.this).using(Product.class)
							.save(entity);

					Log.d("product id", String.valueOf(product.getProduct_id()));
					
					 JSONArray productCategoryArray = productObject.getJSONArray("categories");
					 
					
					for (int y = 0; y < productCategoryArray.length(); y++) {
						//JSONObject productCategoryObject = productCategoryArray.getJSONObject(y);
						ProductCategory prodcat = new ProductCategory();

						
						String category = productCategoryArray.getString(y);
						Log.d("category", category);
						
						prodcat.setCategory_id(getCategory(category, categories ));
						prodcat.setProduct_id(product.getProduct_id());

						prodcat.setId(Integer.parseInt(String.valueOf(prodcat
								.getProduct_id())
								+ String.valueOf((prodcat.getCategory_id()))));
						
						
						

						NoSQLEntity<ProductCategory> entity2 = new NoSQLEntity<ProductCategory>(
								"product_categories", String.valueOf(prodcat
										.getId()));
						entity2.setData(prodcat);
						NoSQL.with(MainActivity.this).using(ProductCategory.class)
								.save(entity2);
						
						Log.d("product category id",
								String.valueOf(prodcat.getId()));

						prodcats.add(prodcat);
					}
					
					JSONArray optionsArray = productObject.getJSONArray("attributes");
					try {
						for (int z = 0; z < optionsArray.length(); z++) {
							JSONObject optionObject = optionsArray.getJSONObject(z);
							OptionGroup optionGroup = new OptionGroup();

							optionGroup.setOption_group_id(Integer.parseInt(product.getProduct_id() + "" +z));
							
							optionGroup.setName(optionObject.getString("name"));
							optionGroup.setProduct_id(product.getProduct_id());

							optionGroup.setHas_image(0);
							
							

							NoSQLEntity<OptionGroup> entity3 = new NoSQLEntity<OptionGroup>(
									"option_group", String.valueOf(optionGroup
											.getOption_group_id()));
							entity3.setData(optionGroup);
							NoSQL.with(MainActivity.this).using(OptionGroup.class)
									.save(entity3);
							Log.d("option group id",
									String.valueOf(optionGroup.getOption_group_id()));

							JSONArray optionValuesArray = optionObject
									.getJSONArray("options");

							for (int x = 0; x < optionValuesArray.length(); x++) {
								String option_name =  optionValuesArray.getString(x);
								Option option = new Option();

								option.setOption_id(Integer.parseInt(product.getProduct_id() + "" + z + "" + x));
								option.setName(option_name);
								option.setOption_title(optionGroup.getName());
								option.setProduct_id(optionGroup.getProduct_id());
								option.setOption_group_id(optionGroup.getOption_group_id());
								// option_id option_name option_title product_id
								
								
								
								NoSQLEntity<Option> entity2 = new NoSQLEntity<Option>(
										"options",
										String.valueOf(option.getOption_id()));
								entity2.setData(option);
								NoSQL.with(MainActivity.this).using(Option.class)
										.save(entity2);
								Log.d("option id",
										String.valueOf(option.getOption_id()));

							}

						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			NoSQL.with(MainActivity.this).using(Product.class)
					.bucketId("products")
					.retrieve(new RetrievalCallback<Product>() {

						@Override
						public void retrievedResults(
								List<NoSQLEntity<Product>> entities) {
							// TODO Auto-generated method stub
							if (entities.size() > 0) {

								products = new ArrayList<Product>();
								Log.d("products",
										String.valueOf(entities.size()));
								for (NoSQLEntity<Product> entity : entities) {
									products.add(entity.getData());
								}
								Log.d("product retrival", "done");
							}

							NoSQL.with(MainActivity.this)
									.using(ItemImage.class)
									.bucketId("images")
									.retrieve(
											new RetrievalCallback<ItemImage>() {

												@Override
												public void retrievedResults(
														List<NoSQLEntity<ItemImage>> entities) {
													// TODO Auto-generated
													// method stub
													if (entities.size() > 0) {
														images = new ArrayList<ItemImage>();
														for (NoSQLEntity<ItemImage> entity : entities) {
															images.add(entity
																	.getData());
														}
														Log.d("images retrival",
																"done");
													}

													NoSQL.with(
															MainActivity.this)
															.using(Option.class)
															.bucketId("options")
															.retrieve(
																	new RetrievalCallback<Option>() {

																		@Override
																		public void retrievedResults(
																				List<NoSQLEntity<Option>> entities) {
																			// TODO
																			// Auto-generated
																			// method
																			// stub
																			if (entities
																					.size() > 0) {
																				options = new ArrayList<Option>();
																				for (NoSQLEntity<Option> entity : entities) {
																					options.add(entity
																							.getData());
																				}
																				Log.d("options retrival",
																						"done");
																			}

																			NoSQL.with(
																					MainActivity.this)
																					.using(OptionGroup.class)
																					.bucketId(
																							"option_group")
																					.retrieve(
																							new RetrievalCallback<OptionGroup>() {

																								@Override
																								public void retrievedResults(
																										List<NoSQLEntity<OptionGroup>> entities) {
																									// TODO
																									// Auto-generated
																									// method
																									// stub
																									if (entities
																											.size() > 0) {
																										optionGroups = new ArrayList<OptionGroup>();
																										for (NoSQLEntity<OptionGroup> entity : entities) {
																											optionGroups
																													.add(entity
																															.getData());
																										}
																										Log.d("option group retrival",
																												"done");
																									}

																									items = new Functions(
																											MainActivity.this,
																											products,
																											images,
																											options,
																											optionGroups)
																											.getItems();
																									dataContainer
																											.setItems(items);
																									
																									NoSQL.with(MainActivity.this).using(Item.class)
																								    .bucketId("items")
																								    .delete();
																									
																									for (Item item : items) {
																										NoSQLEntity<Item> entity = new NoSQLEntity<Item>(
																												"items",
																												String.valueOf(item
																														.getId()));
																										entity.setData(item);
																										NoSQL.with(
																												MainActivity.this)
																												.using(Item.class)
																												.save(entity);
																									}

																								}

																							});

																		}
																	});
												}
											});
						}
					});

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			doStuff();

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}
	
	
	public int getCategory(String category, ArrayList<Category> categories){
		for(Category cat : categories){
			if(cat.getName().equals(category)){
				return cat.getCategory_id();
			}
		}
		
		return 0;
	}
	
	/*
	 * Start of country getter
	 * 
	 * 
	 * 
	 */
	
	public class DataGetterCountry extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			JSONParser jsonParser = new JSONParser();
			
			countries = new ArrayList<Country>();
			JSONArray countryArray = jsonParser
					.makeHttpRequestForArray(Config.COUNTRIES_URL, "GET",
							new ArrayList<NameValuePair>());
			
			if(countryArray == null){
				return null;
			}
			
			try {

				for (int i = 0; i < countryArray.length(); i++) {
					JSONObject countryObject = countryArray.getJSONObject(i);
					Country country = new Country();

					country.setId(countryObject.getString("id"));
					country.setIso_code_2(countryObject.getString("iso_code_2"));
					country.setIso_code_3(countryObject.getString("iso_code_3"));
					country.setName(countryObject.getString("name"));
					country.setSequence(countryObject.getString("sequence"));
					country.setStatus(countryObject.getString("status"));
					country.setTax(countryObject.getString("tax"));
					country.setZip_required(countryObject
							.getString("zip_required"));

					NoSQLEntity<Country> entity = new NoSQLEntity<Country>(
							"countries", String.valueOf(country.getId()));
					entity.setData(country);
					NoSQL.with(MainActivity.this).using(Country.class)
							.save(entity);

					//Log.d("country id", String.valueOf(country.getId()));

					countries.add(country);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			

			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			doStuff();

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}
	
	
	
	
	/*
	 * 
	 * End of country getter
	 * 
	 */
	
	/*
	 * Start of zone getter
	 * 
	 * 
	 * 
	 */
	
	public class DataGetterZone extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			JSONParser jsonParser = new JSONParser();

			zones = new ArrayList<Zone>();
			JSONArray zonesArray = jsonParser.makeHttpRequestForArray(
					Config.ZONES_URL, "GET", new ArrayList<NameValuePair>());
			
			if(zonesArray == null){
				return null;
			}
			try {

				for (int i = 0; i < zonesArray.length(); i++) {
					JSONObject zoneObject = zonesArray.getJSONObject(i);
					Zone zone = new Zone();

					zone.setId(zoneObject.getString("id"));
					zone.setCountry_id(zoneObject.getString("country_id"));
					zone.setCode(zoneObject.getString("code"));
					zone.setName(zoneObject.getString("name"));
					zone.setStatus(zoneObject.getString("status"));
					zone.setTax(zoneObject.getString("tax"));

					NoSQLEntity<Zone> entity = new NoSQLEntity<Zone>("zones",
							String.valueOf(zone.getId()));
					entity.setData(zone);
					NoSQL.with(MainActivity.this).using(Zone.class)
							.save(entity);

					// Log.d("zone id", String.valueOf(zone.getId()));

					zones.add(zone);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			doStuff();

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}
	
	
	
	
	/*
	 * 
	 * End of zones getter
	 * 
	 */
	
	
	/*
	 * Start of banner getter
	 * 
	 */
	
	public class DataGetterBanners extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			JSONParser jsonParser = new JSONParser();
			JSONObject json;

			/*
			 * Banners
			 */

			Log.d("banners url", Config.BANNERS_URL);
			JSONArray bannersArray = jsonParser.makeHttpRequestForArray(
					Config.BANNERS_URL, "GET", new ArrayList<NameValuePair>());
			
			if(bannersArray == null){
				return null;
			}
			try {

				for (int i = 0; i < bannersArray.length(); i++) {
					JSONObject bannerObject = bannersArray.getJSONObject(i);
					Banner banner = new Banner();
					banner.setBannerId(bannerObject.getInt("banner_id"));
					banner.setCategoryId(bannerObject
							.getInt("banner_collection_id"));
					banner.setIsShowing(1);
					banner.setOrder(bannerObject.getInt("sequence"));
					banner.setImage(bannerObject.getString("image"));

					NoSQLEntity<Banner> entity = new NoSQLEntity<Banner>(
							"banners", String.valueOf(banner.getBannerId()));
					entity.setData(banner);
					NoSQL.with(MainActivity.this).using(Banner.class)
							.save(entity);
					Log.d("id", String.valueOf(banner.getBannerId()));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			doStuff();

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}
	
	
	/*
	 * 
	 * End of banner getter
	 */

	public void doStuff() {
		
		dataContainer.setItems(items);
		dataContainer.setCategories(categories);
		dataContainer.setProdcats(prodcats);
		dataContainer.setCountries(countries);
		dataContainer.setZones(zones);
		
		now.setToNow();
		Log.d("start time" , String.valueOf(now.minute) + ":" + String.valueOf(now.second));
		
		
		Log.d("items size ", String.valueOf(items.size()));
		
		NoSQL.with(MainActivity.this).using(Banner.class).bucketId("banners")
				.retrieve(new RetrievalCallback<Banner>() {

					@Override
					public void retrievedResults(
							List<NoSQLEntity<Banner>> entities) {
						// TODO Auto-generated method stub
						banners.clear();
						if (entities.size() > 0) {
							for (NoSQLEntity<Banner> entity : entities) {
								banners.add(entity.getData());
							}
						}

						mAdapter.notifyDataSetChanged();
						ProgressBar pbLoading = (ProgressBar) findViewById(R.id.pbIntroLoading);

						if (products.size() != 0) {
							pbLoading.setVisibility(ProgressBar.GONE);
							pDialog.dismiss();
						}
					}
				});

		task_number++;
		httpExecutionListener.onComplete(task_number);

	}

	public void startTimerForSlides() {

		final Handler handler = new Handler();
		final Runnable Update = new Runnable() {
			int currentPage = 0;

			public void run() {

				if (currentPage == NUM_PAGES) {
					currentPage = 0;
				}
				mPager.setCurrentItem(currentPage++, true);

			}
		};

		final Timer swipeTimer = new Timer();
		swipeTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.post(Update);
			}
		}, 1000, 3000);

	}

	public void startMarqueeing() {
		final Handler handler = new Handler();
		final Runnable Update = new Runnable() {
			int currentPage = 0;

			public void run() {

				if (currentPage == Config.marquee_texts.length) {
					currentPage = 0;
				}
				vpMarqueeText.setCurrentItem(currentPage++, true);
			}
		};
		Timer swipeTimer = new Timer();
		swipeTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.post(Update);
			}
		}, 2500, 6000);

	}

	private class GiloAdapter extends PagerAdapter {

		Context context;
		ArrayList<Banner> banners;

		public GiloAdapter(Context c, ArrayList<Banner> banners) {
			this.context = c;
			this.banners = banners;
		}

		// This is the number of pages -- 5
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return banners.size();
		}

		@Override
		public float getPageWidth(int position) {

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
		public Object instantiateItem(View pager, final int position) {
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.intro_single_slide, null, false);
			ImageView image = (ImageView) v.findViewById(R.id.ivImageSlide);

			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MainActivity.this,
							ShopDrawerActivity.class);
					intent.putExtra("category_id", banners.get(position)
							.getCategoryId());
					startActivity(intent);
				}
			});

			Picasso.with(context)
					.load(Config.IMAGE_FULL + banners.get(position).getImage())
					.into(image);

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

	private class MarqueeTextAdapter extends PagerAdapter {

		Context context;

		public MarqueeTextAdapter(Context c) {
			this.context = c;
		}

		// This is the number of pages -- 5
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Config.marquee_texts.length;
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
			View v = inflater.inflate(R.layout.intro_single_marquee_text, null,
					false);

			TextView tvMarqueeText = (TextView) v
					.findViewById(R.id.tvSingleMarquee);

			tvMarqueeText.setText(Config.marquee_texts[position]);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_shopping, menu);
		return true;
	}

	public class DataGetterLocal extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			NoSQL.with(MainActivity.this).using(Category.class)
					.bucketId("categories")
					.retrieve(new RetrievalCallback<Category>() {

						@Override
						public void retrievedResults(
								List<NoSQLEntity<Category>> entities) {
							// TODO Auto-generated method stub

							if (entities.size() > 0) {

								for (NoSQLEntity<Category> entity : entities) {

									categories.add(entity.getData());
									Log.d("category local id", String
											.valueOf(entity.getData()
													.getCategory_id()));

								}

							}
						}
					});

			NoSQL.with(MainActivity.this).using(Country.class)
					.bucketId("countries")
					.retrieve(new RetrievalCallback<Country>() {

						@Override
						public void retrievedResults(
								List<NoSQLEntity<Country>> entities) {
							// TODO Auto-generated method stub

							if (entities.size() > 0) {

								for (NoSQLEntity<Country> entity : entities) {

									countries.add(entity.getData());

								}

							}
						}
					});

			NoSQL.with(MainActivity.this).using(Zone.class).bucketId("zones")
					.retrieve(new RetrievalCallback<Zone>() {

						@Override
						public void retrievedResults(
								List<NoSQLEntity<Zone>> entities) {
							// TODO Auto-generated method stub

							if (entities.size() > 0) {

								for (NoSQLEntity<Zone> entity : entities) {

									zones.add(entity.getData());
								}

							}
						}
					});

			NoSQL.with(MainActivity.this).using(ProductCategory.class)
					.bucketId("product_categories")
					.retrieve(new RetrievalCallback<ProductCategory>() {

						@Override
						public void retrievedResults(
								List<NoSQLEntity<ProductCategory>> entities) {
							// TODO Auto-generated method stub

							if (entities.size() > 0) {

								for (NoSQLEntity<ProductCategory> entity : entities) {

									prodcats.add(entity.getData());
									Log.d("product category local id", String
											.valueOf(entity.getData()
													.getCategory_id()));

								}

							}
						}
					});

			NoSQL.with(MainActivity.this).using(Product.class)
					.bucketId("products")
					.retrieve(new RetrievalCallback<Product>() {

						@Override
						public void retrievedResults(
								List<NoSQLEntity<Product>> entities) {
							// TODO Auto-generated method stub
							if (entities.size() > 0) {
								Log.d("products",
										String.valueOf(entities.size()));
								for (NoSQLEntity<Product> entity : entities) {
									products.add(entity.getData());
								}
								Log.d("product retrival", "done");
							}

							NoSQL.with(MainActivity.this)
									.using(ItemImage.class)
									.bucketId("images")
									.retrieve(
											new RetrievalCallback<ItemImage>() {

												@Override
												public void retrievedResults(
														List<NoSQLEntity<ItemImage>> entities) {
													// TODO Auto-generated
													// method stub
													if (entities.size() > 0) {
														for (NoSQLEntity<ItemImage> entity : entities) {
															images.add(entity
																	.getData());
														}
														Log.d("images retrival",
																"done");
													}

													NoSQL.with(
															MainActivity.this)
															.using(Option.class)
															.bucketId("options")
															.retrieve(
																	new RetrievalCallback<Option>() {

																		@Override
																		public void retrievedResults(
																				List<NoSQLEntity<Option>> entities) {
																			// TODO
																			// Auto-generated
																			// method
																			// stub
																			if (entities
																					.size() > 0) {
																				for (NoSQLEntity<Option> entity : entities) {
																					options.add(entity
																							.getData());
																				}
																				Log.d("options retrival",
																						"done");
																			}

																			NoSQL.with(
																					MainActivity.this)
																					.using(OptionGroup.class)
																					.bucketId(
																							"option_group")
																					.retrieve(
																							new RetrievalCallback<OptionGroup>() {

																								@Override
																								public void retrievedResults(
																										List<NoSQLEntity<OptionGroup>> entities) {
																									// TODO
																									// Auto-generated
																									// method
																									// stub
																									if (entities
																											.size() > 0) {
																										for (NoSQLEntity<OptionGroup> entity : entities) {
																											optionGroups
																													.add(entity
																															.getData());
																										}
																										Log.d("option group retrival",
																												"done");
																									}

																									items = new Functions(
																											MainActivity.this,
																											products,
																											images,
																											options,
																											optionGroups)
																											.getItems();
																									dataContainer
																											.setItems(items);
																									for (Item item : items) {
																										NoSQLEntity<Item> entity = new NoSQLEntity<Item>(
																												"items",
																												String.valueOf(item
																														.getId()));
																										entity.setData(item);
																										NoSQL.with(
																												MainActivity.this)
																												.using(Item.class)
																												.save(entity);
																									}

																								}

																							});

																		}
																	});
												}
											});
						}
					});

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			final Configuration croutonConfiguration = new Configuration.Builder()
			.setDuration(2500).build();

			doStuff();

			Thread timer = new Thread() {
				public void run() {
					try {
						sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						new DataGetter().execute();
						new DataGetterCountry().execute();
						new DataGetterZone().execute();
						new DataGetterBanners().execute();
						
					}
				}
			};

			tvCategories.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(items.size() > 0){
					startActivity(new Intent(MainActivity.this,
							ShopDrawerActivity.class));
					}else{
						Style style_error = new Style.Builder()
						.setBackgroundColorValue(Color.parseColor("#f14749"))
						.setGravity(Gravity.CENTER_HORIZONTAL)
						.setPaddingInPixels(30)
						.setConfiguration(croutonConfiguration)

						.setTextSize(16).build();
						
						Crouton.makeText(
								MainActivity.this,
								"Oops, The shop is still not compeletely setup. Wait a few moments.",
								style_error).show();
					}
				}
			});
			
			if (new ConnectionDetector(MainActivity.this)
					.isConnectingToInternet()) {
				timer.start();
			} else {
				
				// Define custom styles for crouton
				Style style = new Style.Builder()
						.setBackgroundColorValue(Color.parseColor("#6a89ae"))
						.setGravity(Gravity.CENTER_HORIZONTAL)
						.setPaddingInPixels(30)
						.setConfiguration(croutonConfiguration)

						.setTextSize(16).build();

				Style style_error = new Style.Builder()
						.setBackgroundColorValue(Color.parseColor("#f14749"))
						.setGravity(Gravity.CENTER_HORIZONTAL)
						.setPaddingInPixels(30)
						.setConfiguration(croutonConfiguration)

						.setTextSize(16).build();
				
				Crouton.makeText(
						MainActivity.this,
						"Oops, Unable to connect to the Internet.",
						style_error).show();
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			
			pDialog.setMessage("Setting up the shop");
			pDialog.setCancelable(false);
			pDialog.show();
			
			
			super.onPreExecute();
		}
	}

}
