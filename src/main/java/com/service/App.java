package com.service;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.db.MongoOps;
import com.service.SearchService;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * App class.
 * 
 */
public class App {

	// only one and one instance of mongo should be present in the application
	// instance
	public static MongoOps mongoInstance;
	// singleton instance of searchly service [ElasticSearch as a Service]
	public static SearchService searchly;

	public static TwilioService twilioService;
	// Base URI the Grizzly HTTP server will listen on
	public static final URI BASE_URI = URI
			.create("http://localhost:9998/todo/");

	public static Logger appLogger = LoggerFactory.getLogger(App.class);

	public final static String ITEM_CREATED = "Item created successfully";
	public final static String ITEM_DELETED = "Item deleted successfully";
	public final static String ITEM_UPDATED = "Item updated successfully";

	public static void main(String[] args) {
		try {
			appLogger.info("todo app server api demo");

			final HttpServer server = GrizzlyHttpServerFactory
					.createHttpServer(BASE_URI, createApp());

			// initialize mongodb driver
			mongoInstance = new MongoOps();

			// initialize search service
			searchly = new SearchService();

			// initialize twilio service
			twilioService = new TwilioService();

			appLogger.info(String
					.format("Application started.%nApplication URL: "
							+ BASE_URI));
			appLogger.info("Hit enter to stop it...");
			System.in.read();
			server.stop();
		} catch (IOException ex) {
			//appLogger.log(Level.SEVERE, null, ex);
			appLogger.debug("IOException", ex);
			
		} catch (Exception e) {
			appLogger.debug("Exception", e);
		}
	}

	public static ResourceConfig createApp() {
		return new ResourceConfig().packages("com.service").registerInstances(
				new JsonMoxyConfigurationContextResolver());
	}

	@Provider
	final static class JsonMoxyConfigurationContextResolver implements
			ContextResolver<MoxyJsonConfig> {

		public MoxyJsonConfig getContext(Class<?> objectType) {
			final MoxyJsonConfig configuration = new MoxyJsonConfig();

			Map<String, String> namespacePrefixMapper = new HashMap<String, String>(
					1);
			namespacePrefixMapper.put(
					"http://www.w3.org/2001/XMLSchema-instance", "xsi");

			configuration.setNamespacePrefixMapper(namespacePrefixMapper);
			configuration.setNamespaceSeparator(':');

			return configuration;
		}
	}
}