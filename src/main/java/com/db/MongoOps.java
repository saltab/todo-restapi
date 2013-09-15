package com.db;

/**
 * @author Saurabh Talbar (saurabh.talbar at gmail.com)
 */

import java.net.UnknownHostException;
import java.util.List;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.model.ItemBean;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class MongoOps {
	private MongoClient mongo;
	private Datastore ds;
	private Morphia morphling;

	final String titleKey = "title";
	final String bodyKey = "body";
	final String doneKey = "done";
	final String dbName = "newitemdb";

	public MongoOps(){
		try{
			// lets create the mongoclient and connect our Morphius ORM to MongoDB
			mongo = new MongoClient("localhost",27017);

			morphling = new Morphia();
			morphling.map(ItemBean.class);

			ds = morphling.createDatastore(mongo,dbName);						
			ds.ensureIndexes(); //creates indexes from @Index annotations in your entities
			ds.ensureCaps(); //creates capped collections from @Entity

			System.out.println("DB: "+dbName+" created");
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(MongoException m){
			m.printStackTrace();
		}

	}

	public void put(ItemBean item){
		if(item!=null && !this.contains(item.getTitle()))
			ds.save(item);
		else
			this.update(item);
		System.out.println("Item with title: "+item.getTitle()+" inserted!");
	}

	public ItemBean get(String title){
		ItemBean item = null;
		if(title!=null)
			item = ds.find(ItemBean.class).field(titleKey).equal(title).get();

		if(item.getTitle().contentEquals(title))
			System.out.println("Item with title: "+title+" found!");
		else
			System.out.println("Item with title: "+title+" NOT found!");

		return item;
	}

	public List<ItemBean> getAll(String title){
		if(title != null)
			return ds.find(ItemBean.class).field(titleKey).equal(title).asList();
		else
			return null;
	}

	public boolean contains(String title){

		ItemBean item = ds.find(ItemBean.class).field(titleKey).equal(title).get();

		if(item == null)
			return false;
		else
			return true;
	}

	public void delete(String title){
		if(title != null && this.contains(title)){
			Query<ItemBean> deleteQuery = ds.createQuery(ItemBean.class).field(titleKey).equal(title);
			ds.delete(deleteQuery);
			//ds.findAndDelete(ds.find(ItemBean.class).field(titleKey).equal(title));
			System.out.println("Item with title: "+title+" deleted!");
		}else{
			System.out.println("Item with title: "+title+" cannot be found in the database to delete");
		}
	}

	public void update(ItemBean item){
		UpdateOperations<ItemBean> ops;
		Query<ItemBean> updateQuery = ds.createQuery(ItemBean.class).field(titleKey).equal(item.getTitle());
		//Query<ItemBean> updateQuery = ds.createQuery(ItemBean.class).field(Mapper.).equal(item.getTitle());

		ops = ds.createUpdateOperations(ItemBean.class).set(bodyKey, item.getBody()).set(doneKey, item.getDone());
		ds.update(updateQuery, ops);

		System.out.println("Item with title: "+item.getTitle()+" updated!");
	}

}
