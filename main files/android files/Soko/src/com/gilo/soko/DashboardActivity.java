package com.gilo.soko;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gilo.wooApi.soko.R;

public class DashboardActivity extends Activity{
	
	LinearLayout llOrder, llPaymentMethod, llShippingMethod, llBanners;

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		
		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTextColor(Color.parseColor("#4E6A9F"));
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		//getActionBar().setIcon(R.drawable.ic_ab_logo);
		getActionBar().setTitle("Dashboard");
		
		llOrder = (LinearLayout) findViewById(R.id.llDashboard_Orders);
		llPaymentMethod = (LinearLayout) findViewById(R.id.llDashboard_PaymentMethods);
		llShippingMethod = (LinearLayout) findViewById(R.id.llDashboard_ShippingMethods);
		llBanners = (LinearLayout) findViewById(R.id.llDashboard_Banners);
		
		llOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DashboardActivity.this, OrderActivity.class ));
			}
		});
		
	}
}
