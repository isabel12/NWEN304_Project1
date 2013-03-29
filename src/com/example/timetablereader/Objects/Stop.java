package com.example.timetablereader.Objects;

public class Stop {
	private int stopId;
	private String stopName;
	private double stopLat;
	private double stopLon;
	
	
	public int getStopId() {
		return stopId;
	}
	public void setStopId(int stopId) {
		this.stopId = stopId;
	}
	public String getStopName() {
		return stopName;
	}
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	public double getStopLat() {
		return stopLat;
	}
	public void setStopLat(double stopLat) {
		this.stopLat = stopLat;
	}
	public double getStopLon() {
		return stopLon;
	}
	public void setStopLon(double stopLon) {
		this.stopLon = stopLon;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + stopId;
		long temp;
		temp = Double.doubleToLongBits(stopLat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(stopLon);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((stopName == null) ? 0 : stopName.hashCode());
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
		Stop other = (Stop) obj;
		if (stopId != other.stopId)
			return false;
		if (Double.doubleToLongBits(stopLat) != Double
				.doubleToLongBits(other.stopLat))
			return false;
		if (Double.doubleToLongBits(stopLon) != Double
				.doubleToLongBits(other.stopLon))
			return false;
		if (stopName == null) {
			if (other.stopName != null)
				return false;
		} else if (!stopName.equals(other.stopName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Stop [stopId=" + stopId + ", stopName=" + stopName
				+ ", stopLat=" + stopLat + ", stopLon=" + stopLon + "]";
	}
	
	

}
