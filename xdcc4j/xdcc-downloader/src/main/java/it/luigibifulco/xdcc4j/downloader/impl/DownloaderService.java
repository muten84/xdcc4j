package it.luigibifulco.xdcc4j.downloader.impl;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.downloader.XdccDownloader;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer.FileTransferStatusListener;
import it.luigibifulco.xdcc4j.ft.impl.FileTrasnferFactory;
import it.luigibifulco.xdcc4j.search.XdccSearch;
import it.luigibifulco.xdcc4j.search.query.XdccQuery;
import it.luigibifulco.xdcc4j.search.query.XdccQueryBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.inject.Inject;

public class DownloaderService implements XdccDownloader {

	@Inject
	private Map<String, XdccSearch> searchTypesMap;

	private String currentServer;

	private Map<String, XdccRequest> searchResult;

	private ConcurrentHashMap<String, Download> downloadMap;

	private String incomingDir;

	public DownloaderService(String incominDir) {
		searchResult = new HashMap<String, XdccRequest>();
		downloadMap = new ConcurrentHashMap<String, Download>();
		this.incomingDir = incominDir;
	}

	@Override
	public boolean setServer(String server) {
		this.currentServer = server;
		return true;
	}

	@Override
	public Download getDownload(String id) {
		return downloadMap.get(id);
	}

	@Override
	public Map<String, XdccRequest> search(String where, String text) {
		Map<String, XdccRequest> searchResult = new HashMap<String, XdccRequest>();
		XdccSearch seeker = searchTypesMap.get(where);
		if (seeker == null) {
			throw new RuntimeException(where + " search engine not supported");
		}

		XdccQuery query = XdccQueryBuilder.create().to(where).params(text);
		Set<XdccRequest> result = seeker.search(query);
		for (XdccRequest xdccRequest : result) {
			if (xdccRequest == null) {
				continue;
			}
			xdccRequest.setDestination(incomingDir);
			String id = xdccRequest.getChannel() + ";" + xdccRequest.getPeer()
					+ ";" + xdccRequest.getResource();
			searchResult.put(id, xdccRequest);
		}
		this.searchResult.putAll(searchResult);
		return searchResult;
	}

	@Override
	public String startDownload(final String id) {
		XdccRequest request = cache().get(id);
		if (request == null) {
			return null;
		}
		if (incomingDir == null || incomingDir.isEmpty()) {
			throw new RuntimeException("incoming dir is empty");
		}
		if (currentServer != null && !currentServer.isEmpty()) {
			request.setHost(currentServer);
		}
		XdccFileTransfer xft = FileTrasnferFactory.createFileTransfer(request);
		final Download d = new Download(id, xft, null);
		downloadMap.put(id, d);
		xft.start(new FileTransferStatusListener() {

			@Override
			public void onStart() {
				d.setStatusListener(this);

			}

			@Override
			public void onProgress(int perc, int rate) {
				if (getDownload(id) == null) {
					return;
				}
				downloadMap.get(id).setPercentage(perc);
				downloadMap.get(id).setRate(rate);

			}

			@Override
			public void onFinish() {
				if (getDownload(id) == null) {
					return;
				}
				downloadMap.remove(id);

			}

			@Override
			public void onError(Throwable e) {
				if (getDownload(id) == null) {
					return;
				}
				downloadMap.remove(id);
			}
		});

		return id;

	}

	@Override
	public String startAnyAvailableFromList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cancelDownload(String id) {
		if (getDownload(id) == null) {
			return null;
		}
		downloadMap.get(id).getCurrentTransfer().cancel();
		return id;
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
