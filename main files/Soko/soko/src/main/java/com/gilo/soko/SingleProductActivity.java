package com.gilo.soko;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Html.TagHandler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.gilo.soko.XMLParsers.ItemsInitiator;
import com.gilo.soko.config.Config;
import com.gilo.soko.interfaces.CartChangeListener;
import com.gilo.soko.models.CartItem;
import com.gilo.soko.models.Comment;
import com.gilo.soko.models.Item;
import com.gilo.soko.models.ItemImage;
import com.gilo.soko.models.Option;
import com.gilo.soko.utils.JSONParser;
import com.gilo.soko.utils.TinyDB;
import com.gilo.soko.views.HorizontalListView;
import com.gilo.wooApi.soko.R;
import com.squareup.picasso.Picasso;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class SingleProductActivity extends ActionBarActivity {

	Boolean infoVisible = false, commentVisible = false;
	TextView tvInfo, tvName, tvPrice, tvBrand, tvOptionValue1, tvOptionValue2,
			tvOptionValue3, tvOptionName1, tvOptionName2, tvOptionName3;
	RatingBar rbRating;
	ProgressBar pbCommentsLoading;
	ImageView ivThumbnail;
	ScrollView svInfo;
	Button bSimilar, bAddToCart;
	Style style, style_error;
	double rating = 0;

	Boolean inCart = false;
	
	Option colorOption = null;

	int col_with_images = 0;
	int color_option_pos = 0;
	
	ThumbNailsItemsAdapter thumbAdapter;
	ColorThumbsAdapter colorThumbsAdapter;
	
	ArrayList<Comment> comments = new ArrayList<Comment>();

	String color_id = "default";

	TinyDB tinydb;
	ArrayList<Integer> loveArray;

	ListView lvComments;
	int id = 0;
	int option_num = 0;
	Item item = new Item();
	ArrayList<ItemImage> itemImages = new ArrayList<ItemImage>();
	GiloAdapter mAdapter;

	static CartChangeListener cartChangeListener;
	
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.view_product);

		Intent intent = getIntent();
		id = SingleProductDrawerActivity.ITEM_ID;
		item = new ItemsInitiator(this).getItem(id);

		//item = ShopActivity.curr_item;
		
		getColorId();
		

		// Define configuration options
		Configuration croutonConfiguration = new Configuration.Builder()
				.setDuration(2500).build();
		style = new Style.Builder()
				.setBackgroundColorValue(Color.parseColor("#6a89ae"))
				.setGravity(Gravity.CENTER_HORIZONTAL).setPaddingInPixels(30)
				.setConfiguration(croutonConfiguration)

				.setTextSize(16).build();
		
		style_error = new Style.Builder()
		.setBackgroundColorValue(Color.parseColor("#f14749"))
		.setGravity(Gravity.CENTER_HORIZONTAL).setPaddingInPixels(30)
		.setConfiguration(croutonConfiguration)

		.setTextSize(16).build();
		
		ArrayList<ItemImage> sortedImages = item.getImages();
		//Sorting 
		Collections.sort(sortedImages, new Comparator<ItemImage>() {
		        @Override
		        public int compare(ItemImage  image1, ItemImage  image2){
		            return  image1.getPosition() - image2.getPosition();
		        }
		    });
		
		item.setImages(sortedImages);
		
		

		for (ItemImage itemImage : item.getImages()) {
			if (itemImage.getColor_id().equals(color_id)) {
				itemImages.add(itemImage);
			}
		}
		
