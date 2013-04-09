package com.example.timetablereader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.timetablereader.Objects.Trip;
import com.example.timetablereader.XMLParsing.DataLoader;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayTripsActivity extends ListActivity {


	private List<Trip> trips;

	private int routeId;
	private boolean outbound;
	private List<String> releventTrips;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_trips);

		// load trips
		Log.write("loading trips");
		trips = DataHolder.getTrips();
		Log.write("loaded trips");

		// work out what trip they want to display
		Intent intent = getIntent();
		this.routeId = intent.getIntExtra(RoutesActivity.ROUTE_ID, 0);
		this.outbound = intent.getBooleanExtra(RoutesActivity.OUTBOUND, false);

		// display the trips
		findTrips();
		displayTrips();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_trips, menu);
		return true;
	}


	private void findTrips(){
		// find relevent trips
		releventTrips = new ArrayList<String>();
    	for (Trip trip : trips){
    		if (trip.isOutbound() == outbound && trip.getRouteId() == routeId){
    			releventTrips.add(trip.getTripId() + "");
    		}
    	}
    	Collections.sort(releventTrips);
	}


	private void displayTrips(){
		// display the title
		TextView title = (TextView)findViewById(R.id.trips_title);
		String titleText = String.format("%s trips for route %d", outbound ? "Outbound": "Inbound", routeId);
		title.setText(titleText);


    	// display them
    	ArrayAdapter<String> adapter =
    		new ArrayAdapter<String>(this, R.layout.trip_row,releventTrips);
    	ListView myList = getListView();
        myList.setAdapter(adapter);
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// get the trip that was selected
		int tripId = Integer.parseInt((String)getListView().getItemAtPosition(position));

		// set up the intent
		Intent intent = new Intent(this, StopTimesActivity.class);
		intent.putExtra(RoutesActivity.ROUTE_ID, routeId);
		intent.putExtra(RoutesActivity.TRIP_ID, tripId);
    	intent.putExtra(RoutesActivity.OUTBOUND, outbound);

		this.startActivity(intent);
	}

}
