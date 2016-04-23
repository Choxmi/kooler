package com.gilo.soko;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gilo.soko.config.Config;
import com.gilo.wooApi.soko.R;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class LoginActivity extends Activity{
	
	EditText etEmail, etPassword;
	Button bLogin;
	Style style, style_error;

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTextColor(Color.parseColor("#4E6A9F"));
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		//getActionBar().setIcon(R.drawable.ic_ab_logo);
		getActionBar().setTitle("Login");
		
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
		
		etEmail = (EditText) findViewById(R.id.etLogin_email);
		etPassword = (EditText) findViewById(R.id.etLogin_password);
		bLogin = (Button) findViewById(R.id.bLogin_login);
		
		bLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(etEmail.getText().toString().equals(Config.ADMIN_USERNAME) && etPassword.getText().toString().equals(Config.ADMIN_PASSWORD)){
					startActivity(new Intent(getBaseContext(), DashboardActivity.class));
				}else{
					Crouton.makeText(
							LoginActivity.this,
							"Oops, Incorrect username / password",
							style_error).show();
				}
			}
		});
	}
}
