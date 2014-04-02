package com.db;

/**
 * @author Saurabh Talbar (saurabh.talbar at gmail.com)
 */

import io.searchbox.core.Index;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.model.ItemBean;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.service.App;
import com.twilio.sdk.TwilioRestException;

public class MongoOps {
	private MongoClient mongo;
	private Datastore ds;
	private Morphia morphling;

	final String titleKey = "title";
	final String bodyKey = "body";
	final String doneKey = "done";
	// please change the name to your appropriate db name
	final String dbName = "mongo-items-db-6";
	
	String postMessage = "curl -X POST 'https://api.twilio.com/2010-04-01/Accounts/ACc4dc56710567a3daada7493a2d8fcc1c/Messages.json' " +
			"--data-urlencode 'To=+19049105497' " +
			"--data-urlencode 'From=+12252284707' " +
			"--data-urlencode 'Body=hello from twilio api' " +
			"-u ACc4dc56710567a3daada7493a2d8fcc1c:df7a149d7ce62259e79012d05a2ab28c";
	
	public MongoOps() {
		try {
			// lets create the mongoclient and connect our Morphius ORM to
			// MongoDB
			mongo = new MongoClient("localhost", 27017);

			morphling = new Morphia();
			morphling.map(ItemBean.class);

			ds = morphling.createDatastore(mongo, dbName);
			ds.ensureIndexes(); // creates indexes from @Index annotations in
			// your entities
			ds.ensureCaps(); // creates capped collections from @Entity

			System.out.println("DB: " + dbName + " created");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException m) {
			m.printStackTrace();
		}

	}

	public void put(ItemBean item) {
		if (item != null && !this.contains(item.getTitle())) {
			try {
				// item.setId(++App.id_track);
				ds.save(item);
				Index index = new Index.Builder(item)
				.index("testindex2")
				.type(App.searchly.getIndexType()).build();
				App.searchly.execute(index);
				System.out.println("Item "+item.getTitle()+" indexed!");
				System.out.println("Index URI: "+index.getURI());
				System.out.println("Index: "+index.getIndex());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Logger.getLogger(MongoOps.class.getName()).log(Level.SEVERE,
						null, e);
			}

		} else
			this.update(item);
		System.out
		.println("Item with title: " + item.getTitle() + " inserted!");
	}

	public ItemBean get(String title) {
		ItemBean item = null;
		if (title != null)
			item = ds.find(ItemBean.class).field(titleKey).equal(title).get();

		if (item.getTitle().contentEquals(title))
			System.out.println("Item with title: " + title + " found!");
		else
			System.out.println("Item with title: " + title + " NOT found!");

		return item;
	}

	public List<ItemBean> getAll(String title) {
		if (title != null)
			return ds.find(ItemBean.class).field(titleKey).equal(title)
					.asList();
		else
			return null;
	}

	public boolean contains(String title) {

		ItemBean item = ds.find(ItemBean.class).field(titleKey).equal(title)
				.get();

		if (item == null)
			return false;
		else
			return true;
	}

	public void delete(String title) {
		if (title != null && this.contains(title)) {
			Query<ItemBean> deleteQuery = ds.createQuery(ItemBean.class)
					.field(titleKey).equal(title);
			ds.delete(deleteQuery);
			System.out.println("Item with title: " + title + " deleted!");
		} else {
			System.out.println("Item with title: " + title
					+ " cannot be found in the database to delete");
		}
	}

	public void update(ItemBean item) {
		UpdateOperations<ItemBean> ops;
		Query<ItemBean> updateQuery = ds.createQuery(ItemBean.class)
				.field(titleKey).equal(item.getTitle());

		ops = ds.createUpdateOperations(ItemBean.class)
				.set(bodyKey, item.getBody()).set(doneKey, item.getDone());
		ds.update(updateQuery, ops);
		
		if(item.getDone())
			try {
				App.twilioService.postMessage(item.getTitle());
				System.out.println("Task Done!Message sent to user");
			} catch (TwilioRestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		System.out.println("Item with title: " + item.getTitle() + " updated!");
	}
	
	public void postTwilioMessage(){
		try {
			Runtime.getRuntime().exec(postMessage);
			System.out.println("Message posted via Twilio!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
