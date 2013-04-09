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
import com.example.timetablereader.XMLParsing.DataLoader;

public class DataHolder {

	public static final String STOPS_FILENAME = "stops.xml";
	public static final String STOPTIMES_FILENAME = "stop_times.xml";
	public static final String TRIPS_FILENAME = "trips.xml";
	public static final String ROUTES_FILENAME = "routes.xml";




	private static List<Route> routes;
	private static List<Trip> trips;
	private static Map<Integer, Stop> stops;
	private static Map<Integer, List<StopTime>> stoptimes;
	private static Map<String, Integer> fileVersions;

	private static List<RouteUpdate> routeUpdates;


	public static void initialise(Activity activity){
		DataLoader dataloader = new DataLoader(activity);

		routes = dataloader.loadRoutes();
		trips = dataloader.loadTrips();
		stops = dataloader.loadStops();
		//stoptimes = dataloader.loadStopTimes();
		fileVersions = dataloader.loadFileVersions();

		// update
		routeUpdates = dataloader.loadRouteUpdates(1);
		Log.write("loaded route updates: " + routeUpdates.size());

		for(RouteUpdate r: routeUpdates){
			r.applyUpdate(routes);
		}
	}

	private static void applyUpdates(List<RouteUpdate> routeUpdates){

	}


	public static List<Route> getRoutes(){
		return routes;
	}

	public static List<Trip> getTrips(){
		return trips;
	}

	public static Map<Integer, Stop> getStops(){
		return stops;
	}

	public static Map<Integer, List<StopTime>> getStopTimes(){
		return stoptimes;
	}

	public static int getFileVersion(String filename){
		return fileVersions.get(filename);
	}


}
