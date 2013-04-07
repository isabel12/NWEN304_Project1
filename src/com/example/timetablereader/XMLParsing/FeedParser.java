package com.example.timetablereader.XMLParsing;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.example.timetablereader.Objects.Route;
import com.example.timetablereader.Objects.Stop;
import com.example.timetablereader.Objects.StopTime;
import com.example.timetablereader.Objects.Trip;

public interface FeedParser {

	List<StopTime> parseStopTimes(String feedUrl);
	List<StopTime> parseStopTimes(InputStream inputStream);
	
	Map<Integer, Stop> parseStops(String feedUrl);
	Map<Integer, Stop> parseStops(InputStream inputStream);
	
	List<Trip> parseTrips(String feedUrl);
	List<Trip> parseTrips(InputStream inputStream);
	
	List<Route> parseRoutes(String feedUrl);
	List<Route> parseRoutes(InputStream inputStream);

}
