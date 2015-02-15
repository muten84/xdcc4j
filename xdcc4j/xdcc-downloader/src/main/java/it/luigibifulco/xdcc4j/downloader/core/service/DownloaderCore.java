package it.luigibifulco.xdcc4j.downloader.core.service;

import it.biffi.jirc.bot.SearchBot;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.downloader.core.XdccDownloader;
import it.luigibifulco.xdcc4j.downloader.core.XdccDownloader.DownloadListener;
import it.luigibifulco.xdcc4j.downloader.core.model.Download;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer.FileTransferStatusListener;
import it.luigibifulco.xdcc4j.ft.impl.FileTransferFactory;
import it.luigibifulco.xdcc4j.search.service.SearchService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.inject.Inject;

public class DownloaderCore implements XdccDownloader {

	// @Inject
	// private Map<String, XdccSearch> searchTypesMap;
	@Inject
	private SearchService searchService;

	private String currentServer;

	private Map<String, XdccRequest> searchResult;

	private ConcurrentHashMap<String, Download> downloadMap;

	private String incomingDir;

	private ExecutorService workers;

	private ConcurrentHashMap<String, List<DownloadListener>> listenerRegistry;

	private ExecutorService queueWorkers;

	public DownloaderCore(String incominDir) {
		searchResult = new HashMap<String, XdccRequest>();
		downloadMap = new ConcurrentHashMap<String, Download>();
		this.incomingDir = incominDir;
		listenerRegistry = new ConcurrentHashMap<String, List<XdccDownloader.DownloadListener>>();
		workers = Executors.newCachedThreadPool();
		queueWorkers = Executors.newFixedThreadPool(1);

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
		// XdccSearch seeker = searchTypesMap.get(where);
		// if (seeker == null) {
		// throw new RuntimeException(where + " search engine not supported");
		// }
		List<XdccRequest> result = searchService.search(where, text);
		// XdccQuery query = XdccQueryBuilder.create().to(where).params(text);
		// Set<XdccRequest> result = seeker.search(query);
		for (XdccRequest xdccRequest : result) {
			if (xdccRequest == null) {
				continue;
			}
			xdccRequest.setDestination(incomingDir);

			searchResult.put(xdccRequest.getId(), xdccRequest);
		}
		this.searchResult.clear();
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
		if (downloadMap.get(id) != null) {
			return id;
		}
		XdccFileTransfer xft = FileTransferFactory.createFileTransfer(request);
		final Download d = new Download(request.getId(),
				request.getDescription(), xft, null);
		downloadMap.put(id, d);
		workers.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return xft.start(new FileTransferStatusListener() {

					@Override
					public void onStart() {
						d.setStatusListener(this);
						List<DownloadListener> listeners = listenerRegistry
								.get(id);
						for (DownloadListener downloadListener : listeners) {
							Callable<Boolean> task = () -> {
								downloadListener.onDownloadStausUpdate(id,
										"start", 0, 0);
								return true;
							};
							queueWorkers.submit(task);
						}

					}

					@Override
					public void onProgress(int perc, int rate) {
						if (getDownload(id) == null) {
							return;
						}
						downloadMap.get(id).setPercentage(perc);
						downloadMap.get(id).setRate(rate);
						List<DownloadListener> listeners = listenerRegistry
								.get(id);
						for (DownloadListener downloadListener : listeners) {
							Callable<Boolean> task = () -> {
								downloadListener.onDownloadStausUpdate(id,
										"progress", perc, rate);
								return true;
							};
							queueWorkers.submit(task);
						}

					}

					@Override
					public void onFinish() {
						if (getDownload(id) == null) {
							return;
						}
						downloadMap.remove(id);
						List<DownloadListener> listeners = listenerRegistry
								.get(id);
						for (DownloadListener downloadListener : listeners) {
							Callable<Boolean> task = () -> {
								downloadListener.onDownloadStausUpdate(id,
										"finish", 100, 100);
								return true;
							};
							queueWorkers.submit(task);
						}
					}

					@Override
					public void onError(Throwable e) {
						if (getDownload(id) == null) {
							return;
						}
						Download d = downloadMap.remove(id);
						List<DownloadListener> listeners = listenerRegistry
								.get(id);
						for (DownloadListener downloadListener : listeners) {
							Callable<Boolean> task = () -> {
								downloadListener.onDownloadStausUpdate(id,
										"error", d.getPercentage(), d.getRate());
								return true;
							};
							queueWorkers.submit(task);
						}
					}

					@Override
					public void onStatusUpdate(String status) {
						List<DownloadListener> listeners = listenerRegistry
								.get(id);
						for (DownloadListener downloadListener : listeners) {
							Callable<Boolean> task = () -> {
								downloadListener.onDownloadStausUpdate(id,
										status, d.getPercentage(), d.getRate());
								return true;
							};
							queueWorkers.submit(task);
						}

					}
				});
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
		if (downloadMap != null && downloadMap.size() > 0) {
			KeySetView<String, Download> set = downloadMap.keySet();
			for (String string : set) {
				Download d = downloadMap.get(string);
				if (d != null)
					d.getCurrentTransfer().cancel();
			}
			return new ArrayList<String>(set);
		}
		return null;
	}

	@Override
	public int cleanSearch() {
		int result = searchResult.size();
		searchResult.clear();
		return result;
	}

	@Override
	public Map<String, XdccRequest> cache() {
		return new HashMap<String, XdccRequest>(this.searchResult);
	}

	@Override
	public Collection<Download> getAllDownloads() {
		return downloadMap.values();
	}

	@Override
	public boolean reindex(String channel, String user) {
		return searchService.reindex(this.currentServer, channel, user, false);
	}

	@Override
	public void addDownloadStatusListener(String downloadId,
			DownloadListener listener) {
		List<DownloadListener> list = listenerRegistry.get(downloadId);
		if (list == null) {
			list = new ArrayList<XdccDownloader.DownloadListener>();
		}
		list.add(listener);
		listenerRegistry.put(downloadId, list);

	}

	@Override
	public void removeDownloadStatusListener(String downloadId) {
		listenerRegistry.remove(downloadId);

	}

	@Override
	public List<String> listChannels() {
		SearchBot bot = new SearchBot(false);
		List<String> list = bot.listChannels();
		bot.stop();
		return list;
	}

	@Override
	public List<String> listUsers(String channel) {
		SearchBot bot = new SearchBot(false);
		List<String> list = bot.listUsersInChannel(channel);
		bot.stop();
		return list;
	}
}
