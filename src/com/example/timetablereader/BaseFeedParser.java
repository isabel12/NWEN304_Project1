package com.example.timetablereader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public abstract class BaseFeedParser implements FeedParser {
	// names of the XML tags
	static final String DOCUMENT = "Document";
	static final String RECORD = "Record";

	static final String ROUTE_ID = "Route id";
	static final String TRIP_ID = "Trip id";
	static final String DIRECTION_ID = "Direction id";
	static final String ARRIVAL_TIME = "Arrival time";
	static final String DEPARTURE_TIME = "Departure time";
	static final String STOP_ID = "Stop id";
	static final String STOP_SEQUENCE = "Stop sequence";
	static final String STOP_NAME = "Stop name";
	static final String STOP_LAT = "Stop lat";
	static final String STOP_LON = "Stop lon";

	private final URL feedUrl;

	protected BaseFeedParser(String feedUrl){
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStream() {
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public abstract List<StopTime> parse();
}
