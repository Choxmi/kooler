package com.gilo.soko;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gilo.soko.models.Order;
import com.gilo.soko.models.OrderItem;
import com.gilo.soko.utils.JSONParser;
import com.gilo.wooApi.soko.R;

public class OrderActivity extends Activity{
	
	
	ListView lvOrderList;
	ArrayList<Order> orders = new ArrayList<Order>();
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list);
		
		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTextColor(Color.parseColor("#4E6A9F"));
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		//getActionBar().setIcon(R.drawable.ic_ab_logo);
		getActionBar().setTitle("Orders");
		
		lvOrderList = (ListView) findViewById(R.id.lvOrderList);
		
		
		lvOrderList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent  = new Intent(OrderActivity.this, SingleOrderActivity.class);
				intent.putExtra("order", orders.get(pos));
				
				startActivity(intent);
			}
		});
		
		new OrdersGetter().execute();		
	}
	
	public class OrderItemAdapter extends BaseAdapter {

		Context context;
		Typeface font, font_light;
		
		ArrayList<String> options = new ArrayList<String>();
		ArrayList<String> option_images = new ArrayList<String>();
		ArrayList<Order> orders;

		public OrderItemAdapter(Context c, ArrayList<Order> orders) {
			this.context = c;
			this.orders = orders;

			font = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Roman.otf");
			font_light = Typeface.createFromAsset(context.getAssets(),
					"fonts/HelveticaNeueLTPro-Lt.otf");
		}

		public int getCount() {
			// TODO Auto-generated method stub

			return orders.size();
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

			View v = inflater.inflate(R.layout.single_order_item,
					null, false);
			
			Order order = orders.get(position);
			
			TextView productName = (TextView) v.findViewById(R.id.tvSingleOrderItem_products_name);
			TextView email = (TextView) v.findViewById(R.id.tvSingleOrderItem_email);
			TextView phone = (TextView) v.findViewById(R.id.tvSingleOrderItem_phone);
			TextView quantity = (TextView) v.findViewById(R.id.tvSingleOrderItem_quantity);
			TextView status = (TextView) v.findViewById(R.id.tvSingleOrderItem_status);
			TextView total = (TextView) v.findViewById(R.id.tvSingleOrderItem_total);
			
			productName.setText("#" + order.getOrder_number());
			total.setText("$" + String.format("%.2f",  order.getTotal()));
			email.setText(order.getEmail());
			phone.setText(order.getPhone());
			
			int totals = 0;
			for(OrderItem item : order.getOrderItems()){
				totals += item.getQuantity();
			}
			
			quantity.setText(totals + " items");
			
			return v;
		}
	}
	
	public class OrdersGetter extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			dialog =  new ProgressDialog(OrderActivity.this);
			dialog.setMessage("Loading orders");
			dialog.setCancelable(false);
			dialog.show();
			
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			JSONParser jsonParser = new JSONParser();
			
			orders = new ArrayList<Order>();
			JSONArray ordersArray = jsonParser
					.makeHttpRequestForArray("http://kimutai.org/wooApi/api/orders.php", "GET",
							new ArrayList<NameValuePair>());
			
			if(ordersArray == null){
				return null;
			}
			
			Log.d("orders", ordersArray.toString() );
			
			try {

				for (int i = 0; i < ordersArray.length(); i++) {
					JSONObject orderObject = ordersArray.getJSONObject(i);
					Order order = new Order();
					
//					int id, order_number;
//					int customer_id;
//					String status, ordered_on, shipped_on;
//					double tax, total, subtotal, gift_card_discount, coupon_discount, shipping;
//					String shipping_notes, shipping_method, notes, payment_info, referral, company, firstname, 
//					
					
					order.setId(orderObject.getInt("id"));
					order.setCustomer_id(orderObject.getInt("customer_id"));
					order.setOrder_number(orderObject.getInt("order_number"));
					order.setStatus(orderObject.getString("status"));
					order.setOrdered_on(orderObject.getString("ordered_on"));
					order.setShipped_on(orderObject.getString("shipped_on"));
					order.setTax(orderObject.getDouble("tax"));
					order.setTotal(orderObject.getDouble("total"));
					order.setSubtotal(orderObject.getDouble("subtotal"));
					order.setGift_card_discount(orderObject.getDouble("gift_card_discount"));
					order.setCoupon_discount(orderObject.getDouble("coupon_discount"));
					order.setShipping(orderObject.getDouble("shipping"));
					order.setShipping_notes(orderObject.getString("shipping_notes"));
					order.setShipping_method(orderObject.getString("shipping_method"));
					order.setNotes(orderObject.getString("notes"));
					order.setPayment_info(orderObject.getString("payment_info"));
					order.setReferral(orderObject.getString("referral"));
					order.setCompany(orderObject.getString("company"));
					order.setFirstname(orderObject.getString("firstname"));
					
					order.setLastname(orderObject.getString("lastname"));
					order.setPhone(orderObject.getString("phone"));
					order.setEmail(orderObject.getString("email"));
					order.setShip_company(orderObject.getString("ship_company"));
					order.setShip_firstname(orderObject.getString("ship_firstname"));
					order.setShip_lastname(orderObject.getString("ship_lastname"));
					order.setShip_email(orderObject.getString("ship_email"));
					order.setShip_phone(orderObject.getString("ship_phone"));
					
					order.setShip_address1(orderObject.getString("ship_address1"));
					order.setShip_address2(orderObject.getString("ship_address2"));
					order.setShip_city(orderObject.getString("ship_city"));
					order.setShip_zip(orderObject.getString("ship_zip"));
					order.setShip_zone(orderObject.getString("ship_zone"));
					order.setShip_zone_id(orderObject.getString("ship_zone_id"));
					order.setShip_country(orderObject.getString("ship_country"));
					
					order.setShip_country_code(orderObject.getString("ship_country_code"));
					order.setShip_country_id(orderObject.getString("ship_country_id"));
					order.setBill_company(orderObject.getString("bill_company"));
					order.setBill_firstname(orderObject.getString("bill_firstname"));
					order.setBill_lastname(orderObject.getString("bill_lastname"));
					order.setBill_email(orderObject.getString("bill_email"));
					order.setBill_phone(orderObject.getString("bill_phone"));
					
					order.setBill_address1(orderObject.getString("bill_address1"));
					order.setBill_address2(orderObject.getString("bill_address2"));
					order.setBill_city(orderObject.getString("bill_city"));
					order.setBill_zip(orderObject.getString("bill_zip"));
					order.setBill_zone(orderObject.getString("bill_zone"));
					order.setBill_zone_id(orderObject.getString("bill_zone_id"));
					order.setBill_country(orderObject.getString("bill_country"));
					
					order.setBill_country_code(orderObject.getString("bill_country_code"));
					order.setBill_country_id(orderObject.getString("bill_country_id"));
					
//					lastname, phone, email, ship_company, ship_firstname, ship_lastname, ship_email, ship_phone, 
//					ship_address1, ship_address2, ship_city, ship_zip, ship_zone, ship_zone_id, ship_country, 
//					ship_country_code, ship_country_id, 
					//bill_company,bill_firstname, bill_lastname, 
//					bill_email,bill_phone, bill_address1, bill_address2, bill_city, bill_zip, bill_zone, bill_zone_id, bill_country,
					//bill_country_code, bill_country_id;
//					
					JSONArray productsArray = orderObject.getJSONArray("products");
					ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
					
					for(int j=0; j< productsArray.length(); j++){
						JSONObject productObject = productsArray.getJSONObject(j);
						OrderItem orderItem = new OrderItem();
						
						orderItem.setId(productObject.getInt("id"));
						orderItem.setOrder_id(productObject.getInt("order_id"));
						orderItem.setProduct_id(productObject.getInt("product_id"));
						orderItem.setQuantity(productObject.getInt("quantity"));
						orderItem.setOption(productObject.getString("attr"));
						orderItems.add(orderItem);
						
					}
					
					order.setOrderItems(orderItems);
					
					

					orders.add(order);
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
			dialog.dismiss();
			
			lvOrderList.setAdapter(new OrderItemAdapter(OrderActivity.this, orders));

			super.onPostExecute(result);
		}

		
		
		
	}
}
