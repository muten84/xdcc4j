package it.luigibifulco.xdcc4j.downloader.service;

import it.luigibifulco.xdcc4j.downloader.service.model.DownloadBean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface XdccDownloaderService {

	public boolean setServer(String server);

	public Collection<DownloadBean> getAllDownloads();

	public DownloadBean getDownload(String id);

	public int cleanSearch();

	public Map<String, DownloadBean> search(String where, String what);

	public String startDownload(String id);

	public String startAnyAvailableFromList();

	public String cancelDownload(String id);

	public List<String> cancelAll();

	public Map<String, DownloadBean> cache();
}
