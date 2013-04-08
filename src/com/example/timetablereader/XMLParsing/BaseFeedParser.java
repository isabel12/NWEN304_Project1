package com.example.timetablereader.XMLParsing;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;

public abstract class BaseFeedParser implements IFeedParser {
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

	public static final String EDIT = "edit";
	public static final String DELETE = "delete";
	public static final String ADD = "add";

}
