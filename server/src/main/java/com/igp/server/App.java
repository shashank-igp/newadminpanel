package com.igp.server;

import com.igp.config.Environment;
import com.igp.server.filters.RequestFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(Environment.getServerPort());
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        ServletHolder servletHolder = new ServletHolder(ServletContainer.class);
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter("jersey.config.server.provider.packages", "com.igp.api;com.igp.handles");
        servletHolder.setInitParameter(org.glassfish.jersey.server.ServerProperties.PROVIDER_CLASSNAMES,
            MultiPartFeature.class.getCanonicalName());
        context.addServlet(servletHolder, "/*");

        context.addFilter(RequestFilter.class, "/*", EnumSet.of(DispatcherType.INCLUDE,
            DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR,
            DispatcherType.FORWARD));


        GzipHandler gzipHandler = new GzipHandler();
        //String listOfMethods = Environment.getAllowedMethods();
        gzipHandler.setIncludedMethods("GET");
        //String listOfMimeTypes = Environment.getMimeTypes();
        gzipHandler.setIncludedMimeTypes("application/javascript", "application/json", "text/html", "text/plain", "text/xml", "application/xhtml+xml", "application/xml", "text/css", "image/svg+xml", "application/xml");
        gzipHandler.setIncludedPaths("/*");




        FilterHolder corsFilterHolder = new FilterHolder(CrossOriginFilter.class);
        corsFilterHolder.setInitParameter("allowedOrigins", Environment.getAllowedOrigins());
        corsFilterHolder.setInitParameter("allowedMethods", Environment.getAllowedMethods());
        corsFilterHolder.setInitParameter("allowedHeaders", "token,fkAssociateId,associateName,X-IGP-UISK,Content-Type");
        context.addFilter(corsFilterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));

        server.setHandler(gzipHandler);
        server.setHandler(context);

        try {
            server.start();
            logger.debug("Server started: {}", server);
            server.join();
        } catch (Throwable t) {
            logger.error("Error in starting server: ", System.err);
        } finally {
            server.destroy();
            logger.debug("Server destroyed: {}", server);
        }
    }
}
