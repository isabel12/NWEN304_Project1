package com.example.timetablereader.Objects.ObjectUpdates;

import java.util.List;

import android.util.Log;

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

	public void applyUpdate(C collection){
		switch(type){
		case Add:	applyAdd(collection);
		break;
		case Delete: applyDelete(collection);
		break;
		case Edit: applyEdit(collection);
		break;
		}
	}

	protected abstract void applyEdit(C collection);

	protected abstract void applyDelete(C collection);

	protected abstract void applyAdd(C collection);


}
