package com.example.timetablereader;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


import org.xmlpull.v1.XmlSerializer;

import com.example.timetablereader.Objects.StopTime;
import com.example.timetablereader.XMLParsing.FeedParser;
import com.example.timetablereader.XMLParsing.XMLPullFeedParser;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StopTimesActivity extends Activity {

	public static final String STOP_TIMES_URL = "http://homepages.ecs.vuw.ac.nz/~ian/nwen304/simpletimetable/stop_times.xml";
	private List<StopTime> stopTimes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {	
        super.onCreate(savedInstanceState);
     
        loadStopTimes();
        displayTripStopTimes(2, 53832, true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stop_times, menu);
        return true;
    }



	private void loadStopTimes(){
    	try{

	    	FeedParser parser = (FeedParser) new XMLPullFeedParser(STOP_TIMES_URL);
	    	stopTimes = parser.parseStopTimes();

	    	//String xml = writeXml();
	    	Log.d("TimetableReader", "stop times read - " + stopTimes.size());
    	} catch (Throwable t){
    		Log.e("TimetableReader",t.getMessage(),t);
    	}
    }
	
	private void displayTripStopTimes(int routeId, int tripId, boolean outbound ){
        setContentView(R.layout.activity_stop_times);
        TextView t=new TextView(this); 
        t=(TextView)findViewById(R.id.route_id); 
        t.setText("Route id is " + routeId);
        
        t=(TextView)findViewById(R.id.trip_id); 
        t.setText("Trip id is " + tripId);
			
		// get list of all stoptimes applicable
        List<String> toDisplay = new ArrayList<String>();
        for(StopTime s: this.stopTimes){
        	Log.d("TimetableReader", s.toString());        	  	
        	if (s.getRouteId() == routeId && s.getTripId() == tripId && s.isOutbound()){
        		String message = "" + s.getStopId();
        		
        		toDisplay.add(message);
        	}
        }
        Log.d("TimetableReader", "found applicable stoptimes - " + toDisplay.size());
        
        
        // display them
        ArrayAdapter<String> adapter = 
	    		new ArrayAdapter<String>(this, R.layout.row,toDisplay);           
        ListView myList=(ListView)findViewById(R.id.stop_list);
        myList.setAdapter(adapter);
       
        
	}



	private String writeXml(){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
//			serializer.setOutput(writer);
//			serializer.startDocument("UTF-8", true);
//			serializer.startTag("", "messages");
//			serializer.attribute("", "number", String.valueOf(messages.size()));
//			for (Message msg: messages){
//				serializer.startTag("", "message");
//				serializer.attribute("", "date", msg.getDate());
//				serializer.startTag("", "title");
//				serializer.text(msg.getTitle());
//				serializer.endTag("", "title");
//				serializer.startTag("", "url");
//				serializer.text(msg.getLink().toExternalForm());
//				serializer.endTag("", "url");
//				serializer.startTag("", "body");
//				serializer.text(msg.getDescription());
//				serializer.endTag("", "body");
//				serializer.endTag("", "message");
//			}
//			serializer.endTag("", "messages");
//			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}



}
