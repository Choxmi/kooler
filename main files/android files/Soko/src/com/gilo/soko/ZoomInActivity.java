package com.gilo.soko;

import java.util.ArrayList;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import com.gilo.soko.XMLParsers.ItemsInitiator;
import com.gilo.soko.config.Config;
import com.gilo.soko.models.Item;
import com.gilo.soko.models.ItemImage;
import com.gilo.soko.models.Option;
import com.gilo.soko.views.HorizontalListView;
import com.gilo.wooApi.soko.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ZoomInActivity extends ActionBarActivity {

	Boolean infoVisible = false, commentVisible = false;
	
	Option colorOption = null;
	
	int col_with_images = 0;
	
	ThumbNailsItemsAdapter thumbAdapter;
	ColorThumbsAdapter colorThumbsAdapter;
	
	String color_id="default";
	
	int pager_position = 0;
	int color_option_pos = 0;
	int id = 0;
	int option_num = 0;
	Item item = new Item();
	ArrayList<ItemImage> itemImages = new ArrayList<ItemImage>();
	GiloAdapter mAdapter;

	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.zoom_in);

		Intent intent = getIntent();
		id = intent.getIntExtra("item_id", 1);
		color_option_pos = intent.getIntExtra("color_option_pos", 0);
		pager_position = intent.getIntExtra("pager_position", 1);
		
		
		
		color_id = intent.getStringExtra("color_id");
		item = new ItemsInitiator(this).getItem(id);

		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTextColor(Color.parseColor("#4E6A9F"));
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		//getActionBar().setIcon(R.drawable.ic_ab_logo);
		getActionBar().setTitle("Zoom In");
		
		getColorId();
		color_id = intent.getStringExtra("color_id");
		
		for(ItemImage itemImage : item.getImages()){
			//if(itemImage.getColor_id().equals(color_id)){
				itemImages.add(itemImage);
			//}
		}
		
		mAdapter = new GiloAdapter(this, itemImages);

		// instantiate the Views
		final ViewPager mPager = (ViewPager) findViewById(R.id.pager);
		
		
		
		
		
		
		ListView lvThumbs = (ListView) findViewById(R.id.lvlvView_product_thumbnails);
		thumbAdapter = new ThumbNailsItemsAdapter(this,itemImages);
		thumbAdapter.setSelectedItem(0);
		lvThumbs.setAdapter(thumbAdapter);
		lvThumbs.setSelection(0);
		
		final HorizontalListView lvColorOptions = (HorizontalListView) findViewById(R.id.lvView_product_color_options);
		if(colorOption != null){
			colorThumbsAdapter = new ColorThumbsAdapter(this, colorOption);
			colorThumbsAdapter.setSelectedItem(color_option_pos);
			lvColorOptions.setAdapter(colorThumbsAdapter);
		}else{
			lvColorOptions.setVisibility(ListView.GONE);
		}
		
		lvColorOptions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				colorThumbsAdapter.setSelectedItem(pos);
				colorThumbsAdapter.notifyDataSetChanged();
				
				color_id = colorOption.getColor_id().get(pos);
				
				while(itemImages.size() != 0 ){
					itemImages.remove(0);
				}
				
				for(ItemImage itemImage : item.getImages()){
					if(itemImage.getColor_id().equals(color_id)){
						itemImages.add(itemImage);
					}
				}
				
				mAdapter.notifyDataSetChanged();
				thumbAdapter.notifyDataSetChanged();
				
				
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

		mPager.setCurrentItem(pager_position, true);
		
		
	}
	
	public void getColorId(){
		
		ArrayList<Option> options = item.getOptions();
		//Collections.reverse(options);
		
		for(Option option : options){
			col_with_images++;
			if(option.getStyle().equals("images")){
				color_id = option.getColor_id().get(0);
				Log.d("color_id", color_id);
				colorOption = option;
				
				break;
			}
		}
		
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
		public Object instantiateItem(View pager, int position) {
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.intro_single_slide, null, false);
			final ImageView image = (ImageView) v.findViewById(R.id.ivImageSlide);

			image.setScaleType(ScaleType.FIT_CENTER);
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// startActivity(new Intent(SingleProductActivity.this,
					// ShopActivity.class));
				}
			});

			Callback imageLoadedCallback = new Callback() {

			    @Override
			    public void onSuccess() {
			    	PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);
			    }

			    @Override
			    public void onError() {
			        // TODO Auto-generated method stub

			    }
			};
			
			
			Picasso.with(context).load(Config.IMAGE_FULL+ images.get(position).getImage()).placeholder(R.drawable.placeholder).into(image, imageLoadedCallback);
			
			 
			 
			

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
			View v = inflater
					.inflate(R.layout.single_thumbnail_item, null, false);
			
			if(position == selectedItem){
				v.setBackgroundResource(R.drawable.list_selector_thumbnail);
			}
			
			ImageView image = (ImageView) v.findViewById(R.id.ivSingleThumbNail_image);
			
			Picasso.with(context).load(Config.IMAGE_FULL + itemImages.get(position).getImage()).placeholder(R.drawable.placeholder).into(image);
			
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
			
			View v = inflater.inflate(R.layout.single_thumbnail_color_item, null, false);
			
			
			

				ImageView image = (ImageView) v
						.findViewById(R.id.ivSingleThumbNail_image);
				
				Picasso.with(context).load(Config.IMAGE_FULL+ option_images.get(position)).placeholder(R.drawable.placeholder).into(image);
				

				if(position == selectedItem){
					image.setBackgroundResource(R.drawable.list_selector_thumbnail);
				}
				
			return v;
		}
	}
	
}
