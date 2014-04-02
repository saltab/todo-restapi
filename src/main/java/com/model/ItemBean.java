package com.model;

/**
 * @author Saurabh Talbar (saurabh.talbar at gmail.com)
 */

import org.bson.types.ObjectId;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

// Definition of our each todo task item
@Entity("items")
public class ItemBean {

	// @JestId will be automatically created by ElasticSearch
	String title;

	// This Id is required by Morphia for indexing
	@Id
	ObjectId mid;

	String body;
	boolean done;

	public ItemBean() {

	}

	public ItemBean(String title, String body, boolean done) {
		this.title = title;
		this.body = body;
		this.done = done;
	}

	public ObjectId getmId() {
		return mid;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public boolean getDone() {
		return done;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

}
