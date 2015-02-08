package it.luigibifulco.xdcc4j.downloader;

import it.luigibifulco.xdcc4j.downloader.core.DownloaderServiceModule;
import it.luigibifulco.xdcc4j.downloader.service.XdccDownloaderService;
import it.luigibifulco.xdcc4j.downloader.service.delegate.rest.DownloaderRestModule;

import java.util.EnumSet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

public class Main {

	public static class DownloaderModule extends AbstractModule {
		protected void configure() {
			install(new DownloaderServiceModule("./"));
			install(new DownloaderRestModule());
		};
	}

	public static void main(String[] args) throws Exception {
		Injector inject = Guice.createInjector(new DownloaderModule());
		Server server = new Server(8080);

		ServletContextHandler context = new ServletContextHandler(server, "/",
				ServletContextHandler.SESSIONS);
		context.addFilter(GuiceFilter.class, "/*", EnumSet
				.<javax.servlet.DispatcherType> of(
						javax.servlet.DispatcherType.REQUEST,
						javax.servlet.DispatcherType.ASYNC));

		context.addServlet(DefaultServlet.class, "/*");

		server.start();

		server.join();
		// lock.unlock();

		System.out.println("starte instance service"
				+ inject.getInstance(XdccDownloaderService.class));

	}
}
