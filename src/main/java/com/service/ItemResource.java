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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.model.ItemBean;


@Path("item")
@Produces("application/json")
@Consumes("application/json")
public class ItemResource {

	@GET
	@Path("{title}")
	public Response getItem(@PathParam("title") String title) {

		if(title==null || title.trim().length()==0)
			return Response.serverError().entity("title cannot be blank").build();		

		if(App.mongoInstance.contains(title))
			//return App.mongoInstance.get(title);
			return Response.ok(App.mongoInstance.get(title), "application/json").build();
		else
			throw new WebApplicationException(404);
	}

	@POST
	public Response createItem(ItemBean input) {		

		if(input==null)
			return Response.serverError().entity("json input cannot be blank").build();

		//insert/update item into mongodb
		if(App.mongoInstance.contains(input.getTitle())){
			App.mongoInstance.update(input);
			return Response.status(200).entity("Item with title "+input.getTitle()+" updated successfully").build();
		}
		else{
			App.mongoInstance.put(input);
			return Response.status(200).entity("Item with title "+input.getTitle()+" created successfully").build();
		}
	}

	@PUT
	public Response updateItem(ItemBean input) {		

		if(input==null)
			return Response.serverError().entity("json input cannot be blank").build();

		//insert/update item into mongodb
		if(App.mongoInstance.contains(input.getTitle())){
			App.mongoInstance.update(input);
			return Response.status(200).entity("Item with title "+input.getTitle()+" updated successfully").build();
		}
		else{
			App.mongoInstance.put(input);
			return Response.status(200).entity("Item with title "+input.getTitle()+" created successfully").build();
		}
	}

	@DELETE
	@Path("{title}")
	public Response deleteItem(@PathParam("title") String title){
		if(title==null || title.trim().length()==0)
			return Response.serverError().entity("title cannot be blank").build();		

		App.mongoInstance.delete(title);
		return Response.status(200).entity("Item with title "+title+" deleted successfully").build();
	}
}