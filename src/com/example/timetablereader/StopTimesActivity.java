package com.example.timetablereader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.timetablereader.Log;
import com.example.timetablereader.Objects.Stop;
import com.example.timetablereader.Objects.StopTime;
import com.example.timetablereader.XMLParsing.DataLoader;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class StopTimesActivity extends Activity {

	// all the data
	private Map<Integer, List<StopTime>> stopTimes;
	private Map<Integer, Stop> stops;

	// data relating to this trip
	private int routeId;
	private int tripId;
	private List<Map<String, Object>> stopsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.write("started stop times activity");

        // load data
        stopTimes = DataHolder.getStopTimes();
        stops = DataHolder.getStops();

		// work out what trip they want to display
		Intent intent = getIntent();
		this.routeId = intent.getIntExtra(RoutesActivity.ROUTE_ID, 0);
		this.tripId =  intent.getIntExtra(RoutesActivity.TRIP_ID, 0);

		// find stop times
		findStopTimes();

        // display stop times
        displayTripStopTimes();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stop_times, menu);
        return true;
    }


	private void findStopTimes(){
		// build list to display
        stopsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;

        Log.write("total number of stops is " + stopTimes.size());

        List<StopTime> applicableStopTimes = stopTimes.get(tripId);
        Collections.sort(applicableStopTimes);
        for(StopTime s: applicableStopTimes){
        	map = new HashMap<String, Object>();
        	String stopName = stops.get(s.getStopId()).getStopName();

        	map.put("stop_name", stopName);
        	map.put("departure_time", s.getDepartureTime());
        	stopsList.add(map);
        }

        Log.write("Number of stops found is " + stopsList.size());
	}


	private void displayTripStopTimes(){
        setContentView(R.layout.activity_stop_times);

        // set title
        TextView t=new TextView(this);
        t=(TextView)findViewById(R.id.title);
        t.setText("Stop times for route " + routeId + ", trip " + tripId);

        // display the stops
        String[] from = new String[]{"stop_name", "departure_time"};
        int[] to = new int[]{R.id.stop_name, R.id.departure_time};
        SimpleAdapter adapter = new SimpleAdapter(this, stopsList, R.layout.row, from, to);
        ListView myList=(ListView)findViewById(R.id.stop_list);
        myList.setAdapter(adapter);

	}
}
