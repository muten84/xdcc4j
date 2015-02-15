package it.luigibifulco.xdcc4j.downloader.service;

import it.luigibifulco.xdcc4j.downloader.service.delegate.rest.DownloaderDelegate;
import it.luigibifulco.xdcc4j.downloader.service.servlet.websocket.DownloadEventServlet;
import it.luigibifulco.xdcc4j.downloader.service.servlet.websocket.WsFactory;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.eclipse.jetty.servlet.DefaultServlet;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class DownloaderServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(DefaultServlet.class).in(Singleton.class);
		bind(DownloadEventServlet.class).in(Singleton.class);
		bind(DownloaderDelegate.class).in(Singleton.class);
		bind(WsFactory.class).in(Singleton.class);
		ServletContext ctx = getServletContext();
		// ServerContainer wscontainer = WebSocketServerContainerInitializer
		// .configureContext();
		bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
		bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);

		HashMap<String, String> options = new HashMap<>();
		options.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		serve("/services/*").with(GuiceContainer.class, options);
		serve("/events/*").with(DownloadEventServlet.class);
	}
}
