package com.example.timetablereader.XMLParsing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.example.timetablereader.Objects.Route;
import com.example.timetablereader.Objects.Stop;
import com.example.timetablereader.Objects.StopTime;
import com.example.timetablereader.Objects.Trip;


/**
 * This class handles the loading of data from XML files either online or in memory.
 * @author Izzi
 *
 */
public class DataLoader {

	public static final String STOP_TIMES_URL = "http://homepages.ecs.vuw.ac.nz/~ian/nwen304/stop_times.xml";
	public static final String STOPS_URL = "http://homepages.ecs.vuw.ac.nz/~ian/nwen304/stops.xml";
	public static final String ROUTES_URL = "http://homepages.ecs.vuw.ac.nz/~ian/nwen304/routes.xml";
	public static final String TRIPS_URL = "http://homepages.ecs.vuw.ac.nz/~ian/nwen304/trips.xml";	
	public static final String STOP_TIMES_URL_SHORT = "http://homepages.ecs.vuw.ac.nz/~ian/nwen304/simpletimetable/stop_times.xml";
	
	private FeedParser parser;
	private Activity activity;
	
	public DataLoader(Activity activity){
		this.activity = activity;
		this.parser = new XMLPullFeedParser();
	}
	
	public List<StopTime> loadStopTimes(){
    	
	   	List<StopTime> stopTimes = parser.parseStopTimes(STOP_TIMES_URL_SHORT);

    	//String xml = writeXml();
    	Log.d("TimetableReader", "stop times read - " + stopTimes.size());
    	for(StopTime s: stopTimes){
    		Log.d("TimetableReader", s.toString());
    	}
    	
    	return stopTimes;
    }
	
	public List<Trip> loadTrips(){

    	List<Trip> trips = parser.parseTrips(TRIPS_URL);

    	//String xml = writeXml();
	    Log.d("TimetableReader", "trips read - " + trips.size());
	    for(Trip t: trips){
	    	Log.d("TimetableReader", t.toString());
	    }
	    
	    return trips;
	}
	
	public List<Route> loadRoutes(){

	   	List<Route> routes = parser.parseRoutes(ROUTES_URL);
	    	//String xml = writeXml();
    	Log.d("TimetableReader", "routes read - " + routes.size());
    	for(Route r: routes){   		
    		Log.d("TimetableReader", r.toString());
    	}
    	
    	return routes;	
	}
	
	public Map<Integer, Stop> loadStops(){
		String FILENAME = "stops.xml";	
		Map<Integer, Stop> stops = null;

 		try{
	    	try{	
	    		FileInputStream fis = activity.openFileInput(FILENAME);
	    		Log.d("TimetableReader", "Reading stops from memory");
	    		stops = parser.parseStops(fis);  
	    		fis.close();
	    		Log.d("TimetableReader", "Loaded stops - " + stops.size());
	    		
	    		return stops;
	    	} 
	    	catch (FileNotFoundException e){
	    		try {
		    		Log.d("TimetableReader", "Reading stops from online");
		    		
		       		// load stops online
		        	stops = parser.parseStops(STOPS_URL);
		
		        	// convert to xml
		        	List<Stop> stopList = new ArrayList<Stop>();
		        	stopList.addAll(stops.values());
		        	String xml = writeStopsToXml(stopList); 				
		       		FileOutputStream fos;
				
		       		// save to internal memory
					fos = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
					fos.write(xml.getBytes());   		
		       		fos.close();	
		       		
		       		return stops;
		       		
				} catch (FileNotFoundException e1) {
					throw new RuntimeException(e1);
				}        		
	    	}
 		} catch(IOException e3){
    		throw new RuntimeException(e3);
    	}	
	}
	

	private String writeStopsToXml(List<Stop> stops){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", BaseFeedParser.DOCUMENT);		
			for (Stop st: stops){
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
