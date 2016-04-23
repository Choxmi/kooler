package com.gilo.soko;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import com.gilo.wooApi.soko.R;

public class AboutActivity extends ActionBarActivity {
	
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.about);
		
		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTextColor(Color.parseColor("#4E6A9F"));
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		//getActionBar().setIcon(R.drawable.ic_ab_logo);
		getActionBar().setTitle("About");

		ImageView fb = (ImageView)findViewById(R.id.ivAbout_facebook);
		ImageView twitter = (ImageView)findViewById(R.id.ivAbout_twitter);
		ImageView gplus = (ImageView)findViewById(R.id.ivAbout_gplus);
		ImageView insta = (ImageView)findViewById(R.id.ivAbout_ig);
		ImageView email = (ImageView)findViewById(R.id.ivAbout_email);

		fb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String url = "http://starinxs.com";
				Log.e("Intent", Intent.ACTION_VIEW);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
		});

		twitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String url = "http://starinxs.com";
				Log.e("Intent",Intent.ACTION_VIEW);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
		});

		gplus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String url = "http://starinxs.com";
				Log.e("Intent", Intent.ACTION_VIEW);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
		});

		insta.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String url = "http://starinxs.com";
				Log.e("Intent", Intent.ACTION_VIEW);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
		});

		email.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String url = "http://starinxs.com";
				Log.e("Intent", Intent.ACTION_VIEW);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
		});
			
	}

}
