package com.example.timetablereader.XMLParsing;

import java.util.List;

import com.example.timetablereader.Objects.Route;
import com.example.timetablereader.Objects.Stop;
import com.example.timetablereader.Objects.StopTime;
import com.example.timetablereader.Objects.Trip;

public interface FeedParser {

	List<StopTime> parseStopTimes();
	
	List<Stop> parseStops();
	
	List<Trip> parseTrips();
	
	List<Route> parseRoutes();

}
