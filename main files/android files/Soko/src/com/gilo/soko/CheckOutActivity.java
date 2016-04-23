package com.gilo.soko;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.TextView;
import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.gilo.soko.config.Config;
import com.gilo.soko.models.CartItem;
import com.gilo.soko.models.Item;
import com.gilo.soko.models.Place;
import com.gilo.soko.utils.JSONParser;
import com.gilo.wooApi.soko.R;
import com.google.gson.JsonObject;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class CheckOutActivity extends ActionBarActivity{

	ArrayList<CartItem> cartItems;
	TextView tvShipping, tvSubTotal, tvTotal, tvShippingCost;
	 ImageView ivEditShipping, ivViewItems;
	 
	 FrameLayout flPaypal, flCOD, flPick;
	 ImageView ivPaypalCheck, ivCODCheck, ivPickCheck;
	 TextView tvPaymentInfo;
	 
	 Button bCancel;
	 ListView lvCartItems;
	 float total= 0;
	 Dialog dialog;
	 Boolean dialog_open = false;
	 Place place;
	 
	 CartItemsAdapter cartItemAdapter;
	
	 private static final String TAG = "paymentExample";
	    
	
	    Style style, style_error;
	    
	protected void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.payment_info);
		
		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTextColor(Color.parseColor("#4E6A9F"));
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		//getActionBar().setIcon(R.drawable.ic_ab_logo);
		getActionBar().setTitle("Checkout");
		
		// Define configuration options
				Configuration croutonConfiguration = new Configuration.Builder()
						.setDuration(2500).build();
		// Define custom styles for crouton
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
		
		 tvSubTotal = (TextView) findViewById(R.id.tvPaymentInfo_subtotal);
		 tvShippingCost = (TextView) findViewById(R.id.tvPaymentInfo_shippingCost);
	     tvTotal = (TextView) findViewById(R.id.tvPaymentInfo_totals);
	     ivViewItems = (ImageView) findViewById(R.id.ivPaymentInfo_ViewItems);
	     
	     flCOD = (FrameLayout) findViewById(R.id.flPaymentInfo_COD);
	     flPaypal = (FrameLayout) findViewById(R.id.flPaymentInfo_Paypal);
	     flPick = (FrameLayout) findViewById(R.id.flPaymentInfo_Pick);
	     
	     ivCODCheck = (ImageView) findViewById(R.id.ivPaymentInfo_CODCheck);
	     ivPaypalCheck = (ImageView) findViewById(R.id.ivPaymentInfo_PaypalCheck);
	     ivPickCheck = (ImageView) findViewById(R.id.ivPaymentInfo_PickCheck);
	     
	     tvPaymentInfo = (TextView) findViewById(R.id.tvPaymentInfo_PaymentInfo);
	     
	     flCOD.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(ivCODCheck.getVisibility() == ImageView.INVISIBLE){
					ivCODCheck.setVisibility(ImageView.VISIBLE);
					ivPaypalCheck.setVisibility(ImageView.INVISIBLE);
					ivPickCheck.setVisibility(ImageView.INVISIBLE);
					
					tvPaymentInfo.setText(getString(R.string.payment_details_cod));
				}
			}
		});
	     
	     flPaypal.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(ivPaypalCheck.getVisibility() == ImageView.INVISIBLE){
						ivCODCheck.setVisibility(ImageView.INVISIBLE);
						ivPaypalCheck.setVisibility(ImageView.VISIBLE);
						ivPickCheck.setVisibility(ImageView.INVISIBLE);
						
						tvPaymentInfo.setText(getString(R.string.payment_details_paypal));
					}
				}
			});
	     
	     flPick.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(ivPickCheck.getVisibility() == ImageView.INVISIBLE){
						ivCODCheck.setVisibility(ImageView.INVISIBLE);
						ivPaypalCheck.setVisibility(ImageView.INVISIBLE);
						ivPickCheck.setVisibility(ImageView.VISIBLE);
						
						tvPaymentInfo.setText(getString(R.string.payment_details_pick));
					}
				}
			});
	     
	     if(!Config.PAYMENT_COD){
	    	flCOD.setVisibility(FrameLayout.GONE); 
	     }
	     
	     if(!Config.PAYMENT_PAYPAL){
		    	flPaypal.setVisibility(FrameLayout.GONE); 
		 }
	     
	     if(!Config.PAYMENT_PICK_FROM_SHOP){
		    	flPick.setVisibility(FrameLayout.GONE); 
		 }
	        
		getCartItems();
		
		Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, Config.config);
        startService(intent);
        Button bOrder = (Button) findViewById(R.id.bPaymentInfo_Order);
        bOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(cartItems.size() > 0){
					
				 if(place == null){
					Crouton.makeText(
							CheckOutActivity.this,
							"Oops, Please provide the shipping address",
							style_error).show();
				 }else{
					 
					 
					 if(ivCODCheck.getVisibility() == ImageView.VISIBLE){
						 new MakeOrder(CheckOutActivity.this).execute();
					 }else if(ivPaypalCheck.getVisibility() == ImageView.VISIBLE){
						 onBuyPressed();
					 }else if(ivPickCheck.getVisibility() == ImageView.VISIBLE){
						 new MakeOrder(CheckOutActivity.this).execute();
					 }else{
						 Crouton.makeText(
									CheckOutActivity.this,
									"Oops, Please pick a payment method first.",
									style_error).show();
					 }
					 
				 }
				}else{
					Crouton.makeText(
							CheckOutActivity.this,
							"Oops, seems like you have not added any item to your cart",
							style_error).show();
				}
			}
		});
        
        ivEditShipping = (ImageView) findViewById(R.id.ivPaymentInfo_editShipping);
        ivEditShipping.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CheckOutActivity.this, ShippingInformationActivity.class);
				startActivityForResult(intent, Config.REQUEST_CODE_SET_PLACE);
				
			}
		});
        
        tvShipping = (TextView) findViewById(R.id.tvPaymentInfo_shipping);
        NoSQL.with(this).using(Place.class).bucketId("shipping")
		.retrieve(new RetrievalCallback<Place>() {

			@Override
			public void retrievedResults(
					List<NoSQLEntity<Place>> entities) {
				// TODO Auto-generated method stub
				if (entities.size() > 0) {
					place = entities.get(0).getData(); 
					String shipping = "";
					shipping += place.getFirstname() + "  " + place.getLastname() + ", \n";
					shipping += "P.O. Box " + place.getAddress() + " - " + place.getZip() + ", \n" ;
					shipping += place.getCity() +", "+ place.getCountry().getName() + ".\n";
					
					tvShipping.setText(shipping);
					ivEditShipping.setImageResource(R.drawable.pencil);
					
				}
			}
		});
        
        bCancel = (Button) findViewById(R.id.bPaymentInfo_Cancel);
        bCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(CheckOutActivity.this)
			    
			    .setMessage("Are you sure you want to leave?")
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			           finish();
			        }
			     })
			    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // do nothing
			        	
			        }
			     })
			    
			     .show();
			}
		});
		
	}
	
	public void onBuyPressed() {
        /* 
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to 
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         * 
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        Intent intent = new Intent(CheckOutActivity.this, PaymentActivity.class);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, Config.REQUEST_CODE_PAYMENT);
    }
	
	private PayPalPayment getThingToBuy(String paymentIntent) {
		
		PayPalItem[] payPalItems = new PayPalItem[cartItems.size()];
		
		float totals = 0;
		String names = "";
		
		for(int i = 0; i < cartItems.size(); i++){
			CartItem cartItem = cartItems.get(i);
			Item item = cartItem.getItem();
			
			if(cartItems.size() != i + 1){
				names += item.getName() +", ";
			}else{
				names += item.getName();
			}
			
			totals += cartItem.getItem().getPrice() * cartItem.getQty();
			PayPalItem payPalItem = new PayPalItem(item.getName(),  cartItem.getQty(), new BigDecimal(String.format("%.2f", item.getPrice())), "USD", String.valueOf(item.getId()));			
			payPalItems[i] = payPalItem;
		}

		
		
		BigDecimal subtotal = PayPalItem.getItemTotal(payPalItems);
        BigDecimal shipping = new BigDecimal(Config.FLATRATE_SHIPPING);
        BigDecimal tax = new BigDecimal("0.0");
        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
        BigDecimal amount = subtotal.add(shipping).add(tax);
        PayPalPayment payment = new PayPalPayment(amount, "USD", names, paymentIntent);
        payment.items(payPalItems).paymentDetails(paymentDetails);
        
        payment.custom("Payments for : " + names);
		
		return payment;
    }
	
	
	private PayPalPayment getStuffToBuy(String paymentIntent) {
        //--- include an item list, payment amount details
        PayPalItem[] items =
            {
                    new PayPalItem("old jeans with holes", 2, new BigDecimal("87.50"), "USD",
                            "sku-12345678"),
                    new PayPalItem("free rainbow patch", 1, new BigDecimal("0.00"),
                            "USD", "sku-zero-price"),
                    new PayPalItem("long sleeve plaid shirt (no mustache included)", 6, new BigDecimal("37.99"),
                            "USD", "sku-33333") 
            };
        BigDecimal subtotal = PayPalItem.getItemTotal(items);
        BigDecimal shipping = new BigDecimal("7.21");
        BigDecimal tax = new BigDecimal("4.67");
        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
        BigDecimal amount = subtotal.add(shipping).add(tax);
        PayPalPayment payment = new PayPalPayment(amount, "USD", "hipster jeans", paymentIntent);
        payment.items(items).paymentDetails(paymentDetails);

        //--- set other optional fields like invoice_number, custom field, and soft_descriptor
        payment.custom("This is text that will be associated with the payment that the app can use.");

        return payment;
    }
	
	public void getCartItems(){
		cartItems = new ArrayList<CartItem>();
		NoSQL.with(this).using(CartItem.class)
	    .bucketId("cart")
	    .retrieve(new RetrievalCallback<CartItem>() {
		@Override
		public void retrievedResults(List<NoSQLEntity<CartItem>> entities) {
			// TODO Auto-generated method stub
			if(entities.size() > 0){
				total = 0;
				
				for(NoSQLEntity<CartItem> entity : entities){
					cartItems.add(entity.getData());
					
					total += entity.getData().getItem().getPrice() * entity.getData().getQty();
				}
				
				tvShippingCost.setText(String.format("%.2f", Config.FLATRATE_SHIPPING));
				tvSubTotal.setText(String.format("%.2f", total));
		        tvTotal.setText(String.format("%.2f", total + Config.FLATRATE_SHIPPING));
		        ivViewItems.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showPopUp();
					}
				});
		        if(dialog_open){
			        cartItemAdapter = new CartItemsAdapter(CheckOutActivity.this, cartItems);
			        cartItemAdapter.notifyDataSetChanged();
			        dialog.dismiss();
			        showPopUp();
		        }
			}
		}   
	    });
		
		
	}
	
	
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == Config.REQUEST_CODE_PAYMENT) {
	            if (resultCode == Activity.RESULT_OK) {
	                PaymentConfirmation confirm =
	                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
	                if (confirm != null) {
	                    try {
	                        Log.i(TAG, confirm.toJSONObject().toString(4));
	                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
	                        /**
	                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
	                         * or consent completion.
	                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
	                         * for more details.
	                         *
	                         * For sample mobile backend interactions, see
	                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
	                         */
	                        Crouton.makeText(
	        						CheckOutActivity.this,
	        						"Payment has been received, successfully.",
	        						style).show();
	                        
	                        new MakeOrder(this).execute();
	                        

	                    } catch (JSONException e) {
	                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
	                        Crouton.makeText(
	    							CheckOutActivity.this,
	    							"Oops, Something went wrong",
	    							style_error).show();
	                    }
	                }
	            } else if (resultCode == Activity.RESULT_CANCELED) {
	                Log.i(TAG, "The user canceled.");
	            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
	                Log.i(
	                        TAG,
	                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
	                Crouton.makeText(
							CheckOutActivity.this,
							"Invalid Payment, please try again",
							style_error).show();
	                
	            }
	        }else if(requestCode == Config.REQUEST_CODE_SET_PLACE){
	        	if(resultCode == RESULT_OK){
	                place = (Place) data.getSerializableExtra("result");
	                
	               
	                String shipping = "";
					shipping += place.getFirstname() + " " + place.getLastname() + ", \n";
					shipping += "P.O. Box " + place.getAddress() + " - " + place.getZip() + ", \n" ;
					shipping += place.getCity() +", "+ place.getCountry().getName() + ".\n";
					
					tvShipping.setText(shipping);
					ivEditShipping.setImageResource(R.drawable.pencil);
	                
	            }
	            if (resultCode == RESULT_CANCELED) {
	                //Write your code if there's no result
	            }
	        }  
	    }
	 
	 public void clearCart(){
		 for(CartItem cartItem : cartItems){
			 Time time = new Time();   time.setToNow();
			 NoSQLEntity<CartItem> entity = new NoSQLEntity<CartItem>(
						"purchased", String.valueOf(time.toMillis(false)));
				entity.setData(cartItem);
				NoSQL.with(CheckOutActivity.this).using(CartItem.class)
						.save(entity);
		 }
		 
		 
		 
		 for(CartItem cartItem : cartItems){
			
				NoSQL.with(CheckOutActivity.this).using(CartItem.class)
			    .bucketId("cart")
			    .entityId(String.valueOf(cartItem.getItem().getId()))
			    .delete();
		 }
	 }
	
	 public void showPopUp() {
			dialog = new Dialog(CheckOutActivity.this,
					R.style.FullHeightDialog);
			dialog.setContentView(R.layout.view_product_color_popup);

			lvCartItems = (ListView) dialog.findViewById(R.id.lvColorPopup_color);
			cartItemAdapter = new CartItemsAdapter(this, cartItems);
			lvCartItems.setAdapter(cartItemAdapter);

			lvCartItems.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
						long arg3) {
					// TODO Auto-generated method stub
					
					Intent intent = new Intent(CheckOutActivity.this, SingleProductActivity.class);
					intent.putExtra("item_id", cartItems.get(pos).getItem().getId());
					startActivity(intent);
				}
			});
			
			dialog.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					dialog_open =false;
				}
			});
			
			dialog.setCancelable(true);
			dialog.show();
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
				return cartItems.size();
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
				
				Item item = cartItems.get(position).getItem();
				
				TextView tvName = (TextView) v.findViewById(R.id.tvCartItem_name);
				TextView tvOptions = (TextView) v.findViewById(R.id.tvCartItem_Options);
				TextView tvPrice = (TextView) v.findViewById(R.id.tvCartItem_Price);
				TextView tvQty = (TextView) v.findViewById(R.id.tvCartItem_Qty);
				ImageView ivThumb = (ImageView) v.findViewById(R.id.ivCartItem_thumbnail);
				
				
				
				Picasso.with(CheckOutActivity.this)
				.load(Config.IMAGE_MEDIUM+ item.getFeatured_image()).placeholder(R.drawable.placeholder)
				.into(ivThumb)
				;
				
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
		                CheckOutActivity.this);
		        
		        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CheckOutActivity.this, android.R.layout.select_dialog_singlechoice);
		        
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
		    					NoSQL.with(CheckOutActivity.this).using(CartItem.class)
		    							.save(entity);
		    					dialog_open = true;
		    					
		    					getCartItems();
		                    }
		                    	
		                });
		        
		        builderSingle.show();
		}
		
		public class MakeOrder extends AsyncTask<Void, Void, Void>{
			
			/*
			ship_zone_id	ship_country	ship_country_id	bill_company	bill_firstname	bill_lastname	bill_email	bill_phone			
			bill_address1	bill_address2	bill_city	bill_zip	bill_zone				
			bill_zone_id	bill_country	bill_country_id		shipping_method		shipping_price	
			tax gift_card_discount	coupon_discount	cart_subtotal	cart_total	payment_description		referral 	shipping_notes				
										*/

			
			Context context;
			ProgressDialog dialog;
			Boolean error = false;
			
			public MakeOrder(Context context){
				this.context = context;	
			}
			
			
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				//JSONListener.onPreExecute();
				
				dialog = new ProgressDialog(CheckOutActivity.this);
				dialog.setMessage("Making an Order");
				dialog.setCancelable(false);
				dialog.show();
			}
			

			/**
			 * 
			 * */
			protected Void doInBackground(Void... args) {
				// getting JSON string from URL
				
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(); 
				params.add(new BasicNameValuePair("customer_id", ""));
				params.add(new BasicNameValuePair("company", ""));
				params.add(new BasicNameValuePair("firstname", place.getFirstname()));
				params.add(new BasicNameValuePair("lastname", place.getLastname()));
				params.add(new BasicNameValuePair("phone", place.getPhone_number()));
				params.add(new BasicNameValuePair("email", place.getEmail()));
				params.add(new BasicNameValuePair("ship_company", ""));
				params.add(new BasicNameValuePair("ship_firstname", place.getFirstname()));
				params.add(new BasicNameValuePair("ship_lastname", place.getLastname()));
				params.add(new BasicNameValuePair("ship_email", place.getEmail()));
				params.add(new BasicNameValuePair("ship_phone", place.getPhone_number()));
				params.add(new BasicNameValuePair("ship_address1", place.getAddress()));
				params.add(new BasicNameValuePair("ship_address2", place.getAddress2()));
				params.add(new BasicNameValuePair("ship_city", place.getCity()));
				params.add(new BasicNameValuePair("ship_zip", place.getZip()));
				params.add(new BasicNameValuePair("ship_zone", place.getZone().getName()));
				params.add(new BasicNameValuePair("ship_zone_id", place.getZone().getId()));
				params.add(new BasicNameValuePair("ship_country", place.getCountry().getName()));
				params.add(new BasicNameValuePair("ship_country_id", place.getCountry().getId()));
				params.add(new BasicNameValuePair("bill_company", ""));
				params.add(new BasicNameValuePair("bill_firstname", place.getFirstname()));
				params.add(new BasicNameValuePair("bill_lastname", place.getLastname()));
				params.add(new BasicNameValuePair("bill_email", place.getEmail()));
				params.add(new BasicNameValuePair("bill_phone", place.getPhone_number()));
				params.add(new BasicNameValuePair("bill_address1", place.getAddress()));
				params.add(new BasicNameValuePair("bill_address2", place.getAddress2()));
				params.add(new BasicNameValuePair("bill_city", place.getCity()));
				params.add(new BasicNameValuePair("bill_zip", place.getZip()));
				params.add(new BasicNameValuePair("bill_zone", place.getZone().getName()));
				params.add(new BasicNameValuePair("bill_zone_id", place.getZone().getId()));
				params.add(new BasicNameValuePair("bill_country", place.getCountry().getName()));
				params.add(new BasicNameValuePair("bill_country_id", place.getCountry().getId()));
				
				params.add(new BasicNameValuePair("tax", "0"));
				params.add(new BasicNameValuePair("gift_card_discount", "0"));
				params.add(new BasicNameValuePair("coupon_discount", "0"));
				
				
				
				
				if(ivCODCheck.getVisibility() == ImageView.VISIBLE){
					params.add(new BasicNameValuePair("payment_description", "C.O.D"));
				 }else if(ivPaypalCheck.getVisibility() == ImageView.VISIBLE){
					 params.add(new BasicNameValuePair("payment_description", "Paypal"));
				 }else if(ivPickCheck.getVisibility() == ImageView.VISIBLE){
					 params.add(new BasicNameValuePair("payment_description", "Pick From Shop"));
				 }else{
					 params.add(new BasicNameValuePair("payment_description", "Something went wrong"));
				 }
				
				
				params.add(new BasicNameValuePair("referral", "Referal"));
				
				params.add(new BasicNameValuePair("product_id", "2"));
				params.add(new BasicNameValuePair("quantity", "1"));
				
				float total = 0;
				String options = "";
				for(CartItem cartItem : cartItems){
					params.add(new BasicNameValuePair("product_ids[]", String.valueOf(cartItem.getItem().getId())));
					params.add(new BasicNameValuePair("quantities[]", String.valueOf(cartItem.getQty())));
					params.add(new BasicNameValuePair("options[]", String.valueOf(cartItem.getOptions())));
					
					total += cartItem.getItem().getPrice() * cartItem.getQty();
					options += cartItem.getItem().getName() + "\t-\t" +cartItem.getOptions() + "\n";
				}
				
				params.add(new BasicNameValuePair("shipping_method", "FLATRATE SHIPPING"));
				params.add(new BasicNameValuePair("shipping_price", String.valueOf(Config.FLATRATE_SHIPPING))); //to-do later
				params.add(new BasicNameValuePair("cart_subtotal", String.valueOf(total)));
				params.add(new BasicNameValuePair("cart_total", String.valueOf(total + Config.FLATRATE_SHIPPING)));
				params.add(new BasicNameValuePair("shipping_notes", options));
				
				JSONObject json = new JSONParser().makeHttpRequest("http://kimutai.org/wooApi/api/create_order.php", "POST", params);
				
				
				Log.d("create order", json.toString());
				
				if(json == null){
					error = true;
				}
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
				dialog.dismiss();
				
				if(!error){
				Crouton.makeText(
						CheckOutActivity.this,
						"Thank you for purchasing!",
						style).show();
                
                clearCart();
                
                new Thread(){
                	public void run(){
                		try {
							sleep(2500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally{
							startActivity(new Intent(CheckOutActivity.this, PurchasedItemsActivity.class));
							finish();
						}
                	}
                }.start();
				
                }else{
                	Crouton.makeText(
    						CheckOutActivity.this,
    						"Unable to connect. Ensure that you have mobile data",
    						style_error).show();
                }
				
				
				
			}
		}
		
}
