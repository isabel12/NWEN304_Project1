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
import com.example.timetablereader.Objects.ObjectUpdates.RouteUpdate;
import com.example.timetablereader.Objects.ObjectUpdates.Update;
import com.example.timetablereader.Objects.ObjectUpdates.UpdateType;
import com.example.timetablereader.Objects.ObjectUpdates.VersionedCollection;

import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;

public class XMLPullFeedParser implements IFeedParser {

	// names of the XML tags
	public static final String DOCUMENT = "document";
	public static final String RECORD = "record";

	public static final String ROUTE_ID = "route_id";
	public static final String TRIP_ID = "trip_id";
	public static final String DIRECTION_ID = "direction_id";
	public static final String ARRIVAL_TIME = "arrival_time";
	public static final String DEPARTURE_TIME = "departure_time";
	public static final String STOP_ID = "stop_id";
	public static final String STOP_SEQUENCE = "stop_sequence";
	public static final String STOP_NAME = "stop_name";
	public static final String STOP_LAT = "stop_lat";
	public static final String STOP_LON = "stop_lon";
	public static final String AGENCY_ID = "agency_id";
	public static final String ROUTE_NAME = "route_long_name";

	public static final String FILENAME = "filename";
	public static final String CURRENT_VERSION = "current_version";
	public static final String VERSION = "version";

	public static final String TYPE = "type";
	public static final String EDIT = "edit";
	public static final String DELETE = "delete";
	public static final String ADD = "add";




