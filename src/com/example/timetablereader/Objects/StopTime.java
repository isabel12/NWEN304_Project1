package com.example.timetablereader.Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StopTime implements Comparable<StopTime>{

	private int tripId;
	private int stopId;
	private Date arrivalTime;
	private Date departureTime;
	private int stopSequence;
	
	static SimpleDateFormat INPUT_FORMATTER = 
			new SimpleDateFormat("HH:mm:ss");

	static SimpleDateFormat OUTPUT_FORMATTER = 
			new SimpleDateFormat("HH:mm");

	
	
	public int getTripId() {
		return tripId;
	}
	public void setTripId(int tripId) {
		this.tripId = tripId;
	}
	public String getArrivalTime() {
		return INPUT_FORMATTER.format(arrivalTime);
	}
	public String getArrivalTimePretty(){	
		return OUTPUT_FORMATTER.format(arrivalTime);
	}
	public void setArrivalTime(String arrivalTime) {
		try {
			this.arrivalTime = INPUT_FORMATTER.parse(arrivalTime.trim());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	public String getDepartureTime() {
		return INPUT_FORMATTER.format(departureTime);
	}
	public String getDepartureTimePretty(){
		return OUTPUT_FORMATTER.format(departureTime);
	}
	public void setDepartureTime(String departureTime) {
		try {
			this.departureTime = INPUT_FORMATTER.parse(departureTime.trim());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	public int getStopId() {
		return stopId;
	}
	public void setStopId(int stopId) {
		this.stopId = stopId;
	}
	public int getStopSequence() {
		return stopSequence;
	}
	public void setStopSequence(int stopSequence) {
		this.stopSequence = stopSequence;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arrivalTime == null) ? 0 : arrivalTime.hashCode());
		result = prime * result
				+ ((departureTime == null) ? 0 : departureTime.hashCode());
		result = prime * result + stopId;
		result = prime * result + stopSequence;
		result = prime * result + tripId;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StopTime other = (StopTime) obj;
		if (arrivalTime == null) {
			if (other.arrivalTime != null)
				return false;
		} else if (!arrivalTime.equals(other.arrivalTime))
			return false;
		if (departureTime == null) {
			if (other.departureTime != null)
				return false;
		} else if (!departureTime.equals(other.departureTime))
			return false;
		if (stopId != other.stopId)
			return false;
		if (stopSequence != other.stopSequence)
			return false;
		if (tripId != other.tripId)
			return false;
		return true;
	}
	@Override
	public int compareTo(StopTime another) {
		// if same trip, order by stop sequence (note, assumes same inward/outward)
		if (this.tripId == another.tripId){
			return this.stopSequence - another.stopSequence;
		}

		// otherwise order by trip id
		return this.tripId - another.tripId;
	}
	
	@Override
	public String toString() {
		return "StopTime [tripId=" + tripId
				+ ", stopId=" + stopId 
				+ ", arrivalTime=" + getArrivalTime() + ", departureTime="
				+ getDepartureTime() + ", stopSequence=" + stopSequence + "]";
	}

}
