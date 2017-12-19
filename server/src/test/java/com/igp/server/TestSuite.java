package com.igp.server;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * Created by anjana on 5/1/16.
 */
@RunWith(Suite.class)
@SuiteClasses({})
public class TestSuite {

    private static Server server;

    @BeforeClass
    public static void setUp() throws Exception {
        System.out.println("Starting server");
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(9998);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");

        ServletHolder servletHolder = new ServletHolder(ServletContainer.class);
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter("jersey.config.server.provider.packages", "com.igp.api");
        context.addServlet(servletHolder, "/*");

        server.setHandler(context);

        server.start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        System.out.println("Shutting down the server");
        server.stop();
        server.destroy();
    }
}
