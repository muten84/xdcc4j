package it.luigibifulco.xdcc4j.downloader.core.service;

import it.luigibifulco.xdcc4j.common.util.OsUtils;
import it.luigibifulco.xdcc4j.downloader.core.XdccDownloader;
import it.luigibifulco.xdcc4j.search.SearchModule;

import com.google.inject.AbstractModule;

public class DownloaderServiceModule extends AbstractModule {
	private String destinationDir;

	private String incomingDir;

	public DownloaderServiceModule() {
		this.destinationDir = OsUtils.getUserHomeDir()
				+ OsUtils.getPathDirSeparator() + ".xdcc-cache";
		this.incomingDir = OsUtils.getDownloadDir();

	}

//	public DownloaderServiceModule(String destinationDir) {
//		this.destinationDir = destinationDir;
//	}

	@Override
	protected void configure() {
		install(new SearchModule());
		bind(XdccDownloader.class).toInstance(
				new DownloaderCore(destinationDir, incomingDir));
	}

}
