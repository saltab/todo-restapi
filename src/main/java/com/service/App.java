package com.service;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;

import com.db.MongoOps;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * App class.
 *
 */
public class App {
	
	// only one and one instance of mongo should be present in the application instance
	protected static MongoOps mongoInstance;
    // Base URI the Grizzly HTTP server will listen on
	protected static final URI BASE_URI = URI.create("http://localhost:9998/todo/");

    public static void main(String[] args) {
        try {
            System.out.println("TODO App server api demo");

            final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, createApp());

            //initialize mongodb driver
            mongoInstance = new MongoOps();
            
            System.out.println(String.format("Application started.%nApplication URL: "+BASE_URI));
            System.out.println("Hit enter to stop it...");
            System.in.read();
            server.stop();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ResourceConfig createApp() {
        return new ResourceConfig().
                packages("com.service").
                registerInstances(new JsonMoxyConfigurationContextResolver());
    }

    @Provider
    final static class JsonMoxyConfigurationContextResolver implements ContextResolver<MoxyJsonConfig> {

        public MoxyJsonConfig getContext(Class<?> objectType) {
            final MoxyJsonConfig configuration = new MoxyJsonConfig();

            Map<String, String> namespacePrefixMapper = new HashMap<String, String>(1);
            namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");

            configuration.setNamespacePrefixMapper(namespacePrefixMapper);
            configuration.setNamespaceSeparator(':');

            return configuration;
        }
    }

/*    public static final String BASE_URI = "http://localhost:7070/todo/";

    *//**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     *//*
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.service package
        final ResourceConfig rc = new ResourceConfig().packages("com.service");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    *//**
     * Main method.
     * @param args
     * @throws IOException
     *//*
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }*/
}

