package com.example.timetablereader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.xmlpull.v1.XmlSerializer;

import com.example.timetablereader.Objects.Route;
import com.example.timetablereader.Objects.Stop;
import com.example.timetablereader.Objects.StopTime;
import com.example.timetablereader.Objects.Trip;
import com.example.timetablereader.XMLParsing.BaseFeedParser;
import com.example.timetablereader.XMLParsing.FeedParser;
import com.example.timetablereader.XMLParsing.XMLPullFeedParser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class StopTimesActivity extends Activity {

	public static final String STOP_TIMES_URL = "http://homepages.ecs.vuw.ac.nz/~ian/nwen304/stop_times.xml";
	public static final String STOPS_URL = "http://homepages.ecs.vuw.ac.nz/~ian/nwen304/stops.xml";
	public static final String ROUTES_URL = "http://homepages.ecs.vuw.ac.nz/~ian/nwen304/routes.xml";
	public static final String TRIPS_URL = "http://homepages.ecs.vuw.ac.nz/~ian/nwen304/trips.xml";
	
	public static final String STOP_TIMES_URL_SHORT = "http://homepages.ecs.vuw.ac.nz/~ian/nwen304/simpletimetable/stop_times.xml";
	
	private FeedParser parser;
	
	
	private List<StopTime> stopTimes;
	private Map<Integer, Stop> stops;
	private List<Route> routes;
	private List<Trip> trips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {	
        super.onCreate(savedInstanceState);
        Log.d("TimetableReader", "about to load stops");
        this.parser = (FeedParser) new XMLPullFeedParser();
        
        
        loadStopTimes();
        loadTrips();
        loadRoutes();
        Log.d("TimetableReader", "about to load stops");
        loadStops();
        
        Log.d("TimetableReader", "loaded stops");
        displayTripStopTimes(2, 53832, true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stop_times, menu);
        return true;
    }



	private void loadStopTimes(){
    	try{
	    	stopTimes = parser.parseStopTimes(STOP_TIMES_URL_SHORT);

	    	//String xml = writeXml();
	    	Log.d("TimetableReader", "stop times read - " + stopTimes.size());
	    	for(StopTime s: stopTimes){
	    		Log.d("TimetableReader", s.toString());
	    	}
	    	
    	} catch (Throwable t){
    		Log.e("TimetableReader",t.getMessage(),t);
    	}
    }
	
	private void loadTrips(){
    	try{
	    	trips = parser.parseTrips(TRIPS_URL);

	    	//String xml = writeXml();
	    	Log.d("TimetableReader", "trips read - " + trips.size());
	    	for(Trip t: trips){
	    		Log.d("TimetableReader", t.toString());
	    	}
    	} catch (Throwable t){
    		Log.e("TimetableReader",t.getMessage(),t);
    	}	
	}
	
	private void loadRoutes(){
    	try{
	    	routes = parser.parseRoutes(ROUTES_URL);

	    	//String xml = writeXml();
	    	Log.d("TimetableReader", "routes read - " + routes.size());
	    	for(Route r: routes){   		
	    		Log.d("TimetableReader", r.toString());
	    	}
    	} catch (Throwable t){
    		Log.e("TimetableReader",t.getMessage(),t);
    	}		
	}
	
	private void loadStops(){
		String FILENAME = "stops.xml";	
		
    	try{
 		
    		try{	
    			FileInputStream fis = openFileInput(FILENAME);
    			Log.d("TimetableReader", "Reading stops from memory");
    			stops = parser.parseStops(fis);  
    			fis.close();
    			
    			Log.d("TimetableReader", "Loaded stops - " + stops.size());
    		} catch (FileNotFoundException e){
    			Log.d("TimetableReader", "Reading stops from online");
    			
        		// load stops online
    	    	stops = parser.parseStops(STOPS_URL);

    	    	// save to internal memory
    	    	String xml = writeStopsToXml(); 				
        		FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);	
        		fos.write(xml.getBytes());   		
        		fos.close();	
    		}
    	} catch (Throwable t){
    		Log.e("TimetableReader",t.getMessage(),t);
    	}			
	}
	
	
	private void displayTripStopTimes(int routeId, int tripId, boolean outbound ){
        setContentView(R.layout.activity_stop_times);
        TextView t=new TextView(this); 
        t=(TextView)findViewById(R.id.title); 
        t.setText("Route " + routeId + ", trip " + tripId);

        
        // build list to display
        List<Map<String, Object>> stopsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for(StopTime s: this.stopTimes){    
        	if (s.getRouteId() == routeId && s.getTripId() == tripId && s.isOutbound()){
        		map = new HashMap<String, Object>(); 
        		String stopName = stops.get(s.getStopId()).getStopName();
        		map.put("stop_name", stopName);
        		map.put("departure_time", s.getDepartureTime());
        		stopsList.add(map);
        	}
        }
        
        // display them
        String[] from = new String[]{"stop_name", "departure_time"};
        int[] to = new int[]{R.id.stop_name, R.id.departure_time};       
        SimpleAdapter adapter = new SimpleAdapter(this, stopsList, R.layout.row, from, to);
        ListView myList=(ListView)findViewById(R.id.stop_list);
        myList.setAdapter(adapter);
                
	}



	private String writeStopsToXml(){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", BaseFeedParser.DOCUMENT);		
			for (Stop st: stops.values()){
				serializer.startTag("", BaseFeedParser.RECORD);
				
				serializer.startTag("", BaseFeedParser.STOP_ID);
				serializer.text("" + st.getStopId());
				serializer.endTag("", BaseFeedParser.STOP_ID);
				
				serializer.startTag("", BaseFeedParser.STOP_NAME);
				serializer.text(st.getStopName());
				serializer.endTag("", BaseFeedParser.STOP_NAME);
				
				serializer.startTag("", BaseFeedParser.STOP_LAT);
				serializer.text("" + st.getStopLat());
				serializer.endTag("", BaseFeedParser.STOP_LAT);
				
				serializer.startTag("", BaseFeedParser.STOP_LON);
				serializer.text("" + st.getStopLon());
				serializer.endTag("", BaseFeedParser.STOP_LON);
				
				serializer.endTag("", BaseFeedParser.RECORD);
			}
			serializer.endTag("", BaseFeedParser.DOCUMENT);
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}



}
