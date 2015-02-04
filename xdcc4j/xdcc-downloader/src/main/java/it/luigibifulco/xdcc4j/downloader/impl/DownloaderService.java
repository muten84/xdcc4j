package it.luigibifulco.xdcc4j.downloader.impl;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.downloader.XdccDownloader;
import it.luigibifulco.xdcc4j.search.XdccSearch;
import it.luigibifulco.xdcc4j.search.query.XdccQuery;
import it.luigibifulco.xdcc4j.search.query.XdccQueryBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

public class DownloaderService implements XdccDownloader {

	@Inject
	private Map<String, XdccSearch> searchTypesMap;

	private String currentServer;

	private Map<String, XdccRequest> searchResult;

	public DownloaderService() {
		searchResult = new HashMap<String, XdccRequest>();
	}

	@Override
	public boolean setServer(String server) {
		this.currentServer = server;
		return true;
	}

	@Override
	public Download getDownload(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, XdccRequest> search(String where, String text) {
		Map<String, XdccRequest> searchResult = new HashMap<String, XdccRequest>();
		XdccSearch seeker = searchTypesMap.get(where);
		XdccQuery query = XdccQueryBuilder.create().to(where).params(text);
		Set<XdccRequest> result = seeker.search(query);
		for (XdccRequest xdccRequest : result) {
			if (xdccRequest == null) {
				continue;
			}
			String id = xdccRequest.getChannel() + ";" + xdccRequest.getPeer()
					+ ";" + xdccRequest.getResource();
			searchResult.put(id, xdccRequest);
		}
		this.searchResult.putAll(searchResult);
		return searchResult;
	}

	@Override
	public String startDownload(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String startAnyAvailableFromList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cancelDownload(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> cancelAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int cleanSearch() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, XdccRequest> cache() {
		return this.searchResult;
	}

}
