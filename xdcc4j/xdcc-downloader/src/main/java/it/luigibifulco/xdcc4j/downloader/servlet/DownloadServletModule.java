package it.luigibifulco.xdcc4j.downloader.servlet;

import com.google.inject.servlet.ServletModule;

public class DownloadServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(DownloadServlet.class);
		serve("/download.service").with(DownloadServlet.class);
	}

}
