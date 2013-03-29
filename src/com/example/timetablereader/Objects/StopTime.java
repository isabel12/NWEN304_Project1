package com.example.timetablereader.Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StopTime implements Comparable<StopTime>{

	private int routeId;
	private int tripId;
	private int stopId;
	private boolean outbound;
	private Date arrivalTime;
	private Date departureTime;
	private int stopSequence;
	
	static SimpleDateFormat FORMATTER = 
			new SimpleDateFormat("HH:mm:ss");

	public int getRouteId() {
		return routeId;
	}
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	public int getTripId() {
		return tripId;
	}
	public void setTripId(int tripId) {
		this.tripId = tripId;
	}
	public boolean isOutbound() {
		return outbound;
	}
	public void setOutbound(boolean outbound) {
		this.outbound = outbound;
	}
	public Date getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		try {
			this.arrivalTime = FORMATTER.parse(arrivalTime.trim());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	public Date getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		try {
			this.departureTime = FORMATTER.parse(departureTime.trim());
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
		result = prime * result + (outbound ? 1231 : 1237);
		result = prime * result + routeId;
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
		if (outbound != other.outbound)
			return false;
		if (routeId != other.routeId)
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
		return "StopTime [routeId=" + routeId + ", tripId=" + tripId
				+ ", stopId=" + stopId + ", outbound=" + outbound
				+ ", arrivalTime=" + FORMATTER.format(arrivalTime) + ", departureTime="
				+ FORMATTER.format(departureTime) + ", stopSequence=" + stopSequence + "]";
	}

}
