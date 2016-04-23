package com.gilo.soko;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import com.gilo.soko.CheckOutActivity.MakeOrder;
import com.gilo.soko.config.Config;
import com.gilo.soko.utils.JSONParser;
import com.gilo.wooApi.soko.R;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class LeaveACommentActivity extends ActionBarActivity{

	
	int id = 0;
	Style style, style_error;
	RatingBar rbOverall, rbStyle, rbComfort;
	EditText etComment, etName;
	Button bSubmit, bCancel;
	
	protected void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.leave_comment);
		
		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTextColor(Color.parseColor("#4E6A9F"));
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		//getActionBar().setIcon(R.drawable.ic_ab_logo);
		getActionBar().setTitle(getString(R.string.review));

		id = getIntent().getIntExtra("item_id", 0);
		
		
		rbOverall = (RatingBar) findViewById(R.id.rbLeaveComment_overall);
		rbComfort = (RatingBar) findViewById(R.id.rbLeaveComment_comfort);
		rbStyle = (RatingBar) findViewById(R.id.rbLeaveComment_style);
		
		
		
		etComment = (EditText) findViewById(R.id.etLeaveComment_comment);
		etName = (EditText) findViewById(R.id.etLeaveComment_name);
		
		bSubmit = (Button) findViewById(R.id.bLeaveComment_Submit);
		bCancel = (Button) findViewById(R.id.bLeaveComment_cancel);
		
		
		
		// Define configuration options
				Configuration croutonConfiguration = new Configuration.Builder()
						.setDuration(2500).build();
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
				
		bSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new ReviewsMaker(LeaveACommentActivity.this).execute();
			}
		});
		
		bCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LeaveACommentActivity.this,
						SingleProductDrawerActivity.class);
				intent.putExtra("item_id", id);
				startActivity(intent);
				
				finish();
			}
		});
	}
	
public class ReviewsMaker extends AsyncTask<Void, Void, Void>{
		
		Context context;
		ProgressDialog dialog;
		Boolean error = false;
		
		public ReviewsMaker(Context context){
			this.context = context;	
		}
		
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//JSONListener.onPreExecute();
			
			dialog = new ProgressDialog(LeaveACommentActivity.this);
			dialog.setMessage("Adding review");
			dialog.setCancelable(false);
			dialog.show();
			
		}
		

		/**
		 * 
		 * */
		protected Void doInBackground(Void... args) {
			// getting JSON string from URL
			
//			id	product_id	comment	rating	comfort	style	customer_name	date
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(); 
			params.add(new BasicNameValuePair("product_id", String.valueOf(id)));
			params.add(new BasicNameValuePair("comment", etComment.getText().toString()));
			params.add(new BasicNameValuePair("rating", String.valueOf(rbOverall.getRating())));
			params.add(new BasicNameValuePair("comfort",String.valueOf(rbOverall.getRating())));
			params.add(new BasicNameValuePair("style",String.valueOf(rbOverall.getRating())));
			params.add(new BasicNameValuePair("customer_name", etName.getText().toString()));
			
			
			
			JSONArray json = new JSONParser().makeHttpRequestForArray(Config.ADD_REVIEW_URL + id, "POST", params);
			
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
				Intent intent = new Intent(LeaveACommentActivity.this,
						SingleProductDrawerActivity.class);
				intent.putExtra("item_id", id);
				startActivity(intent);
				
				finish();
            }else{
            	Crouton.makeText(LeaveACommentActivity.this,
						"Unable to connect. Ensure that you have mobile data",
						style_error).show();
            }
			
			
			
		}
	}
	
	
	
}
