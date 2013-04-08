package com.example.timetablereader.Objects.ObjectUpdates;

import java.util.List;

import com.example.timetablereader.Objects.Route;

/**
 * Where T is the object type, and C is the collection type that will be updated.
 * @author broomeisab
 *
 * @param <T>
 * @param <C>
 */
public abstract class Update<T, C> {

	protected int id;
	UpdateType type;

	protected Update(int id, UpdateType type){
		this.id = id;
		this.type = type;
	}

	public abstract void applyEdit(C collection);

	public abstract void applyDelete(C collection);

	public abstract void applyAdd(C collection);


}
