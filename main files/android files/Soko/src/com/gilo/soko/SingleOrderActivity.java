package com.gilo.soko;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gilo.soko.XMLParsers.ItemsInitiator;
import com.gilo.soko.config.Config;
import com.gilo.soko.models.Item;
import com.gilo.soko.models.Order;
import com.gilo.soko.models.OrderItem;
import com.gilo.wooApi.soko.R;
import com.squareup.picasso.Picasso;

public class SingleOrderActivity extends ActionBarActivity {
	
	
	Order order;
	TextView tvShippingCost, tvSubtotal, tvTotal, tvShippingAddress, tvPaymentMethod, tvShippingMethod;
	
	LinearLayout llProduct;
	
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.single_order);
		
		order = (Order) getIntent().getSerializableExtra("order");
		
		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTextColor(Color.parseColor("#4E6A9F"));
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		//getActionBar().setIcon(R.drawable.ic_ab_logo);
		getActionBar().setTitle("Order #" + order.getOrder_number());
		
		tvShippingCost = (TextView) findViewById(R.id.tvSingleOrder_ShippingCost);
		tvSubtotal = (TextView) findViewById(R.id.tvSingleOrder_subtotal);
		tvTotal = (TextView) findViewById(R.id.tvSingleOrder_totals);
		tvShippingAddress = (TextView) findViewById(R.id.tvSingleOrder_shipping);
		tvShippingMethod = (TextView) findViewById(R.id.tvSingleOrder_ShippingMethod);
		tvPaymentMethod = (TextView) findViewById(R.id.tvSingleOrder_PaymentMethod);
		
		llProduct = (LinearLayout) findViewById(R.id.llSingleOrder_product_layout);
		
		tvShippingCost.setText(String.format("%.2f",  order.getShipping()));
		tvSubtotal.setText(String.format("%.2f", order.getSubtotal()));
		tvTotal.setText(String.format("%.2f", order.getTotal()));
		
		tvPaymentMethod.setText(""+ order.getPayment_info());
		
		String address = order.getFirstname() + " " + order.getLastname() +", \n" 
						+ order.getShip_address1();
		
		if( order.getShip_address2().trim().length() != 0)
			address += ", " + order.getShip_address2();
		
		address += " - " + order.getShip_zip()+ ",\n" 
					+ order.getShip_city() + ", " + order.getShip_zone() + ", " + order.getShip_country() + "."; 
		
		tvShippingAddress.setText(address);
		tvShippingMethod.setText(order.getShipping_method());
		
		for(OrderItem orderItem : order.getOrderItems()){
			LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = vi.inflate(R.layout.single_order_page, null);
			
			ImageView ivThumbnail = (ImageView) v.findViewById(R.id.ivSingleProduct_thumbnail);
			
			TextView tvName = (TextView) v.findViewById(R.id.tvSingleProduct_name);
			TextView tvQty= (TextView) v.findViewById(R.id.tvSingleProduct_qty);
			TextView tvOptions = (TextView) v.findViewById(R.id.tvSingleProduct_options);
			TextView tvStatus = (TextView) v.findViewById(R.id.tvSingleProduct_status);
			
			Item item = new ItemsInitiator(this).getItem(orderItem.getProduct_id());
			tvName.setText(item.getName().trim());
			tvQty.setText(orderItem.getQuantity() + " item (s)");
			tvStatus.setText(order.getStatus());
			tvOptions.setText(orderItem.getOption().trim());
			
			Picasso.with(this)
			.load(Config.IMAGE_MEDIUM+ item.getFeatured_image()).placeholder(R.drawable.placeholder)
			.into(ivThumbnail);
			
			
			
			
			
			llProduct.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		}
			
	}

}
