package com.service;

import io.searchbox.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;

import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.mapping.PutMapping;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.model.ItemBean;

public class SearchService {
	// single instance of JestClient for elasticsearch support
	private JestClient client;
	private String indexName = "itemsindex";
	private String testIndexName = "testindex2";
	private String indexType = "items";
	private String itemsJson = "{\"items\": {\"properties\": {\"body\": "
			+ "{\"type\": \"string\"},\"done\": {\"type\": \"boolean\"},\"title\": "
			+ "{\"type\": \"string\"},\"mid\": {\"properties\":{\"_inc\":	"
			+ "{\"type\": \"long\"},\"_machine\":	{\"type\": \"long\"},\"_new\":	"
			+ "{\"type\": \"boolean\"},\"_time\":	{\"type\": \"long\"}}}}}}";

	public SearchService() {
		try {
			// Configuration of JestClient
			/*
			 * ClientConfig clientConfig = new ClientConfig.Builder(
			 * "http://site:<api-key>@api.searchbox.io/")
			 * .multiThreaded(true).build();
			 * 
			 * // Construct a new Jest client according to configuration via //
			 * factory JestClientFactory factory = new JestClientFactory();
			 * factory.setClientConfig(clientConfig); client =
			 * factory.getObject();
			 */

			// Assuming that ElasticSearch instance is running at:
			// http://localhost:9200
			JestClientFactory factory = new JestClientFactory();
			factory.setHttpClientConfig(new HttpClientConfig.Builder(
					"http://localhost:9200").multiThreaded(true).build());
			client = factory.getObject();

			// create an index
			client.execute(new CreateIndex.Builder(testIndexName).build());

			// ElasticSearch settings
			ImmutableSettings.Builder settingsBuilder = ImmutableSettings
					.settingsBuilder();
			settingsBuilder.put("number_of_shards", 5);
			settingsBuilder.put("number_of_replicas", 1);

			client.execute(new CreateIndex.Builder(testIndexName).settings(
					settingsBuilder.build().getAsMap()).build());

			PutMapping putMapping = new PutMapping.Builder(testIndexName,
					indexType, itemsJson).build();
			client.execute(putMapping);

			System.out.println("Index: " + testIndexName + " created");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void setIndexName(String name) {
		this.indexName = name;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexType(String name) {
		this.indexType = name;
	}

	public String getIndexType() {
		return indexType;
	}

	public JestResult execute(Action action) throws Exception {
		return client.execute(action);
	}

	public void execute() {

	}

	public List<ItemBean> searchItems(String param) {
		try {

			System.out
					.println("Received request to search for param: " + param);
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(QueryBuilders.matchQuery("title", param));

			Search search = new Search.Builder(searchSourceBuilder.toString())
					.addIndex("testindex2").addType("items").build();

			JestResult result = client.execute(search);

			System.out.println("JestResult Error: " + result.getErrorMessage());
			System.out.println("JestResult Hits: " + result);
			System.out.println("JestResult boolean: " + result.isSucceeded());

			return result.getSourceAsObjectList(ItemBean.class);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
