package com.example.timetablereader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.timetablereader.Objects.Route;
import com.example.timetablereader.Objects.Stop;
import com.example.timetablereader.Objects.StopTime;
import com.example.timetablereader.Objects.Trip;
import com.example.timetablereader.XMLParsing.DataLoader;
import com.example.timetablereader.XMLParsing.IFeedParser;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class RoutesActivity extends Activity {

	public static final String ROUTE_ID = "com.example.timetablereader.RoutesActivity.routeId";
	public static final String OUTBOUND = "com.example.timetablereader.RoutesActivity.outbound";
	public static final String TRIP_ID = "com.example.timetablereader.RoutesActivity.tripId";

	private List<Route> routes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routes);

		// load data
		DataHolder.initialise(this, isConnectedToInternet());
		this.routes = DataHolder.getRoutes();

		// load view
		addItemsToRoutesSpinner();
		
		
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		boolean connectedToInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.routes, menu);
		return true;
	}


    /**
     * Called when the user clicks the Send button
     * @param view
     */
    public void viewTrips(View view){

    	// gets the route
    	Spinner spinner = (Spinner) findViewById(R.id.routes_spinner);
    	int routeIndex = spinner.getSelectedItemPosition();
    	Route route = routes.get(routeIndex);

    	// get outbound
    	int outboundId = R.id.radioOutbound;
    	RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
    	int radioButtonId = radioGroup.getCheckedRadioButtonId();
    	boolean outbound = (radioButtonId == outboundId);

    	// the intent binds this view to the other view we want to display
    	Intent intent = new Intent(this, DisplayTripsActivity.class);
    	intent.putExtra(ROUTE_ID, route.getRouteId());
    	intent.putExtra(OUTBOUND, outbound);

    	Log.d("TimetableReader", "Route=" + route.getRouteId() + ", outbound=" + outbound);

    	// start the new activity
    	startActivity(intent);
    }

	private void addItemsToRoutesSpinner(){
		Spinner spinner = (Spinner) findViewById(R.id.routes_spinner);
		List<String> list = new ArrayList<String>();
		for(Route r: routes){
			list.add(r.getRouteId() + " - " + r.getName());
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}
	
	private boolean isConnectedToInternet(){
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
