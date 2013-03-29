package com.example.timetablereader.Objects;

public class Trip {
	private int routeId;
	private int tripId;
	private boolean outbound;
	
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (outbound ? 1231 : 1237);
		result = prime * result + routeId;
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
		Trip other = (Trip) obj;
		if (outbound != other.outbound)
			return false;
		if (routeId != other.routeId)
			return false;
		if (tripId != other.tripId)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Trip [routeId=" + routeId + ", tripId=" + tripId
				+ ", outbound=" + outbound + "]";
	}

	
}
