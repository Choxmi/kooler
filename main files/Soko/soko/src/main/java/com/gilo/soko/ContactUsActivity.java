package com.gilo.soko;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.gilo.wooApi.soko.R;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class ContactUsActivity extends ActionBarActivity {

	Style style, style_error;

	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.contact_us);

		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");
		TextView yourTextView = (TextView) findViewById(titleId);
		yourTextView.setTextColor(Color.parseColor("#4E6A9F"));

		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		// getActionBar().setIcon(R.drawable.ic_ab_logo);
		getActionBar().setTitle("Contact Us");

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

		Button bSend = (Button) findViewById(R.id.bContactUs_send);
		Button bCancel = (Button) findViewById(R.id.bContactUs_cancel);

		bSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Crouton.makeText(
						ContactUsActivity.this,
						"Your message has been sent successfully",
						style).show();
				new Thread() {
					public void run() {
						try {
							
							
							sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally{
							finish();
						}
					}
				}.start();
				;
			}
		});

		bCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		ImageView fb = (ImageView)findViewById(R.id.contact_fb);
		ImageView twitter = (ImageView)findViewById(R.id.contact_twitter);
		ImageView gplus = (ImageView)findViewById(R.id.contact_gplus);
		final ImageView insta = (ImageView)findViewById(R.id.contact_inst);
		ImageView youtube = (ImageView)findViewById(R.id.contact_youtube);

		fb.setOnClickListener(new OnClickListener() {

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

		twitter.setOnClickListener(new OnClickListener() {

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

		gplus.setOnClickListener(new OnClickListener() {

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

		insta.setOnClickListener(new OnClickListener() {

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

		youtube.setOnClickListener(new OnClickListener() {

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

	}

}
