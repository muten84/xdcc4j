package it.luigibifulco.xdcc4j.downloader.service.delegate.rest;

import java.util.HashMap;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.eclipse.jetty.servlet.DefaultServlet;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class DownloaderRestModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(DefaultServlet.class).in(Singleton.class);
		bind(DownloaderDelegate.class).in(Singleton.class);
		
		bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
	    bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);
		
		HashMap<String, String> options = new HashMap<>();
		options.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		serve("/*").with(GuiceContainer.class, options);
	}
}
