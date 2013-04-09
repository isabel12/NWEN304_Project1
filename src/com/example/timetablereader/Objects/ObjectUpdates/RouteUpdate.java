package com.example.timetablereader.Objects.ObjectUpdates;

import java.util.List;
import java.util.Map;

import android.util.Log;

import com.example.timetablereader.Objects.Route;

public class RouteUpdate extends Update<Route, List<Route>> {

	public String name;
	public String agency;


	public RouteUpdate(int id, UpdateType type) {
		super(id, type);
	}

	@Override
	protected void applyEdit(List<Route> collection) {
		// get the route out of the collection
		Route route = null;
		for(Route r: collection){
			if (r.getRouteId() == id){
				route = collection.get(id);
			}
		}

		// edit the fields as necessary
		if (name != null){
			route.setName(name);
			Log.d("TimetableReader", "edited route name: " + name);
		}

		if (agency != null){
			route.setAgency(agency);
			Log.d("TimetableReader", "edited route agency: " + agency);
		}
	}

	@Override
	protected void applyDelete(List<Route> collection) {
		// get the stop out of the collection
		Route route = null;
		for(Route r: collection){
			if (r.getRouteId() == id){
				route = r;
			}
		}

		collection.remove(route);
		Log.d("TimetableReader", "removed route: " + route);
	}

	@Override
	protected void applyAdd(List<Route> collection) {
		// get the stop out of the collection
		Route route = new Route();
		route.setRouteId(id);
		route.setName(name);
		route.setAgency(agency);
		collection.add(route);
		Log.d("TimetableReader", "added new route: " + route);

	}

	@Override
	public String toString() {
		return "RouteUpdate [name=" + name + ", agency=" + agency + ", id="
				+ id + ", type=" + type + "]";
	}



}
