package com.service;

/**
 * @author Saurabh Talbar (saurabh.talbar at gmail.com)
 */

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.model.ItemBean;
import com.model.Items;
import com.model.Message;

@Path("items")
@Produces("application/json")
@Consumes("application/json")
public class ItemResource {
	Response bad_request = Response.status(Status.BAD_REQUEST).build();
	Response not_found = Response.status(Status.NOT_FOUND).build();

	@GET
	public Response getAllItems() {
		return Response.ok(App.mongoInstance.getAllItems(), "application/json")
				.build();
	}

	@GET
	@Path("{title}")
	public Response getItem(@PathParam("title") String title) {

		if (title == null || title.trim().length() == 0)
			return Response.serverError().entity("title cannot be blank")
					.build();

		if (App.mongoInstance.contains(title))
			// return App.mongoInstance.get(title);
			return Response
					.ok(App.mongoInstance.get(title), "application/json")
					.build();
		else
			return not_found;
	}

	@POST
	@Path("search")
	public Response searchItems(ItemBean query) {

		if (query == null || query.getTitle() == "")
			return bad_request;
		if (!App.mongoInstance.contains(query.getTitle()))
			return not_found;

		ArrayList<ItemBean> searchResults = new ArrayList<ItemBean>(
				App.searchly.searchItems(query.getTitle()));
		Items items = new Items(searchResults);

		return Response.ok(items, "application/json").build();
	}

	@POST
	public Response createItem(ItemBean input) {

		if (input == null || input.getTitle() == "")
			return bad_request;

		// insert/update item into mongodb
		if (App.mongoInstance.contains(input.getTitle())) {
			App.mongoInstance.update(input);
			return Response.ok(new Message(App.ITEM_UPDATED),
					"application/json").build();
		} else {
			App.mongoInstance.put(input);
			return Response.ok(new Message(App.ITEM_CREATED),
					"application/json").build();
		}
	}

	@PUT
	public Response updateItem(ItemBean input) {

		if (input == null || input.getTitle() == "")
			return bad_request;

		// insert/update item into mongodb
		if (App.mongoInstance.contains(input.getTitle())) {
			App.mongoInstance.update(input);
			return Response.ok(new Message(App.ITEM_UPDATED),
					"application/json").build();
		} else {
			App.mongoInstance.put(input);
			return Response.ok(new Message(App.ITEM_CREATED),
					"application/json").build();
		}
	}

	@DELETE
	@Path("{title}")
	public Response deleteItem(@PathParam("title") String title) {
		if (title == null)
			return bad_request;
		if (!App.mongoInstance.contains(title))
			return not_found;

		App.mongoInstance.delete(title);
		return Response.ok(new Message(App.ITEM_DELETED), "application/json")
				.build();
	}
}