//		for (ItemImage itemImage : item.getImages()) {
//				itemImages.add(itemImage);
//		}

		
		
		
		mAdapter = new GiloAdapter(this, itemImages);

		// instantiate the Views
		final ViewPager mPager = (ViewPager) findViewById(R.id.pager);

		svInfo = (ScrollView) findViewById(R.id.svView_product_scroll_info);

		tvInfo = (TextView) findViewById(R.id.tvView_product_info);
		tvInfo.setText(Html.fromHtml(item.getInfo().trim(),null, new MyTagHandler()));

		tvName = (TextView) findViewById(R.id.tvView_product_name);
		tvBrand = (TextView) findViewById(R.id.tvView_product_brand);
		tvPrice = (TextView) findViewById(R.id.tvView_product_price);
		rbRating = (RatingBar) findViewById(R.id.rbView_product_rating);
		ivThumbnail = (ImageView) findViewById(R.id.ivView_product_thumbnail);
		bSimilar = (Button) findViewById(R.id.bView_product_similar);
		bAddToCart = (Button) findViewById(R.id.bView_product_add_to_cart);

		bSimilar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SingleProductActivity.this,
						SimilarActivity.class);
				intent.putExtra("item_id", id);

				startActivity(intent);
			}
		});

		NoSQL.with(this).using(CartItem.class).bucketId("cart")
				.entityId(String.valueOf(item.getId()))
				.retrieve(new RetrievalCallback<CartItem>() {

					@Override
					public void retrievedResults(
							List<NoSQLEntity<CartItem>> entities) {
						// TODO Auto-generated method stub
						if (entities.size() > 0) {
							CartItem cartItem = entities.get(0).getData(); 

							bAddToCart.setBackgroundColor(Color
									.parseColor("#f14749"));
							bAddToCart.setText("Remove from Cart");
							inCart = true;
						}
					}
				});

		bAddToCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!inCart){
					Crouton.makeText(
							SingleProductActivity.this,
							item.getName()
									+ " has been added to your shopping cart",
							style).show();
	
					CartItem cartItem = new CartItem();
					cartItem.setItem(item);
					
					
					String options = "";
	
					String option_name = "";
					String option_value="";
					
					options += item.getShop().trim() + "\n";
					
					if (item.getOptions().size() >= 1) {
						option_name = tvOptionName1.getText().toString().trim();
						option_value = tvOptionValue1.getText().toString().trim();
						
						options += option_name.substring(0, 1).toUpperCase() + option_name.substring(1) + ": "+ option_value.substring(0, 1).toUpperCase() + option_value.substring(1) + "\n";
					}
					if (item.getOptions().size() >= 2) {
						option_name = tvOptionName2.getText().toString().trim();
						option_value = tvOptionValue2.getText().toString().trim();
						
						options += option_name.substring(0, 1).toUpperCase() + option_name.substring(1) + ": "+ option_value.substring(0, 1).toUpperCase() + option_value.substring(1) + "\n";
					}
					if (item.getOptions().size() >= 3) {
						option_name = tvOptionName3.getText().toString().trim();
						option_value = tvOptionValue3.getText().toString().trim();
						
						options += option_name.substring(0, 1).toUpperCase() + option_name.substring(1) + ": "+ option_value.substring(0, 1).toUpperCase() + option_value.substring(1);
					}
					
					cartItem.setOptions(options);
					cartItem.setQty(1);
					
					NoSQLEntity<CartItem> entity = new NoSQLEntity<CartItem>(
							"cart", String.valueOf(item.getId()));
					entity.setData(cartItem);
					NoSQL.with(SingleProductActivity.this).using(CartItem.class)
							.save(entity);
	
					bAddToCart.setBackgroundColor(Color.parseColor("#f14749"));
					bAddToCart.setText("Remove from Cart");
					
					}else{
						
						Crouton.makeText(
								SingleProductActivity.this,
								item.getName()
										+ " has been removed from your shopping cart",
								style).show();
						
						NoSQL.with(SingleProductActivity.this).using(CartItem.class)
					    .bucketId("cart")
					    .entityId(String.valueOf(item.getId()))
					    .delete();
						
						bAddToCart.setBackgroundColor(Color
								.parseColor("#55c892"));
						bAddToCart.setText("Add to Cart");
					}
				inCart = !inCart;
				cartChangeListener.onCartChanged();
			}
		});

		ListView lvThumbs = (ListView) findViewById(R.id.lvlvView_product_thumbnails);
		thumbAdapter = new ThumbNailsItemsAdapter(this, itemImages);
		thumbAdapter.setSelectedItem(0);
		lvThumbs.setAdapter(thumbAdapter);
		lvThumbs.setSelection(0);

		final HorizontalListView lvColorOptions = (HorizontalListView) findViewById(R.id.lvView_product_color_options);
		if (colorOption != null) {
			colorThumbsAdapter = new ColorThumbsAdapter(this, colorOption);
			colorThumbsAdapter.setSelectedItem(0);
			lvColorOptions.setAdapter(colorThumbsAdapter);
		} else {
			lvColorOptions.setVisibility(ListView.GONE);
		}

		lvColorOptions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				colorThumbsAdapter.setSelectedItem(pos);
				colorThumbsAdapter.notifyDataSetChanged();

				color_option_pos = pos;
				color_id = colorOption.getColor_id().get(pos);

				while (itemImages.size() != 0) {
					itemImages.remove(0);
				}

				for (ItemImage itemImage : item.getImages()) {
					if (itemImage.getColor_id().equals(color_id)) {
						itemImages.add(itemImage);
					}
				}
				
				mAdapter.notifyDataSetChanged();
				thumbAdapter.notifyDataSetChanged();

				switch (col_with_images) {
				case 1:

					tvOptionValue1.setText(colorOption.getOptions().get(pos));
					break;
				case 2:
					tvOptionValue2.setText(colorOption.getOptions().get(pos));
					break;
				case 3:
					tvOptionValue3.setText(colorOption.getOptions().get(pos));
					break;

				default:
					break;
				}
			}
		});

		lvThumbs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				thumbAdapter.setSelectedItem(pos);
				thumbAdapter.notifyDataSetChanged();

				mPager.setCurrentItem(pos);
			}
		});
		
		Picasso.with(this)
		.load(Config.IMAGE_MEDIUM+ item.getFeatured_image()).placeholder(R.drawable.placeholder)
		.into(ivThumbnail)
		;

		rbRating.setRating(item.getReview());

		tvName.setText(item.getName());
		tvBrand.setText(item.getShop());
		tvPrice.setText(getResources().getString(R.string.currency) + String.format("%.2f", item.getPrice()));

		
		pbCommentsLoading = (ProgressBar) findViewById(R.id.pbView_product_comments_progress);
		lvComments = (ListView) findViewById(R.id.lvView_product_comments);
		lvComments.setAdapter(new CommentsItemsAdapter(this, comments));
		
		new ReviewsGetter(this).execute();

		final ImageView ivInfo = (ImageView) findViewById(R.id.ivView_product_info);
		final ImageView ivComment = (ImageView) findViewById(R.id.ivView_product_comment);
		final ImageView ivLove = (ImageView) findViewById(R.id.ivView_product_love);

		ivInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!infoVisible) {
					mPager.setVisibility(ViewPager.GONE);
					svInfo.setVisibility(ScrollView.VISIBLE);
					lvComments.setVisibility(ListView.GONE);
					pbCommentsLoading.setVisibility(ProgressBar.GONE);
					commentVisible = false;
					infoVisible = true;

					ivInfo.setBackgroundColor(Color.parseColor("#E9ECFA"));
					ivComment.setBackgroundColor(Color.parseColor("#ffffff"));

				} else {
					mPager.setVisibility(ViewPager.VISIBLE);
					svInfo.setVisibility(ScrollView.GONE);
					commentVisible = false;
					infoVisible = false;
					ivInfo.setBackgroundColor(Color.parseColor("#ffffff"));
					ivComment.setBackgroundColor(Color.parseColor("#ffffff"));
				}

			}
		});

		ivComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				if (!commentVisible) {
					mPager.setVisibility(ViewPager.GONE);
					svInfo.setVisibility(ScrollView.GONE);
					lvComments.setVisibility(ListView.VISIBLE);
					pbCommentsLoading.setVisibility(ProgressBar.VISIBLE);
					infoVisible = false;
					commentVisible = true;

					ivInfo.setBackgroundColor(Color.parseColor("#ffffff"));
					ivComment.setBackgroundColor(Color.parseColor("#E9ECFA"));

				} else {
					mPager.setVisibility(ViewPager.VISIBLE);
					lvComments.setVisibility(TextView.GONE);
					pbCommentsLoading.setVisibility(ProgressBar.GONE);
					commentVisible = false;
					infoVisible = false;

					ivInfo.setBackgroundColor(Color.parseColor("#ffffff"));
					ivComment.setBackgroundColor(Color.parseColor("#ffffff"));
				}

			}
		});

		tinydb = new TinyDB(SingleProductActivity.this);
		loveArray = tinydb.getListInt("love_ids", SingleProductActivity.this);

		if (loveArray.contains(id))
			ivLove.setImageResource(R.drawable.love_red);

		ivLove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// Display notice with custom style and configuration

				if (loveArray.contains(id)) {
					ivLove.setImageResource(R.drawable.love);

					loveArray.remove(new Integer(id));
					tinydb.putListInt("love_ids", loveArray,
							SingleProductActivity.this);
					Crouton.makeText(
							SingleProductActivity.this,
							item.getName()
									+ " has been removed from your Favorites",
							style).show();
				} else {
					ivLove.setImageResource(R.drawable.love_red);

					loveArray.add(id);
					tinydb.putListInt("love_ids", loveArray,
							SingleProductActivity.this);

					Crouton.makeText(
							SingleProductActivity.this,
							item.getName()
									+ " has been added to your Favorites",
							style).show();
				}

			}
		});

		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub
				thumbAdapter.setSelectedItem(pos);
				thumbAdapter.notifyDataSetChanged();

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		buildOptions();
	}
	
	public static void setCartChangeListener(CartChangeListener cartChangeListenr){
		cartChangeListener = cartChangeListenr;
	}

	public void getColorId() {

		ArrayList<Option> options = item.getOptions();
		// Collections.reverse(options);
		
		Boolean optionExists = false;
		
		for (Option option : options) {
			if(option.getOption_id() == 0){
				optionExists = true;
			}
		}
		
		if(item.getImages().size() > 0 && !optionExists){
		Option imageOption = new Option();
		imageOption.setOption_group_id(0);
		imageOption.setOption_id(0);
		imageOption.setName(item.getImages().get(0).getVariation_value());
		imageOption.setStyle("images");
		
		ArrayList<String> images = new ArrayList<String>();
		ArrayList<String> option_values = new ArrayList<String>();
		for(ItemImage itemImage : item.getImages()){
			if(!images.contains(itemImage.getVariation_value())){
				
				if(!option_values.contains(itemImage.getColor_id())){
					option_values.add(itemImage.getColor_id());
					
					images.add(itemImage.getImage());
					
				}
			}
		}
		imageOption.setColor_id(option_values);
		imageOption.setImages(images);
		
		imageOption.setOptions(option_values);
		
		options.add(imageOption);
		}

		for (Option option : options) {
			col_with_images++;
			if (option.getStyle().equals("images")) {
				color_id = option.getColor_id().get(0);
				Log.d("color_id", color_id);
				colorOption = option;

				break;
			}
		}
		
		item.setOptions(options);
	}

	public void buildOptions() {
		LinearLayout llCol1 = (LinearLayout) findViewById(R.id.llViewProduct_options_col1);
		LinearLayout llCol2 = (LinearLayout) findViewById(R.id.llViewProduct_options_col2);
		LinearLayout llCol3 = (LinearLayout) findViewById(R.id.llViewProduct_options_col3);

		 tvOptionName1 = (TextView) findViewById(R.id.tvViewProduct_option_name1);
		tvOptionValue1 = (TextView) findViewById(R.id.tvViewProduct_option_value1);

		 tvOptionName2 = (TextView) findViewById(R.id.tvViewProduct_option_name2);
		tvOptionValue2 = (TextView) findViewById(R.id.tvViewProduct_option_value2);

		 tvOptionName3 = (TextView) findViewById(R.id.tvViewProduct_option_name3);
		tvOptionValue3 = (TextView) findViewById(R.id.tvViewProduct_option_value3);
		
		Log.d("number of options", String.valueOf(item.getOptions().size()));

		switch (item.getOptions().size()) {
		
		case 0:
			llCol1.setVisibility(LinearLayout.GONE);
			llCol2.setVisibility(LinearLayout.GONE);
			llCol3.setVisibility(LinearLayout.GONE);

			
			break;
		case 1:
			llCol1.setVisibility(LinearLayout.VISIBLE);
			llCol2.setVisibility(LinearLayout.GONE);
			llCol3.setVisibility(LinearLayout.GONE);

			tvOptionName1.setText(item.getOptions().get(0).getName());
			tvOptionValue1
					.setText(item.getOptions().get(0).getOptions().get(0));
			break;
		case 2:
			llCol1.setVisibility(LinearLayout.VISIBLE);
			llCol2.setVisibility(LinearLayout.VISIBLE);
			llCol3.setVisibility(LinearLayout.GONE);

			tvOptionName1.setText(item.getOptions().get(0).getName());
			tvOptionValue1
					.setText(item.getOptions().get(0).getOptions().get(0));

			tvOptionName2.setText(item.getOptions().get(1).getName());
			tvOptionValue2
					.setText(item.getOptions().get(1).getOptions().get(0));

			break;
		case 3:
			llCol1.setVisibility(LinearLayout.VISIBLE);
			llCol2.setVisibility(LinearLayout.VISIBLE);
			llCol3.setVisibility(LinearLayout.VISIBLE);

			tvOptionName1.setText(item.getOptions().get(0).getName());
			tvOptionValue1
					.setText(item.getOptions().get(0).getOptions().get(0));

			tvOptionName2.setText(item.getOptions().get(1).getName());
			tvOptionValue2
					.setText(item.getOptions().get(1).getOptions().get(0));

			tvOptionName3.setText(item.getOptions().get(2).getName());
			tvOptionValue3
					.setText(item.getOptions().get(2).getOptions().get(0));

			break;

		default:
			break;
		}

		llCol1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showPopUp(item.getOptions().get(0));
				option_num = 1;
			}
		});

		llCol2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				showPopUp(item.getOptions().get(1));
				option_num = 2;
			}
		});

		llCol3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showPopUp(item.getOptions().get(2));
				option_num = 3;
			}
		});

	}

	public void showPopUp(final Option option) {
		final Dialog dialog = new Dialog(SingleProductActivity.this,
				R.style.FullHeightDialog);
		dialog.setContentView(R.layout.view_product_color_popup);

		ListView lvColor = (ListView) dialog
				.findViewById(R.id.lvColorPopup_color);
		lvColor.setAdapter(new ColorItemsAdapter(this, option));

		lvColor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				switch (option_num) {
				case 1:

					tvOptionValue1.setText(option.getOptions().get(pos));
					break;
				case 2:
					tvOptionValue2.setText(option.getOptions().get(pos));
					break;
				case 3:
					tvOptionValue3.setText(option.getOptions().get(pos));
					break;

				default:
					break;
				}

				if (option.getStyle().equals("images") ) {
					color_id = option.getColor_id().get(pos);

					while (itemImages.size() != 0) {
						itemImages.remove(0);
					}

					for (ItemImage itemImage : item.getImages()) {
						if (itemImage.getColor_id().equals(color_id)) {
							itemImages.add(itemImage);
						}
					}
					colorThumbsAdapter.setSelectedItem(pos);
					colorThumbsAdapter.notifyDataSetChanged();
					mAdapter.notifyDataSetChanged();
					thumbAdapter.notifyDataSetChanged();
				}
				dialog.dismiss();
			}
		});

		dialog.setCancelable(true);
		dialog.show();
	}

	private class GiloAdapter extends PagerAdapter {

		Context context;
		ArrayList<ItemImage> images;

		public GiloAdapter(Context c, ArrayList<ItemImage> arrayList) {
			this.context = c;
			this.images = arrayList;
		}

		// This is the number of pages -- 5
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return images.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
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

			image.setScaleType(ScaleType.FIT_CENTER);
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// startActivity(new Intent(SingleProductActivity.this,
					// ShopActivity.class));
				}
			});

			
			 Picasso.with(context).load(Config.IMAGE_MEDIUM+ images.get(position).getImage()).placeholder(R.drawable.placeholder).into(image);
			 
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(SingleProductActivity.this,
							ZoomInActivity.class);
					intent.putExtra("item_id", id);
					intent.putExtra("pager_position", position);
					intent.putExtra("color_option_pos", color_option_pos);
					intent.putExtra("color_id", color_id);
					
					startActivity(intent);
				}
			});

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

	

	public class CommentsItemsAdapter extends BaseAdapter {

		Context context;
		Typeface font, font_light;
		ArrayList<Comment> comments;

		public CommentsItemsAdapter(Context c, ArrayList<Comment> comments) {
			this.context = c;
			this.comments = comments;
			font = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Roman.otf");
			font_light = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Lt.otf");
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return comments.size();
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
			View v = inflater
					.inflate(R.layout.single_comment_item, null, false);
			
			TextView tvName = (TextView) v.findViewById(R.id.tvSingle_Comment_Name);
			TextView tvDate = (TextView) v.findViewById(R.id.tvSingle_Comment_Date);
			TextView tvComment = (TextView) v.findViewById(R.id.tvSingle_Comment_comment);
			
			RatingBar rbRating = (RatingBar) v.findViewById(R.id.rbSingle_Comment_rating);
			
			
			tvName.setText(comments.get(position).getName());
			tvDate.setText(comments.get(position).getDate());
			tvComment.setText(comments.get(position).getComment());
			rbRating.setRating((float) comments.get(position).getRating());

			return v;
		}
	}

	public class ThumbNailsItemsAdapter extends BaseAdapter {

		Context context;
		Typeface font, font_light;
		ArrayList<ItemImage> itemImages;

		public ThumbNailsItemsAdapter(Context c, ArrayList<ItemImage> itemImages) {
			this.context = c;
			this.itemImages = itemImages;
			font = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Roman.otf");
			font_light = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Lt.otf");
		}

		private int selectedItem;

		public void setSelectedItem(int position) {
			selectedItem = position;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return itemImages.size();
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
			View v = inflater.inflate(R.layout.single_thumbnail_item, null,
					false);

			if (position == selectedItem) {
				v.setBackgroundResource(R.drawable.list_selector_thumbnail);
			}

			ImageView image = (ImageView) v
					.findViewById(R.id.ivSingleThumbNail_image);

//			String image_name = itemImages.get(position).getImage();
//			Uri path = Uri.parse("android.resource://"+ context.getResources().getString(R.string.package_name) + "/drawable/"
//					+ image_name);
//			image.setImageURI(path);
			Picasso.with(context).load(Config.IMAGE_MEDIUM+ itemImages.get(position).getImage()).placeholder(R.drawable.placeholder).into(image);

			return v;
		}
	}

	public class ColorItemsAdapter extends BaseAdapter {

		Context context;
		Typeface font, font_light;
		Option option;
		ArrayList<String> options = new ArrayList<String>();
		ArrayList<String> option_images = new ArrayList<String>();

		public ColorItemsAdapter(Context c, Option option) {
			this.context = c;
			this.option = option;
			options = option.getOptions();
			option_images = option.getImages();

			font = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Roman.otf");
			font_light = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Lt.otf");
		}

		public int getCount() {
			// TODO Auto-generated method stub

			return option.getOptions().size();
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
			TextView color_name;
			View v = inflater.inflate(R.layout.single_color_item, null, false);

			if (option.getStyle().equals("images")) {

				v = inflater.inflate(R.layout.single_color_item, null, false);
				ImageView image = (ImageView) v
						.findViewById(R.id.ivSingleColor_image);
				color_name = (TextView) v
						.findViewById(R.id.tvSingleColor_color_name);
				ImageView ivCheck = (ImageView) v
						.findViewById(R.id.ivSingleColor_check);

//				String image_name = option_images.get(position);
//				Uri path = Uri
//						.parse("android.resource://"+ context.getResources().getString(R.string.package_name) + "/drawable/"
//								+ image_name);
//				image.setImageURI(path);
				
				 Picasso.with(context).load(Config.IMAGE_MEDIUM+ option_images.get(position)).placeholder(R.drawable.placeholder).into(image);


				color_name.setText(options.get(position));

				if (option.getColor_id().get(position).equals(color_id))
					ivCheck.setVisibility(ImageView.VISIBLE);

			} else {
				v = inflater.inflate(R.layout.single_option_text_item, null,
						false);
				color_name = (TextView) v
						.findViewById(R.id.tvSingleColor_color_name);
				color_name.setText(options.get(position));
			}

			return v;
		}
	}

	public class ColorThumbsAdapter extends BaseAdapter {

		Context context;
		Typeface font, font_light;
		Option option;
		ArrayList<String> options = new ArrayList<String>();
		ArrayList<String> option_images = new ArrayList<String>();

		public ColorThumbsAdapter(Context c, Option option) {
			this.context = c;
			this.option = option;
			options = option.getOptions();
			option_images = option.getImages();

			font = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Roman.otf");
			font_light = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Lt.otf");
		}

		private int selectedItem;

		public void setSelectedItem(int position) {
			selectedItem = position;
		}

		public int getCount() {
			// TODO Auto-generated method stub

			return option.getOptions().size();
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

			View v = inflater.inflate(R.layout.single_thumbnail_color_item,
					null, false);

			ImageView image = (ImageView) v
					.findViewById(R.id.ivSingleThumbNail_image);

//			String image_name = option_images.get(position);
//			Log.d("image", option_images.get(position));
//			Uri path = Uri.parse("android.resource://"+ context.getResources().getString(R.string.package_name) + "/drawable/"
//					+ image_name);
//			image.setImageURI(path);
			
			Picasso.with(context).load(Config.IMAGE_MEDIUM+ option_images.get(position)).into(image);

			if (position == selectedItem) {
				image.setBackgroundResource(R.drawable.list_selector_thumbnail);
			}

			return v;
		}
	}
	
	public class MyTagHandler implements TagHandler{
		boolean first= true;
		String parent=null;
		int index=1;
		@Override
		public void handleTag(boolean opening, String tag, Editable output,
		        XMLReader xmlReader) {

		    if(tag.equals("ul")) parent="ul";
		    else if(tag.equals("ol")) parent="ol";
		    if(tag.equals("li")){
		        if(parent.equals("ul")){
		            if(first){
		                output.append("\n\t • ");
		                first= false;
		            }else{
		                first = true;
		            }
		        }
		        else{
		            if(first){
		                output.append("\n\t"+index+". ");
		                first= false;
		                index++;
		            }else{
		                first = true;
		            }
		        }   
		    }
		}
		}
	
	public class ReviewsGetter extends AsyncTask<Void, Void, Void>{
		
		/*
		ship_zone_id	ship_country	ship_country_id	bill_company	bill_firstname	bill_lastname	bill_email	bill_phone			
		bill_address1	bill_address2	bill_city	bill_zip	bill_zone				
		bill_zone_id	bill_country	bill_country_id		shipping_method		shipping_price	
		tax gift_card_discount	coupon_discount	cart_subtotal	cart_total	payment_description		referral 	shipping_notes				
									*/

		
		Context context;
		
		Boolean error = false;
		
		public ReviewsGetter(Context context){
			this.context = context;	
		}
		
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//JSONListener.onPreExecute();
			
			
		}
		

		/**
		 * 
		 * */
		protected Void doInBackground(Void... args) {
			// getting JSON string from URL
			
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(); 
			
			
			JSONArray json = new JSONParser().makeHttpRequestForArray(Config.COMMENTS_URL + id, "POST", params);
			//Log.d("json order", json.toString());
			
			if(json == null){
				error = true;
			}else{
				for(int i = 0; i < json.length(); i++){
					try {
						
//	id	product_id	comment	rating	comfort	style	customer_name	date
						
						JSONObject commentObject = json.getJSONObject(i);
						Comment comment = new Comment();
						comment.setName(commentObject.getString("customer_name"));
						comment.setId(commentObject.getInt("id"));
						comment.setProduct_id(commentObject.getInt("product_id"));
						comment.setComment(commentObject.getString("comment"));
						comment.setRating(commentObject.getDouble("rating"));
						comment.setStyle(commentObject.getDouble("style"));
						comment.setDate(commentObject.getString("date"));
						
						rating += comment.getRating();
						
						comments.add(comment);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			
			
			if(!error){
				pbCommentsLoading.setVisibility(ProgressBar.GONE);
				lvComments.setAdapter(new CommentsItemsAdapter(SingleProductActivity.this, comments));
			
            }else{
            	Crouton.makeText(SingleProductActivity.this,
						"Unable to connect. Ensure that you have mobile data",
						style_error).show();
            }
			
			
			
		}
	}
	
	

}
