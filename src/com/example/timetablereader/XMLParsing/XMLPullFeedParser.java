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
import android.util.SparseArray;
import android.util.Xml;

public class XMLPullFeedParser extends BaseFeedParser {

	@Override
	public Map<Integer, List<StopTime>> parseStopTimes(InputStream inputStream){
		try {
			AsyncTask<InputStream, Void, Map<Integer, List<StopTime>>> parseTask = new ParseStopTimesTask().execute(inputStream);
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
	public List<Trip> parseTrips(InputStream inputStream) {
		try {
			AsyncTask<InputStream, Void, List<Trip>> parseTask = new ParseTripsTask().execute(inputStream);
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
	public List<Route> parseRoutes(InputStream inputStream) {
		try {
			AsyncTask<InputStream, Void, List<Route>> parseTask = new ParseRoutesTask().execute(inputStream);
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
	public Map<String, Integer> parseFileVersionNumbers(InputStream inputStream){
		try {
			AsyncTask<InputStream, Void, Map<String, Integer>> parseTask = new ParseVersionsTask().execute(inputStream);
			return parseTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}


	}


	/**
	 * This returns the current version numbers of all files.
	 * @author broomeisab
	 *
	 */
	private class ParseVersionsTask extends AsyncTask<InputStream, Void, Map<String, Integer>> {
		@Override
		protected Map<String, Integer> doInBackground(InputStream... inputStreams) {

			// list of stop times
						Map<String, Integer> versionNumbers = null;

						XmlPullParser parser = Xml.newPullParser();
						try {
							// auto-detect the encoding from the stream
							parser.setInput(inputStreams[0], null);
							int eventType = parser.getEventType();

							boolean done = false;

							boolean record = false;
							while (eventType != XmlPullParser.END_DOCUMENT && !done){
								// fields to save
								String filename = null;
								int version = 0;

								switch (eventType){
									case XmlPullParser.START_DOCUMENT:
										versionNumbers = new HashMap<String, Integer>();
										break;
									case XmlPullParser.START_TAG:
										String name = parser.getName();
										if (name.equalsIgnoreCase(RECORD)){
											record = true;
										} else if (record){
											if (name.equalsIgnoreCase(FILENAME)){
												filename = parser.nextText();
											} else if (name.equalsIgnoreCase(CURRENT_VERSION)){
												version = Integer.parseInt(parser.nextText());
											}
										}
										break;
									case XmlPullParser.END_TAG:
										name = parser.getName();
										if (name.equalsIgnoreCase(RECORD) && record){

											versionNumbers.put(filename, version);
											record = false;

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
						return versionNumbers;


		}



	}



	private class ParseStopTimesTask extends AsyncTask<InputStream, Void, Map<Integer, List<StopTime>>> {

		@Override
		protected Map<Integer, List<StopTime>> doInBackground(InputStream... inputStreams) {
			// list of stop times
			Map<Integer, List<StopTime>> stopTimeMap = null;

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
							stopTimeMap = new HashMap<Integer, List<StopTime>>();
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
								// get the list associated with the trip id
								int key = currentStopTime.getTripId();

								List<StopTime> list = stopTimeMap.get(key);
								if (list == null){
									list = new ArrayList<StopTime>();
									stopTimeMap.put(key, list);
								}

								list.add(currentStopTime);

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
			return stopTimeMap;
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