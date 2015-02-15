package it.luigibifulco.xdcc4j.downloader;

import it.luigibifulco.xdcc4j.downloader.core.service.DownloaderServiceModule;
import it.luigibifulco.xdcc4j.downloader.service.DownloaderServletModule;
import it.luigibifulco.xdcc4j.downloader.service.XdccDownloaderService;
import it.luigibifulco.xdcc4j.downloader.service.servlet.websocket.DownloaderEventNotifier;

import java.util.EnumSet;

import javax.websocket.server.ServerContainer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

public class Main {

	public static class DownloaderModule extends AbstractModule {
		protected void configure() {
			install(new DownloaderServiceModule("./"));
			install(new DownloaderServletModule());
		};
	}

	public static void startEventServer() {
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8080);
		server.addConnector(connector);

		// Setup the basic application "context" for this application at "/"
		// This is also known as the handler tree (in jetty speak)
		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		try {
			// Initialize javax.websocket layer
			ServerContainer wscontainer = WebSocketServerContainerInitializer
					.configureContext(context);

			// Add WebSocket endpoint to javax.websocket layer
			wscontainer.addEndpoint(DownloaderEventNotifier.class);

			server.start();
			server.dump(System.err);
			server.join();
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}

	public static void startDownloader() throws Exception {
		Injector inject = Guice.createInjector(new DownloaderModule());
		Server server = new Server(8080);

		ServletContextHandler context = new ServletContextHandler(server,
				"/xdcc4j", ServletContextHandler.SESSIONS);
		// ServletContextHandler wscontext = new ServletContextHandler(server,
		// "/events", ServletContextHandler.SESSIONS);
		// context.setContextPath("/");
		// wscontext.setContextPath("/events");
		context.addFilter(GuiceFilter.class, "/*", EnumSet
				.<javax.servlet.DispatcherType> of(
						javax.servlet.DispatcherType.REQUEST,
						javax.servlet.DispatcherType.ASYNC));

		context.addServlet(DefaultServlet.class, "/*");

		// ServerContainer wscontainer = WebSocketServerContainerInitializer
		// .configureContext(wscontext);

		// Add WebSocket endpoint to javax.websocket layer
		// wscontainer.addEndpoint(EventSocket.class);
		server.start();

		server.join();
		// lock.unlock();

		System.out.println("starte instance service"
				+ inject.getInstance(XdccDownloaderService.class));
	}

	public static void main(String[] args) throws Exception {
		startDownloader();
		// startEventServer();
	}
}
