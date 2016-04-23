package com.gilo.soko;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
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
			
	}

}
