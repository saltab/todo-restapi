package com.db;

/**
 * @author Saurabh Talbar (saurabh.talbar at gmail.com)
 */

import io.searchbox.core.Index;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.model.ItemBean;
import com.model.Items;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.service.App;
import com.twilio.sdk.TwilioRestException;

public class MongoOps {
	private MongoClient mongo;
	private Datastore ds;
	private Morphia morphling;
	private static Logger mongoLogger = LoggerFactory.getLogger(MongoOps.class);

	final String titleKey = "title";
	final String bodyKey = "body";
	final String doneKey = "done";
	// please change the name to your appropriate db name
	final String dbName = "mongo-items-db";

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

			// System.out.println("DB: " + dbName + " created");
			mongoLogger.info("DB: " + dbName + " created");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException m) {
			m.printStackTrace();
			mongoLogger.debug("mongoLogger", m);
		}

	}

	public void put(ItemBean item) {
		if (item != null && !this.contains(item.getTitle())) {
			try {
				// item.setId(++App.id_track);
				ds.save(item);
				Index index = new Index.Builder(item).index("testindex2")
						.type(App.searchly.getIndexType()).build();
				App.searchly.execute(index);
				mongoLogger.info("Item " + item.getTitle() + " indexed!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				mongoLogger.debug("Exception", e);
			}

		} else
			this.update(item);
		mongoLogger.info("Item with title: " + item.getTitle() + " inserted!");
	}

	public ItemBean get(String title) {
		ItemBean item = null;
		if (title != null)
			item = ds.find(ItemBean.class).field(titleKey).equal(title).get();

		if (item.getTitle().contentEquals(title))
			mongoLogger.info("Item with title: " + title + " found!");
		else
			mongoLogger.info("Item with title: " + title + " NOT found!");

		return item;
	}

	public Items getAllItems() {
		Items items = new Items();
		List<ItemBean> allItems = new ArrayList<ItemBean>();

		for (ItemBean item : ds.find(ItemBean.class))
			allItems.add(item);
		items.setItems(allItems);
		return items;
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
			mongoLogger.info("Item with title: " + title + " deleted!");
		} else {
			mongoLogger.info("Item with title: " + title
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

		if (item.getDone())
			try {
				App.twilioService.postMessage(item.getTitle());
				mongoLogger.info("Task Done!Message sent to user");
			} catch (TwilioRestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mongoLogger.debug("TwilioService Exception", e);
			}

		mongoLogger.info("Item with title: " + item.getTitle() + " updated!");
	}

}
