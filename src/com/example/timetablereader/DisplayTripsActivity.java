package com.example.timetablereader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.timetablereader.Objects.Trip;
import com.example.timetablereader.XMLParsing.DataLoader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DisplayTripsActivity extends Activity {


	private List<Trip> trips;
	private DataLoader dataLoader;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_trips);

		// load trips
		dataLoader = new DataLoader(this);
		trips = dataLoader.loadTrips();

		// work out what trip they want to display
		Intent intent = getIntent();
		int routeId = intent.getIntExtra(RoutesActivity.ROUTE_ID, 0);
		boolean outbound = intent.getBooleanExtra(RoutesActivity.OUTBOUND, false);

		// display the trips
		displayTrips(routeId, outbound);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_trips, menu);
		return true;
	}


	public void displayStops(View view){
		Log.d("TimetableReader", "displaying stops");
	}

	private void displayTrips(int routeId, boolean outbound){

		// find relevent trips
		List<String> releventTrips = new ArrayList<String>();
    	for (Trip trip : trips){
    		if (trip.isOutbound() == outbound && trip.getRouteId() == routeId){
    			releventTrips.add(trip.getTripId() + "");
    		}
    	}
    	Collections.sort(releventTrips);

    	// display them
    	ArrayAdapter<String> adapter =
    		new ArrayAdapter<String>(this, R.layout.trip_row,releventTrips);
    	ListView myList=(ListView)findViewById(R.id.trips_list);
        myList.setAdapter(adapter);
	}

}
