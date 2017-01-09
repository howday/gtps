package com.project.gtps.main;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

/**
 * Created by suresh on 12/23/16.
 */
public class Main {


    /**
     * Base URI the Grizzly HTTP server will listen on
     */
    private static String BASE_URI;
    private static String protocol;
    private static String hostname;
    private static String path;
    private static String port;
    private static Properties settings = new Properties();
    private static Logger logger = LoggerFactory.getLogger(Main.class);

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
        protocol = settings.getProperty("protocol");
        hostname = settings.getProperty("hostname");
        port = settings.getProperty("port");
        path = settings.getProperty("path");
        BASE_URI = protocol + hostname + ":" + port + "/" + path + "/";

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        // check that server configuration file is passed as cmd line argument
        if (args.length != 1) {
            System.err.println("Error: Server configuration file not passed in command line");
            System.exit(1);
        }

        try {
            settings.load(new FileInputStream(args[0]));
        } catch (Exception ex) {
            System.err.println("Error opening configuration file");
            logger.error("Server Exception : '{}'", ex);
            System.exit(1);
        }

        System.out.println("IP-ADDRESS: " + settings.getProperty("hostname"));

        logger.info("Starting server on port '{}'", port);
        final HttpServer server = startServer();
        System.out.println(String.format("GTPS webservice started with WADL available at "
                + "%s\n", BASE_URI));
        logger.info("GTPS webservice started with WADL available at :\n{}'", BASE_URI);

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
