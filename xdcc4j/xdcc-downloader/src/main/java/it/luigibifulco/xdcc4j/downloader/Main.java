package it.luigibifulco.xdcc4j.downloader;

import it.luigibifulco.xdcc4j.downloader.core.service.DownloaderServiceModule;
import it.luigibifulco.xdcc4j.downloader.service.DownloaderServletModule;
import it.luigibifulco.xdcc4j.downloader.service.XdccDownloaderService;
import it.luigibifulco.xdcc4j.downloader.service.servlet.websocket.DownloaderEventNotifier;
import it.luigibifulco.xdcc4j.downloader.system.TrayIconHelper;

import java.util.EnumSet;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.websocket.server.ServerContainer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
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

	public static void startWebApp(String webAppPath) throws Exception {
		Server server = new Server(8080);
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/xdcc4j");
		webapp.setWar(webAppPath);
		server.setHandler(webapp);
		server.start();

		server.join();
	}

	private static void addTrayIcon() {
		/* Use an appropriate Look and Feel */
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		// Schedule a job for the event-dispatching thread:
		// adding TrayIcon.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TrayIconHelper.createAndShowGUI();
			}
		});
	}

	public static void main(String[] args) throws Exception {
		addTrayIcon();
		// URL url = Main.class.getResource("xdcc-ui-1.0.war");
		startWebApp(args[0]);
		// startDownloader();
		// startEventServer();
	}
}
