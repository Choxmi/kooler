package com.gilo.soko;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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



public class NavigationDrawer extends ActionBarActivity {

	

	private DrawerLayout mDrawerLayout;
	ListView cartList;
	FrameLayout flLayout;
	CartItemsAdapter adapter;
	ArrayList<CartItem> cartItems;
	Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_navigation);
		
		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTextColor(Color.parseColor("#4E6A9F"));
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		//getActionBar().setIcon(R.drawable.ic_ab_logo);
		getActionBar().setTitle(getResources().getString(R.string.app_name));

		cartList = (ListView) findViewById(R.id.lvMainNav_lvCart);
		
		
		flLayout = (FrameLayout) findViewById(R.id.flCartSide_layout);
		
		int width = getResources().getDisplayMetrics().widthPixels - 50;
	    DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) flLayout.getLayoutParams();
	    params.width = width;
	   
	    
		
	    
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		flLayout.setLayoutParams(params);
		
		
		
		Button bCheckOut = (Button) findViewById(R.id.bCartSideBar_checkout);
		bCheckOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(NavigationDrawer.this, CheckOutActivity.class));
				
			}
		});
		
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(MainActivity.class);

		}

		
		
	}

	public void getCartItems(){
		cartItems = new ArrayList<CartItem>();
		NoSQL.with(this).using(CartItem.class)
	    .bucketId("cart")
	    .retrieve(new RetrievalCallback<CartItem>() {
	    

		@Override
		public void retrievedResults(List<NoSQLEntity<CartItem>> entities) {
			// TODO Auto-generated method stub
			Log.d("entitiies is size", String.valueOf(entities.size()));
			
			if(entities.size() > 0){
				CartItem cartItem = entities.get(0).getData(); // always check length of a list first...
				
				
				
				for(NoSQLEntity<CartItem> entity : entities){
					cartItems.add(entity.getData());
				}
			}
			
			if(menu != null){
			
			MenuItem cartMenuItem = (MenuItem) 	menu.findItem(R.id.mnCart);
			if(cartItems.size() == 0){
				cartMenuItem.setIcon(R.drawable.cart0);
			}
			if(cartItems.size() == 1){
				cartMenuItem.setIcon(R.drawable.cart1);
			}
			if(cartItems.size() == 2){
				cartMenuItem.setIcon(R.drawable.cart2);
			}
			if(cartItems.size() == 3){
				cartMenuItem.setIcon(R.drawable.cart3);
			}
			if(cartItems.size() == 4){
				cartMenuItem.setIcon(R.drawable.cart4);
			}
			if(cartItems.size() == 5){
				cartMenuItem.setIcon(R.drawable.cart5);
			}
			if(cartItems.size() > 5){
				cartMenuItem.setIcon(R.drawable.cart5plus);
			}
			
			if(cartItems.size() > 10){
				cartMenuItem.setIcon(R.drawable.cart10plus);
			}
			
			}
			adapter = new CartItemsAdapter(NavigationDrawer.this, cartItems);
			cartList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			
			cartList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					// TODO Auto-generated method stub
					if(pos != cartItems.size()){
					Intent intent = new Intent(NavigationDrawer.this, SingleProductDrawerActivity.class);
					intent.putExtra("item_id", cartItems.get(pos).getItem().getId());
					startActivity(intent);
					}
				}
			});
			
			
			
		}
		
		
		
	    });
		
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_shopping, menu);
		
		this.menu = menu;
		// Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.mnSearch)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
		
        //RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.rlCartIcon).getActionView();
		//mCounter = (TextView) badgeLayout.findViewById(R.id.counter);
        getCartItems();
        
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(flLayout);
		
		
		
		if(drawerOpen){
			mDrawerLayout.closeDrawer(Gravity.RIGHT);
		}
		
		if(item.getItemId() == R.id.mnCart){
			if(!drawerOpen){
				getCartItems();
				mDrawerLayout.openDrawer(Gravity.RIGHT);
			}
		}
		
		if(item.getItemId() == R.id.mnAbout){
			startActivity(new Intent(getBaseContext(), AboutActivity.class));
		}
		
		if(item.getItemId() == R.id.mnContactUs){
			startActivity(new Intent(getBaseContext(), ContactUsActivity.class));
		}
		
		if(item.getItemId() == R.id.mnPurchaseItems){
			startActivity(new Intent(getBaseContext(), PurchasedItemsActivity.class));
		}
		
		if(item.getItemId() == R.id.mnShippingInfo){
			startActivity(new Intent(getBaseContext(), ShippingInformationActivity.class));
		}
		
		if(item.getItemId() == R.id.mnFavoriteItems){
			startActivity(new Intent(getBaseContext(), HeartedActivity.class));
		}
		
		if(item.getItemId() == R.id.mnLogin){
			startActivity(new Intent(getBaseContext(), LoginActivity.class));
		}
		
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(flLayout);

		MenuItem searchViewMenuItem = menu.findItem(R.id.mnSearch);    
		SearchView mSearchView = (SearchView) searchViewMenuItem.getActionView();
	    int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
	    ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
	    v.setImageResource(R.drawable.search); 
		
		return super.onPrepareOptionsMenu(menu);
	}

	

	private void displayView(Class<? extends Activity> activity) {
		// update the main content by replacing fragments
		FragmentController fragmentController = new FragmentController();
		Fragment fragment = null;
		fragmentController = new FragmentController();
		fragmentController.setClass(activity);
		fragment = fragmentController;
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).commit();

		
	}
	
	public class CartItemsAdapter extends BaseAdapter {

		Context context;
		Typeface font, font_light;
		ArrayList<CartItem> cartItems;

		public CartItemsAdapter(Context c, ArrayList<CartItem> cartItems) {
			this.context = c;
			this.cartItems = cartItems;
			font = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Roman.otf");
			font_light = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Lt.otf");
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return cartItems.size() + 1;
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
			
			View v = inflater
					.inflate(R.layout.single_cart_item, null, false);

			if(position == cartItems.size() ){
				v = inflater.inflate(R.layout.cart, null, false);
				
				TextView tvSubTotal = (TextView) v.findViewById(R.id.tvCart_subtotal);
				TextView tvTotal = (TextView) v.findViewById(R.id.tvCart_totals);
				TextView tvShipping = (TextView) v.findViewById(R.id.tvCart_shipping);
				
				float totals = 0;
				for(CartItem cartItem : cartItems){
					totals += cartItem.getItem().getPrice() * cartItem.getQty();
				}
				
				tvSubTotal.setText(String.format("%.2f", totals));
				if(cartItems.size() > 0){
					tvTotal.setText(String.format("%.2f", totals + Config.FLATRATE_SHIPPING));
					tvShipping.setText(String.format("%.2f", Config.FLATRATE_SHIPPING));
				}else{
					tvTotal.setText(String.format("%.2f", totals ));
				}
				return v;
			}
			
			Item item = cartItems.get(position).getItem();
			
			TextView tvName = (TextView) v.findViewById(R.id.tvCartItem_name);
			TextView tvOptions = (TextView) v.findViewById(R.id.tvCartItem_Options);
			TextView tvPrice = (TextView) v.findViewById(R.id.tvCartItem_Price);
			TextView tvQty = (TextView) v.findViewById(R.id.tvCartItem_Qty);
			ImageView ivThumb = (ImageView) v.findViewById(R.id.ivCartItem_thumbnail);
			
			
			
			String image_name = item.getFeatured_image();
			
			Picasso.with(context).load(Config.IMAGE_THUMBNAIL + item.getFeatured_image()).into(ivThumb);
			
			tvName.setText(item.getName().trim());
			tvOptions.setText(cartItems.get(position).getOptions());
			tvPrice.setText(context.getResources().getString(R.string.currency) + String.format("%.2f", item.getPrice()));
			tvQty.setText(String.valueOf(cartItems.get(position).getQty()));
			
			LinearLayout llQty = (LinearLayout) v.findViewById(R.id.llCartItem_Qty);
			llQty.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					displayQtyPopUp(position);
				}
			});
			
			return v;
		}
	}

	public void displayQtyPopUp(final int pos){
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	                NavigationDrawer.this);
	        
	        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(NavigationDrawer.this, android.R.layout.select_dialog_singlechoice);
	        
	        for(int i = 1; i <= 20; i++){
	        	arrayAdapter.add(String.valueOf(i) + " Items");
	        }
	        
	        
	        builderSingle.setAdapter(arrayAdapter,
	                new DialogInterface.OnClickListener() {
	        	Item item;
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	CartItem cartItem = cartItems.get(pos);
	                    	cartItem.setQty(which + 1);
	                    	
	                    	NoSQLEntity<CartItem> entity = new NoSQLEntity<CartItem>(
	    							"cart", String.valueOf(cartItem.getItem().getId()));
	    					entity.setData(cartItem);
	    					NoSQL.with(NavigationDrawer.this).using(CartItem.class)
	    							.save(entity);
	    					
	    					getCartItems();
	                    }
	                    	
	                });
	        
	        builderSingle.show();
	}
	

}