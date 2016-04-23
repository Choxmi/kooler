package com.gilo.soko;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.gilo.soko.config.Config;
import com.gilo.soko.models.CartItem;
import com.gilo.soko.models.Item;
import com.gilo.wooApi.soko.R;
import com.squareup.picasso.Picasso;

public class PurchasedItemsActivity extends ActionBarActivity {

	ListView lvPurchasedItems;
	PurchasedItemsAdapter purchasedItemsAdapter;
	ArrayList<CartItem> cartItems;
	Button bContinue;
	
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.purchased_items);
		
		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTextColor(Color.parseColor("#4E6A9F"));
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		//getActionBar().setIcon(R.drawable.ic_ab_logo);
		getActionBar().setTitle("My Purchases");
		
		lvPurchasedItems = (ListView) findViewById(R.id.lvPurchasedItems_list);
		
	
		getCartItems();
		
		Button bPurchaseSomeMore = (Button) findViewById(R.id.bShopMore);
		bPurchaseSomeMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(PurchasedItemsActivity.this, NavigationDrawer.class));
			}
		});
		
		
		
	}
	
	public void getCartItems(){
		cartItems = new ArrayList<CartItem>();
		
		NoSQL.with(this).using(CartItem.class)
	    .bucketId("purchased")
	    .retrieve(new RetrievalCallback<CartItem>() {
		@Override
		public void retrievedResults(List<NoSQLEntity<CartItem>> entities) {
			// TODO Auto-generated method stub
			if(entities.size() > 0){
				
				
				for(NoSQLEntity<CartItem> entity : entities){
					cartItems.add(entity.getData());
				}
				
				purchasedItemsAdapter = new PurchasedItemsAdapter(PurchasedItemsActivity.this, cartItems);
				lvPurchasedItems.setAdapter(purchasedItemsAdapter);
				
				
			}
		}   
	    });
		
		
		
	}

	public class PurchasedItemsAdapter extends BaseAdapter {

		Context context;
		Typeface font, font_light;
		ArrayList<CartItem> cartItems;

		public PurchasedItemsAdapter(Context c, ArrayList<CartItem> cartItems) {
			this.context = c;
			this.cartItems = cartItems;
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
			return cartItems.size(); //cartItems.size();
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
					.inflate(R.layout.single_purchased_items, null, false);
			
			TextView tvName = (TextView) v.findViewById(R.id.tvSinglePurchased_name);
			TextView tvBrand = (TextView) v.findViewById(R.id.tvSinglePurchased_brand);
			TextView tvStatus = (TextView) v.findViewById(R.id.tvSinglePurchased_status);
			RatingBar rbRating = (RatingBar) v.findViewById(R.id.rbSinglePurchased_rating);
			ImageView image = (ImageView) v.findViewById(R.id.ivSinglePurchased_thumbnail);
			
			Item item = cartItems.get(position).getItem();
			
			tvName.setText(item.getName());
			tvBrand.setText(item.getShop());
			
			String image_name = item.getFeatured_image();
			Picasso.with(context).load(Config.IMAGE_MEDIUM + image_name).into(image);
			
			rbRating.setRating(item.getReview());
			
			return v;
		}
	}

	
	
	
}
