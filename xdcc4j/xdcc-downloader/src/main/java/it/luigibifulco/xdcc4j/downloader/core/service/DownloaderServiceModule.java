package it.luigibifulco.xdcc4j.downloader.core.service;

import it.luigibifulco.xdcc4j.downloader.core.XdccDownloader;
import it.luigibifulco.xdcc4j.search.SearchModule;

import com.google.inject.AbstractModule;

public class DownloaderServiceModule extends AbstractModule {
	private String destinationDir;

	public DownloaderServiceModule() {
		this.destinationDir = "./";
	}

	public DownloaderServiceModule(String destinationDir) {
		this.destinationDir = destinationDir;
	}

	@Override
	protected void configure() {
		install(new SearchModule());
		bind(XdccDownloader.class).toInstance(
				new DownloaderCore(destinationDir));
	}

}
