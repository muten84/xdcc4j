package it.luigibifulco.xdcc4j.downloader.delegate.rest;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.downloader.DownloaderServiceModule;

import java.util.EnumSet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.GuiceFilter;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ DownloaderServiceModule.class, DownloaderRestModule.class })
public class DownloaderDelegateTest {

	private static Logger logger = LoggerFactory
			.getLogger(DownloaderDelegateTest.class);

	@Test
	public void startServer() throws Exception {
		logger.info("START SERVER");

		int port = 8080;
		Server server = new Server(port);

		ServletContextHandler context = new ServletContextHandler(server, "/",
				ServletContextHandler.SESSIONS);
		context.addFilter(GuiceFilter.class, "/*", EnumSet
				.<javax.servlet.DispatcherType> of(
						javax.servlet.DispatcherType.REQUEST,
						javax.servlet.DispatcherType.ASYNC));

		context.addServlet(DefaultServlet.class, "/*");

		server.start();

		server.join();
		logger.info("SERVER STARTED");
	}

}
