package com.example.timetablereader;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class StopTimesActivity extends Activity {

	public static final String STOP_TIMES_URL = "http://homepages.ecs.vuw.ac.nz/~ian/nwen304/simpletimetable/stop_times.xml";
	private List<StopTime> stopTimes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_times);
        loadStopTimes();
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
	    	stopTimes = parser.parse();

	    	//String xml = writeXml();
	    	Log.d("TimetableReader", "readStopTimes!");


    	} catch (Throwable t){
    		Log.e("TimetableReader",t.getMessage(),t);
    	}
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
