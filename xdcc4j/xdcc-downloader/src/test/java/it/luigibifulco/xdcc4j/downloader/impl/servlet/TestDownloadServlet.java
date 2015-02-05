package it.luigibifulco.xdcc4j.downloader.impl.servlet;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.downloader.DownloaderServiceModule;
import it.luigibifulco.xdcc4j.downloader.servlet.DownloadServletModule;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

//@RunWith(GuiceJUnitRunner.class)
//@GuiceModules({ DownloaderServiceModule.class, DownloadServletModule.class })
public class TestDownloadServlet {

	private static Logger logger = LoggerFactory
			.getLogger(TestDownloadServlet.class);

	@Before
	public void startServer() throws Exception {
		logger.info("START SERVER");
		DownloaderServiceModule serviceMod = new DownloaderServiceModule();
		DownloadServletModule servlets = new DownloadServletModule();
		Injector injector = Guice.createInjector(serviceMod, servlets);
		int port = 8080;
		Server server = new Server(port);

		ServletContextHandler servletContextHandler = new ServletContextHandler(
				server, "/", ServletContextHandler.SESSIONS);
		servletContextHandler.addFilter(GuiceFilter.class, "/*",
				EnumSet.allOf(DispatcherType.class));

		// You MUST add DefaultServlet or your server will always return 404s
		// servletContextHandler.addServlet(DefaultServlet.class, "/");

		// Start the server
		server.start();

		// Wait until the server exits
		server.join();
		logger.info("SERVER STARTED");
	}

	public static void main(String[] args) throws Exception {
		logger.info("START SERVER");
		DownloaderServiceModule serviceMod = new DownloaderServiceModule();
		DownloadServletModule servlets = new DownloadServletModule();
		Injector injector = Guice.createInjector(serviceMod, servlets);

		int port = 8080;
		Server server = new Server(port);

		ServletContextHandler servletContextHandler = new ServletContextHandler(
				server, "/", ServletContextHandler.SESSIONS);
		servletContextHandler.addFilter(GuiceFilter.class, "/*",
				EnumSet.allOf(DispatcherType.class));

		// You MUST add DefaultServlet or your server will always return 404s
		servletContextHandler.addServlet(DefaultServlet.class, "/");

		// Start the server
		server.start();

		// Wait until the server exits
		server.join();
		logger.info("SERVER STARTED");
	}

	@Test
	public void testServlet() throws Exception {
		logger.info("testServlet STARTED");
	}
}
