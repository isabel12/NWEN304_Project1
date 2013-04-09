package com.example.timetablereader.Objects.ObjectUpdates;

import java.util.Map;

import android.util.Log;

import com.example.timetablereader.Objects.Stop;

public class StopUpdate extends Update<Stop, Map<Integer, Stop>>{

	public String name;
	public double stopLat;
	public double stopLon;

	public StopUpdate(UpdateType type, int id){
		super(id, type);
	}


	@Override
	protected void applyEdit(Map<Integer, Stop> collection) {
		// get the stop out of the collection
		Stop stop = collection.get(id);

		// edit the fields as necessary
		if (name != null){
			stop.setStopName(name);
			Log.d("TimetableReader", "edited stop name: " + stop);
		}

		if (stopLat != Double.NaN){
			stop.setStopLat(stopLat);
			Log.d("TimetableReader", "edited stop lat: " + stop);
		}

		if(stopLon != Double.NaN){
			stop.setStopLon(stopLon);
			Log.d("TimetableReader", "edited stop lon: " + stop);
		}

	}

	@Override
	protected void applyDelete(Map<Integer, Stop> collection) {
		// get the stop out of the collection
		Stop removed = collection.remove(id);
		Log.d("TimetableReader", "removed stop: " + removed);
	}

	@Override
	protected void applyAdd(Map<Integer, Stop> collection) {
		// get the stop out of the collection
		Stop stop = new Stop();
		stop.setStopId(id);
		stop.setStopName(name);
		stop.setStopLat(stopLat);
		stop.setStopLon(stopLon);


		collection.put(id, stop);
		Log.d("TimetableReader", "added new stop: " + stop);

	}



}
