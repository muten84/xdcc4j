package it.luigibifulco.xdcc4j.downloader;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.downloader.impl.Download;

import java.util.List;
import java.util.Map;

public interface XdccDownloader {

	public Download getDownload(String id);

	public Map<String, XdccRequest> search(String text);

	public String startDownload(String id);

	public String startAnyAvailableFromList();

	public String cancelDownload(String id);

	public List<String> cancelAll();

}
