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

public abstract class BaseFeedParser implements FeedParser {
	// names of the XML tags
	static final String DOCUMENT = "document";
	static final String RECORD = "record";

	static final String ROUTE_ID = "route_id";
	static final String TRIP_ID = "trip_id";
	static final String DIRECTION_ID = "direction_id";
	static final String ARRIVAL_TIME = "arrival_time";
	static final String DEPARTURE_TIME = "departure_time";
	static final String STOP_ID = "stop_id";
	static final String STOP_SEQUENCE = "stop_sequence";
	static final String STOP_NAME = "stop_name";
	static final String STOP_LAT = "stop_lat";
	static final String STOP_LON = "stop_lon";

	private final URL feedUrl;
	

	protected BaseFeedParser(String feedUrl){		
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStream() {
		AsyncTask<URL, Void, InputStream> task = new GetInputStream().execute(feedUrl);
		try {
			return task.get();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	class GetInputStream extends AsyncTask<URL, Void, InputStream> {

		@Override
		protected InputStream doInBackground(URL... feedUrls) {
			try {
				return feedUrls[0].openConnection().getInputStream();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}

	
}
