package com.example.timetablereader.XMLParsing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
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

	public static final String STOP_TIMES_URL = "http://homepages.ecs.vuw.ac.nz/~broomeisab/stop_times.xml";
	public static final String STOPS_URL = "http://homepages.ecs.vuw.ac.nz/~broomeisab/stops.xml";
	public static final String ROUTES_URL = "http://homepages.ecs.vuw.ac.nz/~broomeisab/routes.xml";
	public static final String TRIPS_URL = "http://homepages.ecs.vuw.ac.nz/~broomeisab/trips.xml";	
	
	private FeedLoader feedLoader;
	private IFeedParser parser;
	private Activity activity;
	
	public DataLoader(Activity activity){
		this.activity = activity;
		this.parser = new XMLPullFeedParser();
		this.feedLoader = new FeedLoader();
	}
	
	// return them sorted by TripId, and then by stop sequence.
	public Map<Integer, List<StopTime>> loadStopTimes()  {		
		try{		
			String FILENAME = "stopTimes.xml";	
			Map<Integer, List<StopTime>> stopTimes = null;
	
			try{	
				FileInputStream fis = activity.openFileInput(FILENAME);
				Log.d("TimetableReader", "Reading stops from memory");
				stopTimes = parser.parseStopTimes(fis);  
				fis.close();
				Log.d("TimetableReader", "Loaded stopTimes - " + stopTimes.size());
	
				return stopTimes;
			} 
			catch (FileNotFoundException e){
	
				Log.d("TimetableReader", "Reading stops from online");
	
				// load from online and sort
				InputStream input = feedLoader.getFeedInputStream(STOP_TIMES_URL);
				stopTimes = parser.parseStopTimes(input);
	
				Log.d("TimetableReader", "stop times read online for this many trips - " + stopTimes.size());

	
				// convert to xml
				List<StopTime> stopTimesList = new ArrayList<StopTime>();
				for(List<StopTime> list: stopTimes.values()){
					stopTimesList.addAll(list);
				}					
				String xml = writeStopTimesToXML(stopTimesList); 				
				FileOutputStream fos;
	
				// save to internal memory
				fos = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
				fos.write(xml.getBytes());   		
				fos.close();	
	
				return stopTimes;
			}
		} catch (IOException e){
			throw new RuntimeException(e);
		}

	}
	
	public List<Trip> loadTrips(){
		String FILENAME = "trips.xml";	
		List<Trip> trips = null;
		
		try{
			try{	
				FileInputStream fis = activity.openFileInput(FILENAME);
				Log.d("TimetableReader", "Reading trips from memory");
				trips = parser.parseTrips(fis);  
				fis.close();
				Log.d("TimetableReader", "Loaded trips - " + trips.size());

				return trips;
			} 
			catch (FileNotFoundException e){

				Log.d("TimetableReader", "Reading trips from online");

				// load from online 
				InputStream input = feedLoader.getFeedInputStream(TRIPS_URL);
				trips = parser.parseTrips(input);

				// log
				Log.d("TimetableReader", "Loaded trips - " + trips.size());
				for(Trip t: trips){
					Log.d("TimetableReader", t.toString());
				}  

				// convert to xml
				String xml = writeTripsToXml(trips); 				
				FileOutputStream fos;

				// save to internal memory
				fos = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
				fos.write(xml.getBytes());   		
				fos.close();	

				return trips;
			}
		} catch(IOException e){
			throw new RuntimeException(e);
		}	
	}
	
	public List<Route> loadRoutes(){

		String FILENAME = "routes.xml";	
		List<Route> routes = null;
		
		try{
			try{	
				FileInputStream fis = activity.openFileInput(FILENAME);
				Log.d("TimetableReader", "Reading routes from memory");
				routes = parser.parseRoutes(fis);  
				fis.close();
				Log.d("TimetableReader", "Loaded routes- " + routes.size());

				return routes;
			} 
			catch (FileNotFoundException e){

				Log.d("TimetableReader", "Reading routes from online");

				// load from online 
				InputStream input = feedLoader.getFeedInputStream(ROUTES_URL);
				routes = parser.parseRoutes(input);

				// log
				Log.d("TimetableReader", "Loaded routes - " + routes.size());
				for(Route r: routes){
					Log.d("TimetableReader", r.toString());
				}  

				// convert to xml
				String xml = writeRoutesToXml(routes); 				
				FileOutputStream fos;

				// save to internal memory
				fos = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
				fos.write(xml.getBytes());   		
				fos.close();	

				return routes;
			}
		} catch(IOException e){
			throw new RuntimeException(e);
		}	
	}
	
	
	public Map<Integer, Stop> loadStops(){
		String FILENAME = "stops.xml";	
		Map<Integer, Stop> stops = null;
		InputStream inputStream = null;

 		try{
	    	try{	
	    		inputStream = activity.openFileInput(FILENAME);
	    		Log.d("TimetableReader", "Reading stops from memory");
	    		stops = parser.parseStops(inputStream);  
	    		inputStream.close();
	    		Log.d("TimetableReader", "Loaded stops - " + stops.size());
	    		
	    		return stops;
	    	} 
	    	catch (FileNotFoundException e){
	    		try {
		    		Log.d("TimetableReader", "Reading stops from online");
		    		
		       		// load stops online
		    		inputStream = feedLoader.getFeedInputStream(STOPS_URL);
		        	stops = parser.parseStops(inputStream);
		        			
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
	
	private String writeRoutesToXml(List<Route> routes){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", BaseFeedParser.DOCUMENT);		
			for (Route r: routes){
				serializer.startTag("", BaseFeedParser.RECORD);
				
				serializer.startTag("", BaseFeedParser.ROUTE_ID);
				serializer.text("" + r.getRouteId());
				serializer.endTag("", BaseFeedParser.ROUTE_ID);
				
				serializer.startTag("", BaseFeedParser.AGENCY_ID);
				serializer.text(r.getAgency());
				serializer.endTag("", BaseFeedParser.AGENCY_ID);
				
				serializer.startTag("", BaseFeedParser.ROUTE_NAME);
				serializer.text("" + r.getName());
				serializer.endTag("", BaseFeedParser.ROUTE_NAME);
				
				serializer.endTag("", BaseFeedParser.RECORD);
			}
			serializer.endTag("", BaseFeedParser.DOCUMENT);
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private String writeTripsToXml(List<Trip> trips){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", BaseFeedParser.DOCUMENT);		
			for (Trip t: trips){
				serializer.startTag("", BaseFeedParser.RECORD);
				
				serializer.startTag("", BaseFeedParser.ROUTE_ID);
				serializer.text("" + t.getRouteId());
				serializer.endTag("", BaseFeedParser.ROUTE_ID);
				
				serializer.startTag("", BaseFeedParser.TRIP_ID);
				serializer.text(t.getTripId() + "");
				serializer.endTag("", BaseFeedParser.TRIP_ID);
				
				serializer.startTag("", BaseFeedParser.DIRECTION_ID);
				serializer.text(t.isOutbound()? "0":"1");
				serializer.endTag("", BaseFeedParser.DIRECTION_ID);
				
				serializer.endTag("", BaseFeedParser.RECORD);
			}
			serializer.endTag("", BaseFeedParser.DOCUMENT);
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
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
	
	private String writeStopTimesToXML(List<StopTime> stopTimes){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", BaseFeedParser.DOCUMENT);		
			for (StopTime st: stopTimes){
				serializer.startTag("", BaseFeedParser.RECORD);
				
				serializer.startTag("", BaseFeedParser.TRIP_ID);
				serializer.text("" + st.getTripId());
				serializer.endTag("", BaseFeedParser.TRIP_ID);
				
				serializer.startTag("", BaseFeedParser.ARRIVAL_TIME);
				serializer.text("" + st.getArrivalTime());
				serializer.endTag("", BaseFeedParser.ARRIVAL_TIME);
				
				serializer.startTag("", BaseFeedParser.DEPARTURE_TIME);
				serializer.text("" + st.getDepartureTime());
				serializer.endTag("", BaseFeedParser.DEPARTURE_TIME);
				
				serializer.startTag("", BaseFeedParser.STOP_ID);
				serializer.text("" + st.getStopId());
				serializer.endTag("", BaseFeedParser.STOP_ID);
				
				serializer.startTag("", BaseFeedParser.STOP_SEQUENCE);
				serializer.text("" + st.getStopSequence());
				serializer.endTag("", BaseFeedParser.STOP_SEQUENCE);
				
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
