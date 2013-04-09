package com.example.timetablereader.Objects.ObjectUpdates;

/**
 * This class represents a collection with a version number.
 * @author Izzi
 *
 * @param <C>
 */
public class VersionedCollection<C> {
	private C collection;
	private int version;
	
	
	public VersionedCollection(C collection, int version){
		this.collection = collection;
		this.version = version;	
	}


	public C getCollection() {
		return collection;
	}


	public void setCollection(C collection) {
		this.collection = collection;
	}


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}
	
	
	
	
}
