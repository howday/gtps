package com.project.gtps.main;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

/**
 * Created by suresh on 12/23/16.
 */
public class Main {


    /**
     * Base URI the Grizzly HTTP server will listen on
     */
    public static final String BASE_URI;
    public static final String protocol;
    public static final String host;
    public static final String path;
    public static final String port;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    static {
        protocol = "http://";
        host = "172.17.3.126";
        port = "8080";
        path = "myapp";
        BASE_URI = protocol + host + ":" + port + "/" + path + "/";
    }

    /**
     * Starts Grizzly HTTP server exposing JAX-RS api defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        /**
         * create a resource config that scans for JAX-RS api and providers
         * in com.example.rest package
         */
        final ResourceConfig rc = new ResourceConfig().packages("com.project.gtps.api");

        /**
         *  for support JSON
         */
        rc.register(JacksonFeature.class);

        /**
         * create and start a new instance of grizzly http server
         * exposing the Jersey application at BASE_URI
         */
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("GTPS webservice started with WADL available at "
                + "%s\n", BASE_URI));

        /**
         *Stops the server on pressing Ctrl+C on running console
         */
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Stopping server.......");
                logger.info("Stopping server........");
                server.shutdown();
                System.out.println("Server stopped.......");
                logger.info("Stopping server........");
            }
        });
    }

}
