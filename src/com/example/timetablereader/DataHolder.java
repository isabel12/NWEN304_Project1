package com.example.timetablereader;

import java.util.List;
import java.util.Map;

import android.app.Activity;

import com.example.timetablereader.Objects.Route;
import com.example.timetablereader.Objects.Stop;
import com.example.timetablereader.Objects.StopTime;
import com.example.timetablereader.Objects.Trip;
import com.example.timetablereader.Objects.ObjectUpdates.RouteUpdate;
import com.example.timetablereader.Objects.ObjectUpdates.Update;
import com.example.timetablereader.Objects.ObjectUpdates.VersionedCollection;
import com.example.timetablereader.XMLParsing.DataLoader;

public class DataHolder {

	public static final String STOPS_FILENAME = "stops.xml";
	public static final String STOPTIMES_FILENAME = "stop_times.xml";
	public static final String TRIPS_FILENAME = "trips.xml";
	public static final String ROUTES_FILENAME = "routes.xml";


	private static DataLoader dataloader;

	private static VersionedCollection<List<Route>> routes;
	private static VersionedCollection<List<Trip>> trips;
	private static VersionedCollection<Map<Integer, Stop>> stops;
	private static VersionedCollection<Map<Integer, List<StopTime>>> stoptimes;
	
	private static Map<String, Integer> onlineFileVersions;
	private static List<RouteUpdate> routeUpdates;


	public static void initialise(Activity activity, boolean isConnectedToInternet){
		dataloader = new DataLoader(activity);

		routes = dataloader.loadRoutes();
		Log.write("routes is version " + routes.getVersion());
		
		trips = dataloader.loadTrips();
		Log.write("trips is version " + trips.getVersion());
		
		stops = dataloader.loadStops();
		Log.write("stops is version " + stops.getVersion());
		
		//stoptimes = dataloader.loadStopTimes();
		//Log.write("stoptimes is version " + stoptimes.getVersion());
		
		if (isConnectedToInternet){
			checkForFileUpdates();		
		}
	}

	private static void checkForFileUpdates(){

		// get online versions
		onlineFileVersions = dataloader.loadFileVersions();
		Log.write("onlineFileVersions = " + onlineFileVersions.size());
		Log.write(onlineFileVersions.entrySet().toString());

		// routes
		int routesCurrVer = routes.getVersion();
		int routesOnlineVer = onlineFileVersions.get(ROUTES_FILENAME);	
		
		// if (routesCurrVer < routesOnlineVer){
		if (true){

			// get updates
			routeUpdates = dataloader.loadRouteUpdates(routes.getVersion());
			Log.write("loaded route updates: " + routeUpdates.size());

			// apply updates
			for(RouteUpdate r: routeUpdates){
				r.applyUpdate(routes.getCollection());
			}
			
			// save new version to internal memory
			dataloader.writeRoutesToXml(routes.getCollection(), routesOnlineVer);
		}
	}


	public static List<Route> getRoutes(){
		return routes.getCollection();
	}

	public static List<Trip> getTrips(){
		return trips.getCollection();
	}

	public static Map<Integer, Stop> getStops(){
		return stops.getCollection();
	}

	public static Map<Integer, List<StopTime>> getStopTimes(){
		return stoptimes.getCollection();
	}

	public static int getFileVersion(String filename){
		return onlineFileVersions.get(filename);
	}


}
