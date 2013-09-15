package com.service;

/**
 * @author Saurabh Talbar (saurabh.talbar at gmail.com)
 */


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.model.ItemBean;


@Path("item")
@Produces("application/json")
@Consumes("application/json")
public class ItemResource {

	@GET
	@Path("{title}")
	public ItemBean getItem(@PathParam("title") String title) {
		if(App.mongoInstance.contains(title))
			return App.mongoInstance.get(title);
		else
			return new ItemBean("null","null",false);
	}

	@POST
	public ItemBean createItem(ItemBean input) {		

		//insert/update item into mongodb
		if(App.mongoInstance.contains(input.getTitle()))
			App.mongoInstance.update(input);
		else
			App.mongoInstance.put(input);
		return input;
	}
	
	@PUT
	public ItemBean updateItem(ItemBean input) {		

		//insert/update item into mongodb
		if(App.mongoInstance.contains(input.getTitle()))
			App.mongoInstance.update(input);
		else
			App.mongoInstance.put(input);
		return input;
	}
	@DELETE
	@Path("{title}")
	public void deleteItem(@PathParam("title") String title){
		App.mongoInstance.delete(title);

	}
}