package com.example.timetablereader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParser;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

public class XMLPullFeedParser extends BaseFeedParser {

	public XMLPullFeedParser(String feedUrl) {
		super(feedUrl);
	}

	public List<StopTime> parse() {	
		try {
			AsyncTask<InputStream, Void, List<StopTime>> parseTask = new ParseRSSTask().execute(getInputStream());
			return parseTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	
	class ParseRSSTask extends AsyncTask<InputStream, Void, List<StopTime>> {

		@Override
		protected List<StopTime> doInBackground(InputStream... inputStreams) {
			// list of stop times
			List<StopTime> stopTimes = null;

			XmlPullParser parser = Xml.newPullParser();
			try {
				// auto-detect the encoding from the stream
				parser.setInput(inputStreams[0], null);
				int eventType = parser.getEventType();
				StopTime currentStopTime = null;
				boolean done = false;
				while (eventType != XmlPullParser.END_DOCUMENT && !done){
					String name = null;
					switch (eventType){
						case XmlPullParser.START_DOCUMENT:
							stopTimes = new ArrayList<StopTime>();
							break;
						case XmlPullParser.START_TAG:					
							name = parser.getName();
							if (name.equalsIgnoreCase(RECORD)){
								currentStopTime = new StopTime();
							} else if (currentStopTime != null){
								if (name.equalsIgnoreCase(ROUTE_ID)){
									currentStopTime.setRouteId(Integer.parseInt(parser.nextText()));
								} else if (name.equalsIgnoreCase(TRIP_ID)){
									currentStopTime.setTripId(Integer.parseInt(parser.nextText()));
								} else if (name.equalsIgnoreCase(DIRECTION_ID)){
									String dirId = parser.nextText().trim();
									if (dirId.equals("0")){
										currentStopTime.setOutbound(true);
									}else
										currentStopTime.setOutbound(false);
								} else if (name.equalsIgnoreCase(ARRIVAL_TIME)){
									currentStopTime.setArrivalTime(parser.nextText());
								} else if (name.equalsIgnoreCase(DEPARTURE_TIME)){
									currentStopTime.setDepartureTime(parser.nextText());
								}else if (name.equalsIgnoreCase(STOP_ID)){
									currentStopTime.setStopId(Integer.parseInt(parser.nextText()));
								}else if (name.equalsIgnoreCase(STOP_SEQUENCE)){
									currentStopTime.setStopSequence(Integer.parseInt(parser.nextText()));
								}
							}
							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(RECORD) && currentStopTime != null){
								stopTimes.add(currentStopTime);
							} else if (name.equalsIgnoreCase(DOCUMENT)){
								done = true;
							}
							break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			// return the stop times
			return stopTimes;			
		}
		
		
		
	}
	

}