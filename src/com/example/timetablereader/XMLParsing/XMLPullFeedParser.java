package com.example.timetablereader.XMLParsing;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParser;

import com.example.timetablereader.Objects.Route;
import com.example.timetablereader.Objects.Stop;
import com.example.timetablereader.Objects.StopTime;
import com.example.timetablereader.Objects.Trip;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

public class XMLPullFeedParser extends BaseFeedParser {


	public List<StopTime> parseStopTimes(String feedUrl) {	
		
		
		try {
			AsyncTask<InputStream, Void, List<StopTime>> parseTask = new ParseStopTimesTask().execute(getFeedInputStream(feedUrl));
			return parseTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	public List<StopTime> parseStopTimes(InputStream inputStream){
		try {
			AsyncTask<InputStream, Void, List<StopTime>> parseTask = new ParseStopTimesTask().execute(inputStream);
			return parseTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Map<Integer, Stop> parseStops(String feedUrl) {
		
		try {
			AsyncTask<InputStream, Void, Map<Integer, Stop>> parseTask = new ParseStopsTask().execute(getFeedInputStream(feedUrl));
			return parseTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	public Map<Integer, Stop> parseStops(InputStream inputStream){
		try {
			AsyncTask<InputStream, Void, Map<Integer, Stop>> parseTask = new ParseStopsTask().execute(inputStream);
			return parseTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	

	@Override
	public List<Trip> parseTrips(String feedUrl) {
		
		try {
			AsyncTask<InputStream, Void, List<Trip>> parseTask = new ParseTripsTask().execute(getFeedInputStream(feedUrl));
			return parseTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Route> parseRoutes(String feedUrl) {
		
		try {
			AsyncTask<InputStream, Void, List<Route>> parseTask = new ParseRoutesTask().execute(getFeedInputStream(feedUrl));
			return parseTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}	
	
	@Override
	public List<Trip> parseTrips(InputStream inputStream) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Route> parseRoutes(InputStream inputStream) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	private class ParseStopTimesTask extends AsyncTask<InputStream, Void, List<StopTime>> {

		@Override
		protected List<StopTime> doInBackground(InputStream... inputStreams) {
			// list of stop times
			List<StopTime> stopTimes = null;

			XmlPullParser parser = Xml.newPullParser();
			try {
				// auto-detect the encoding from the stream
				parser.setInput(inputStreams[0], null);
				int eventType = parser.getEventType();
				StopTime currentStopTime = null;
				boolean done = false;
				while (eventType != XmlPullParser.END_DOCUMENT && !done){
					String name = null;
					switch (eventType){
						case XmlPullParser.START_DOCUMENT:
							stopTimes = new ArrayList<StopTime>();
							break;
						case XmlPullParser.START_TAG:					
							name = parser.getName();
							if (name.equalsIgnoreCase(RECORD)){
								currentStopTime = new StopTime();
							} else if (currentStopTime != null){
								if (name.equalsIgnoreCase(TRIP_ID)){
									currentStopTime.setTripId(Integer.parseInt(parser.nextText()));
								} else if (name.equalsIgnoreCase(ARRIVAL_TIME)){
									currentStopTime.setArrivalTime(parser.nextText());
								} else if (name.equalsIgnoreCase(DEPARTURE_TIME)){
									currentStopTime.setDepartureTime(parser.nextText());
								}else if (name.equalsIgnoreCase(STOP_ID)){
									currentStopTime.setStopId(Integer.parseInt(parser.nextText()));
								}else if (name.equalsIgnoreCase(STOP_SEQUENCE)){
									currentStopTime.setStopSequence(Integer.parseInt(parser.nextText()));
								}
							}						
							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(RECORD) && currentStopTime != null){
								stopTimes.add(currentStopTime);
							} else if (name.equalsIgnoreCase(DOCUMENT)){
								done = true;
							}
							break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			// return the stop times
			return stopTimes;			
		}
	}
	
	
	private class ParseStopsTask extends AsyncTask<InputStream, Void, Map<Integer, Stop>> {

		@Override
		protected Map<Integer, Stop> doInBackground(InputStream... inputStreams) {
			// list of stop times
			Map<Integer, Stop> stops = null;

			XmlPullParser parser = Xml.newPullParser();
			try {
				// auto-detect the encoding from the stream
				parser.setInput(inputStreams[0], null);
				int eventType = parser.getEventType();
				
				
				Stop currentStop = null;
				boolean done = false;
				while (eventType != XmlPullParser.END_DOCUMENT && !done){
					String name = null;
					switch (eventType){
						case XmlPullParser.START_DOCUMENT:
							stops = new HashMap<Integer, Stop>();
							break;
						case XmlPullParser.START_TAG:					
							name = parser.getName();
							if (name.equalsIgnoreCase(RECORD)){
								currentStop = new Stop();
							} else if (currentStop != null){
								if (name.equalsIgnoreCase(STOP_ID)){									
									currentStop.setStopId(Integer.parseInt(parser.nextText()));
								} else if (name.equalsIgnoreCase(STOP_NAME)){
									currentStop.setStopName(parser.nextText());
								} else if (name.equalsIgnoreCase(STOP_LAT)){
									currentStop.setStopLat(Double.parseDouble(parser.nextText()));
								} else if (name.equalsIgnoreCase(STOP_LON)){
									currentStop.setStopLon(Double.parseDouble(parser.nextText()));
								} 
							}
							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(RECORD) && currentStop != null){
								stops.put(currentStop.getStopId(), currentStop);
							} else if (name.equalsIgnoreCase(DOCUMENT)){
								done = true;
							}
							break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			// return the stops
			return stops;			
		}
	}
	
	
	private class ParseTripsTask extends AsyncTask<InputStream, Void, List<Trip>> {

		@Override
		protected List<Trip> doInBackground(InputStream... inputStreams) {
			// list of trips
			List<Trip> trips = null;

			XmlPullParser parser = Xml.newPullParser();
			try {
				// auto-detect the encoding from the stream
				parser.setInput(inputStreams[0], null);
				int eventType = parser.getEventType();			
				Trip currentTrip = null;
				boolean done = false;
				while (eventType != XmlPullParser.END_DOCUMENT && !done){
					String name = null;
					switch (eventType){
						case XmlPullParser.START_DOCUMENT:
							trips = new ArrayList<Trip>();
							break;
						case XmlPullParser.START_TAG:					
							name = parser.getName();
							if (name.equalsIgnoreCase(RECORD)){
								currentTrip = new Trip();
							} else if (currentTrip != null){
								if (name.equalsIgnoreCase(ROUTE_ID)){									
									currentTrip.setRouteId(Integer.parseInt(parser.nextText()));
								} else if (name.equalsIgnoreCase(TRIP_ID)){
									currentTrip.setTripId(Integer.parseInt(parser.nextText()));
								} else if (name.equalsIgnoreCase(DIRECTION_ID)){
									String dirId = parser.nextText().trim();
									if (dirId.equals("0")){
										currentTrip.setOutbound(true);
									}else
										currentTrip.setOutbound(false);
								} 		
							}
							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(RECORD) && currentTrip != null){
								trips.add(currentTrip);
							} else if (name.equalsIgnoreCase(DOCUMENT)){
								done = true;
							}
							break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			// return the trips
			return trips;			
		}
	}
	
	
	private class ParseRoutesTask extends AsyncTask<InputStream, Void, List<Route>> {

		@Override
		protected List<Route> doInBackground(InputStream... inputStreams) {
			// list of trips
			List<Route> routes = null;

			XmlPullParser parser = Xml.newPullParser();
			try {
				// auto-detect the encoding from the stream
				parser.setInput(inputStreams[0], null);
				int eventType = parser.getEventType();			
				Route currentRoute = null;
				boolean done = false;
				while (eventType != XmlPullParser.END_DOCUMENT && !done){
					String name = null;
					switch (eventType){
						case XmlPullParser.START_DOCUMENT:
							routes = new ArrayList<Route>();
							break;
						case XmlPullParser.START_TAG:					
							name = parser.getName();
							if (name.equalsIgnoreCase(RECORD)){
								currentRoute = new Route();
							} else if (currentRoute != null){
								if (name.equalsIgnoreCase(ROUTE_ID)){									
									currentRoute.setRouteId(Integer.parseInt(parser.nextText()));
								} else if (name.equalsIgnoreCase(AGENCY_ID)){
									currentRoute.setAgency(parser.nextText());
								} else if (name.equalsIgnoreCase(ROUTE_NAME)){
									currentRoute.setName(parser.nextText());	
								}
							}
							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(RECORD) && currentRoute != null){
								routes.add(currentRoute);
							} else if (name.equalsIgnoreCase(DOCUMENT)){
								done = true;
							}
							break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			// return the routes
			return routes;			
		}
	}



	
}