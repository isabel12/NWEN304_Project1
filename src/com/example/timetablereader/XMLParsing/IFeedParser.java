package com.example.timetablereader.XMLParsing;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import android.util.SparseArray;

import com.example.timetablereader.Objects.Route;
import com.example.timetablereader.Objects.Stop;
import com.example.timetablereader.Objects.StopTime;
import com.example.timetablereader.Objects.Trip;
import com.example.timetablereader.Objects.ObjectUpdates.RouteUpdate;
import com.example.timetablereader.Objects.ObjectUpdates.VersionedCollection;

public interface IFeedParser {

	VersionedCollection<Map<Integer, List<StopTime>>> parseStopTimes(InputStream inputStream);

	VersionedCollection<Map<Integer, Stop>> parseStops(InputStream inputStream);

	VersionedCollection<List<Trip>> parseTrips(InputStream inputStream);

	VersionedCollection<List<Route>> parseRoutes(InputStream inputStream);

	Map<String, Integer> parseFileVersionNumbers(InputStream inputStream);

	List<RouteUpdate> parseRouteUpdates(InputStream inputStream, int currentVersion);



}