	@Override
	public VersionedCollection<Map<Integer, List<StopTime>>> parseStopTimes(InputStream inputStream){
		try {
			AsyncTask<InputStream, Void, VersionedCollection<Map<Integer, List<StopTime>>>> parseTask = new ParseStopTimesTask().execute(inputStream);
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
	public VersionedCollection<Map<Integer, Stop>> parseStops(InputStream inputStream){
		try {
			AsyncTask<InputStream, Void, VersionedCollection<Map<Integer, Stop>>> parseTask = new ParseStopsTask().execute(inputStream);
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
	public VersionedCollection<List<Trip>> parseTrips(InputStream inputStream) {
		try {
			AsyncTask<InputStream, Void, VersionedCollection<List<Trip>>> parseTask = new ParseTripsTask().execute(inputStream);
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
	public VersionedCollection<List<Route>> parseRoutes(InputStream inputStream) {
		try {
			AsyncTask<InputStream, Void, VersionedCollection<List<Route>>> parseTask = new ParseRoutesTask().execute(inputStream);
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

	public List<RouteUpdate> parseRouteUpdates(InputStream inputStream, int currentVersion){
		try {
			AsyncTask<InputStream, Void, List<RouteUpdate>> parseTask = new ParseRouteUpdatesTask(currentVersion).execute(inputStream);
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
	 * Parses route updates.
	 * @author broomeisab
	 *
	 */
	private class ParseRouteUpdatesTask extends AsyncTask<InputStream, Void, List<RouteUpdate>> {
		private int currentVersion;

		ParseRouteUpdatesTask(int currentVersion){
			this.currentVersion = currentVersion;
		}


		@Override
		protected List<RouteUpdate> doInBackground(
				InputStream... params) {
			// list of stop times
			ArrayList<RouteUpdate> updates = null;

			XmlPullParser parser = Xml.newPullParser();
			try {
				// auto-detect the encoding from the stream
				parser.setInput(params[0], null);
				int eventType = parser.getEventType();

				boolean done = false;
				boolean record = false;

				// fields to make the Update
				RouteUpdate update = null;
				int id = -1;
				UpdateType updateType = null;
				String agency = null;
				String routeName = null;


				while (eventType != XmlPullParser.END_DOCUMENT && !done){
					switch (eventType){
					case XmlPullParser.START_DOCUMENT:
						updates = new ArrayList<RouteUpdate>();
						break;
					case XmlPullParser.START_TAG:
						String name = parser.getName();
						if (name.equalsIgnoreCase(RECORD)){
							record = true;
							update = null;
							id = -1;
							updateType = null;
							agency = null;
							routeName = null;
						} else if (record == true){
							// parsing update values
							if (name.equalsIgnoreCase(VERSION)){
								int version = Integer.parseInt(parser.nextText());
								if(version == currentVersion){
									done = true;
									break;
								}
							} else if (name.equalsIgnoreCase(TYPE)){
								String type = parser.nextText();
								if(type.equals(EDIT))
									updateType = UpdateType.Edit;

								else if (type.equals(DELETE))
									updateType = UpdateType.Delete;

								else if (type.equals(ADD))
									updateType = UpdateType.Add;
							}

							// parsing object specific values
							else if (name.equalsIgnoreCase(ROUTE_ID)){
								id = Integer.parseInt(parser.nextText());
							} else if (name.equalsIgnoreCase(AGENCY_ID)){
								agency = parser.nextText();
							} else if (name.equalsIgnoreCase(ROUTE_NAME)){
								routeName = parser.nextText();
							}

						}
						break;
					case XmlPullParser.END_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase(RECORD) && record){
							// make update
							update = new RouteUpdate(id, updateType);
							update.agency = agency;
							update.name = routeName;

							// add update
							updates.add(update);

							// reset record
							record = false;

							Log.d("", "loaded update: " + update);

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

			// return the version numbers
			return updates;
		}
	}



	/**
	 * This returns the current version numbers of all online files.
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
				// fields to save
				String filename = null;
				int version = 1;
				
				while (eventType != XmlPullParser.END_DOCUMENT && !done){


					switch (eventType){
					case XmlPullParser.START_DOCUMENT:
						versionNumbers = new HashMap<String, Integer>();				
						break;
					case XmlPullParser.START_TAG:
						String name = parser.getName();
						if (name.equalsIgnoreCase(RECORD)){
							filename = null;
							version = 1;
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
							Log.d("", "put in " + filename + version);
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

			// return the version numbers
			return versionNumbers;
		}
	}



	private class ParseStopTimesTask extends AsyncTask<InputStream, Void, VersionedCollection<Map<Integer, List<StopTime>>>> {

		@Override
		protected VersionedCollection<Map<Integer, List<StopTime>>> doInBackground(InputStream... inputStreams) {
			// list of stop times
			Map<Integer, List<StopTime>> stopTimeMap = null;
			int version = 1; // default is 1

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
						
						if(name.equalsIgnoreCase(VERSION)){
							version = Integer.parseInt(parser.nextText());
						} 
						
						else if (name.equalsIgnoreCase(RECORD)){
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
			return new VersionedCollection<Map<Integer, List<StopTime>>>(stopTimeMap, version);
		}
	}


	private class ParseStopsTask extends AsyncTask<InputStream, Void, VersionedCollection<Map<Integer, Stop>>> {

		@Override
		protected VersionedCollection<Map<Integer, Stop>> doInBackground(InputStream... inputStreams) {
			// list of stop times
			Map<Integer, Stop> stops = null;
			int version = 1; // default

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
							
							if(name.equals(VERSION)){
								version = Integer.parseInt(parser.nextText());
							}
							else if (name.equalsIgnoreCase(RECORD)){
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
			return new VersionedCollection<Map<Integer, Stop>>(stops, version);
		}
	}


	private class ParseTripsTask extends AsyncTask<InputStream, Void, VersionedCollection<List<Trip>>> {

		@Override
		protected VersionedCollection<List<Trip>> doInBackground(InputStream... inputStreams) {
			// list of trips
			List<Trip> trips = null;
			int version = 1; // default

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
							
							if(name.equalsIgnoreCase(VERSION)){
								version = Integer.parseInt(parser.nextText());
							}
							else if (name.equalsIgnoreCase(RECORD)){
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
			return new VersionedCollection<List<Trip>>(trips, version);
		}
	}


	private class ParseRoutesTask extends AsyncTask<InputStream, Void, VersionedCollection<List<Route>>> {

		@Override
		protected VersionedCollection<List<Route>> doInBackground(InputStream... inputStreams) {
			// list of trips
			List<Route> routes = null;
			int version = 1; //default

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
							if(name.equalsIgnoreCase(VERSION)){
								version = Integer.parseInt(parser.nextText());
							}
							else if (name.equalsIgnoreCase(RECORD)){
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
			return new VersionedCollection<List<Route>>(routes, version);
		}
	}




}