package it.luigibifulco.xdcc4j.downloader.core;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.downloader.core.model.Download;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface XdccDownloader {

	public Collection<Download> getAllDownloads();

	public boolean setServer(String server);

	public Download getDownload(String id);

	public int cleanSearch();

	public Map<String, XdccRequest> search(String where, String what);

	public String startDownload(String id);

	public String startAnyAvailableFromList();

	public String cancelDownload(String id);

	public List<String> cancelAll();

	public Map<String, XdccRequest> cache();

	public boolean reindex(String channel, String user);

}
