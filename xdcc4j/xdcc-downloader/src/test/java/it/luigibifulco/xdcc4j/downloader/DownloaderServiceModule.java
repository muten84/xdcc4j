package it.luigibifulco.xdcc4j.downloader;

import it.luigibifulco.xdcc4j.downloader.service.DownloaderService;
import it.luigibifulco.xdcc4j.search.SearchModule;

import com.google.inject.AbstractModule;

public class DownloaderServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new SearchModule());
		bind(XdccDownloader.class).toInstance(new DownloaderService("./build"));
	}

}
