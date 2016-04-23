package com.gilo.soko;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.gilo.soko.models.Country;
import com.gilo.soko.models.Place;
import com.gilo.soko.models.Zone;
import com.gilo.wooApi.soko.R;

public class ShippingInformationActivity extends ActionBarActivity{

	
	EditText etName, etLastName, etEmail, etAddress, etZip, etCity, etPhoneNumber;
	Button bSave, bCancel;
	Spinner sCountries, sZones;
	DataContainer dataContainer;
	
	ArrayList<Country> countries = new ArrayList<Country>();
	ArrayList<Country> countries2 = new ArrayList<Country>();
	ArrayList<String> country_list = new ArrayList<String>();
	ArrayList<Zone> zones = new ArrayList<Zone>();
	ArrayList<String> zoneList = new ArrayList<String>();
	
	protected void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.checkout);
		
		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTextColor(Color.parseColor("#4E6A9F"));
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		//getActionBar().setIcon(R.drawable.ic_ab_logo);
		getActionBar().setTitle("Shipping Information");
		
		etName = (EditText) findViewById(R.id.etCheckOut_name);
		etLastName = (EditText) findViewById(R.id.etCheckOut_lastname);
		etEmail = (EditText) findViewById(R.id.etCheckOut_email);
		etAddress = (EditText) findViewById(R.id.etCheckOut_address);
		etZip = (EditText) findViewById(R.id.etCheckOut_zip);
		etCity = (EditText) findViewById(R.id.etCheckOut_city);
		etPhoneNumber = (EditText) findViewById(R.id.etCheckOut_phone);
		
		
		bSave = (Button) findViewById(R.id.bCheckOut_save);
		bCancel = (Button) findViewById(R.id.bCheckOut_cancel);
		
		sCountries = (Spinner) findViewById(R.id.sCountries);
		sZones = (Spinner) findViewById(R.id.sZone);
		
		dataContainer = ((DataContainer) getApplicationContext());
		
		countries = dataContainer.getCountries();
		Collections.sort(countries, new SequenceComparator());
		for(Country country : countries){
			country_list.add(country.getName());
		}
		
		
		zones = dataContainer.getZones();
		
		
		
		for(Zone zone : zones){
			if(zone.getCountry_id().equals(countries.get(0).getId())){
				zoneList.add(zone.getName());
			}
		}
		
		sCountries.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				zoneList = new ArrayList<String>();
				for(Zone zone : zones){
					if(zone.getCountry_id().equals(countries.get(pos).getId())){
						zoneList.add(zone.getName());
					}
				}
				
				ArrayAdapter<String> zonedataAdapter = new ArrayAdapter<String> (ShippingInformationActivity.this, android.R.layout.simple_spinner_item,zoneList); 
				zonedataAdapter.setDropDownViewResource(R.layout.single_spinner_item);
				sZones.setAdapter(zonedataAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,country_list); 
		dataAdapter.setDropDownViewResource(R.layout.single_spinner_item);
		sCountries.setAdapter(dataAdapter);
		
		ArrayAdapter<String> zonedataAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,zoneList); 
		zonedataAdapter.setDropDownViewResource(R.layout.single_spinner_item);
		sZones.setAdapter(zonedataAdapter);
		
		NoSQL.with(this).using(Place.class).bucketId("shipping")
		
		.retrieve(new RetrievalCallback<Place>() {

			@Override
			public void retrievedResults(
					List<NoSQLEntity<Place>> entities) {
				// TODO Auto-generated method stub
				if (entities.size() > 0) {
					Place place = entities.get(0).getData(); 
					etName.setText(place.getFirstname());
					etLastName.setText(place.getLastname());
					etEmail.setText(place.getEmail());
					etAddress.setText(place.getAddress());
					etZip.setText(place.getZip());
					etCity.setText(place.getCity());
					etPhoneNumber.setText(place.getPhone_number());
					
					setSpinners(place);
					
				}
			}
		});
		
		bSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Place place = new Place();
				place.setId("1");
				place.setEmail(etEmail.getText().toString().trim());
				place.setAddress(etAddress.getText().toString().trim());
				place.setZip(etZip.getText().toString().trim());
				place.setCity(etCity.getText().toString().trim());
				place.setPhone_number(etPhoneNumber.getText().toString().trim());
				//place.setCountry(etCountry.getText().toString().trim());
				place.setFirstname(etName.getText().toString().trim());
				place.setLastname(etLastName.getText().toString().trim());
				place.setCountry(countries.get(sCountries.getSelectedItemPosition()));
				place.setZone(getSelectedZone());
				NoSQLEntity<Place> entity = new NoSQLEntity<Place>(
						"shipping", String.valueOf(place.getAddress()));
				entity.setData(place);
				NoSQL.with(ShippingInformationActivity.this).using(Place.class).bucketId(place.getId())
						.save(entity);
				
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result", place);
				setResult(RESULT_OK,returnIntent);
				finish();
			}
		});
		
		bCancel.setOnClickListener(new OnClickListener() {
			
			@Override 
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(ShippingInformationActivity.this)
			    .setMessage("Are you sure you want to discard an changes?")
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
	
	public Zone getSelectedZone(){
		Zone zone = new Zone();
		
		for(  Zone z : zones){
			if(z.getName().equals(zoneList.get(sZones.getSelectedItemPosition()))){
				return z;
			}
		}
		
		return zone;
	}
	
	public void setSpinners(Place place){
		
				
		sCountries.setSelection(country_list.indexOf(place.getCountry().getName()));
		zoneList = new ArrayList<String>();
		for(Zone zone : zones){
			if(zone.getCountry_id().equals(place.getCountry().getId())){
				zoneList.add(zone.getName());
			}
		}
		
		ArrayAdapter<String> zonedataAdapter = new ArrayAdapter<String> (ShippingInformationActivity.this, android.R.layout.simple_spinner_item,zoneList); 
		zonedataAdapter.setDropDownViewResource(R.layout.single_spinner_item);
		sZones.setAdapter(zonedataAdapter);
		sZones.setSelection(zoneList.indexOf(place.getZone().getName()));
	}
	

	public class SequenceComparator implements Comparator<Country>
	{
	    public int compare(Country left, Country right) {
	        return Integer.parseInt(left.getSequence()) - Integer.parseInt(right.getSequence());
	    }
	}
	
}
