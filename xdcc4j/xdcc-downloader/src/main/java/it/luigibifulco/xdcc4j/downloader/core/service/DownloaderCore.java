package it.luigibifulco.xdcc4j.downloader.core.service;

import it.biffi.jirc.bot.BotClientConfig;
import it.biffi.jirc.bot.BotException;
import it.biffi.jirc.bot.SearchBot;
import it.luigibifulco.xdcc4j.common.model.DownloadBean;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.downloader.core.XdccDownloader;
import it.luigibifulco.xdcc4j.downloader.core.model.Download;
import it.luigibifulco.xdcc4j.downloader.core.util.ConvertUtil;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer.FileTransferStatusListener;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer.TransferState;
import it.luigibifulco.xdcc4j.ft.impl.FileTransferFactory;
import it.luigibifulco.xdcc4j.search.cache.XdccCache;
import it.luigibifulco.xdcc4j.search.service.SearchService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class DownloaderCore implements XdccDownloader {

	// @Inject
	// private Map<String, XdccSearch> searchTypesMap;
	@Inject
	private SearchService searchService;

	@Inject
	private XdccCache cache;

	private String currentServer;

	private Map<String, XdccRequest> searchResult;

	private ConcurrentHashMap<String, Download> downloadMap;

	private String incomingDir;

	private ExecutorService workers;

	private ConcurrentHashMap<String, DownloadListener> listenerRegistry;

	private ExecutorService queueWorkers;

	private static Logger logger = LoggerFactory
			.getLogger(DownloaderCore.class);

	public DownloaderCore(String incominDir) {
		searchResult = new HashMap<String, XdccRequest>();
		downloadMap = new ConcurrentHashMap<String, Download>();
		this.incomingDir = incominDir;
		listenerRegistry = new ConcurrentHashMap<String, DownloadListener>();
		workers = Executors.newCachedThreadPool();
		queueWorkers = Executors.newFixedThreadPool(1);

	}

	@Override
	public int refresh() {
		logger.info("refresh");
		Collection<DownloadBean> downloads = cache.getDownloadsFromCache();
		for (DownloadBean downloadBean : downloads) {
			Download d = new Download(downloadBean.getId(),
					downloadBean.getDesc(), null, null);
			d.setPercentage((int) downloadBean.getPerc());
			d.setRate((int) downloadBean.getRate());
			d.setState(downloadBean.getState());
			downloadMap.put(d.getId(), d);
		}
		logger.info("refresh: " + downloadMap.size());
		return downloadMap.size();
	}

	@Override
	public boolean setServer(String server) {
		logger.info("setServer: " + server);
		this.currentServer = server;
		return true;
	}

	@Override
	public Download getDownload(String id) {
		logger.info("getDownload: " + id);
		return downloadMap.get(id);
	}

	@Override
	public Map<String, XdccRequest> search(String where, String... text) {
		logger.info("search: " + where + " - " + Arrays.asList(text));
		Map<String, XdccRequest> searchResult = new HashMap<String, XdccRequest>();
		List<XdccRequest> result = null;
		if (StringUtils.isEmpty(where)) {
			result = searchService.searchAll(text[0]);
		} else {
			result = searchService.search(where, text);
		}

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
		logger.info("startDownload: " + id);
		DownloadBean download = null;
		XdccRequest request = cache().get(id);
		if (request == null) {
			// try fetch from local
			request = cache.getRequest(id);
			if (request == null) {
				// TODO: memorizzare nel db dei downloads tutte le info per
				// ricostruire la xdccrequest se non dovesse
				// essere in cache
				download = cache.getDownload(id);
				if (download == null) {
					return null;
				}
			}
			if (request != null
					&& StringUtils.isEmpty(request.getDestination())) {
				request.setDestination(incomingDir);
			}

		}
		if (StringUtils.isEmpty(incomingDir)) {
			throw new RuntimeException("incoming dir is empty");
		}
		if (!StringUtils.isEmpty(currentServer)) {
			if (request != null && StringUtils.isEmpty(request.getHost())) {
				request.setHost(currentServer);
			}
		}
		if (downloadMap.get(id) != null
				&& (downloadMap.get(id).getState().equals(TransferState.WORKING
						.name()))) {
			// download tranfer is WORKING so download was just started
			return id;
		}
		if (download != null) {
			request = new XdccRequest();
			request.setId(download.getId());
			request.setChannel(download.getChannel());
			request.setHost(download.getServer());
			request.setDescription(download.getDesc());
			request.setPeer(download.getFrom());
			request.setResource(download.getResource());
			if (StringUtils.isEmpty(request.getDestination())) {
				request.setDestination(incomingDir);
			}
			// request.setTtl(ttl);
		}
		XdccFileTransfer xft = FileTransferFactory.createFileTransfer(request);

		final Download d = new Download(request.getId(),
				request.getDescription(), xft, null);
		if (downloadMap.containsKey(id)) {
			downloadMap.replace(id, d);
		} else {
			downloadMap.put(id, d);
		}

		boolean downloadPersisted = cache.putDownloadInCache(ConvertUtil
				.convert(d));
		logger.info(">>>download persisted: " + downloadPersisted);
		workers.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return xft.start(new FileTransferStatusListener() {

					@Override
					public void onStart() {
						d.setStatusListener(this);
						Download d = downloadMap.get(id);
						// d.getCurrentTransfer().getState().name();
						DownloadListener listener = listenerRegistry.get(id);

						Callable<Boolean> task = () -> {
							listener.onDownloadStausUpdate(id, "start", 0, 0);
							return true;
						};
						queueWorkers.submit(task);

					}

					@Override
					public void onProgress(int perc, int rate) {
						if (getDownload(id) == null) {
							return;
						}
						downloadMap.get(id).setState(
								TransferState.WORKING.name());
						downloadMap.get(id).setPercentage(perc);
						downloadMap.get(id).setRate(rate);
						DownloadListener listener = listenerRegistry.get(id);

						Callable<Boolean> task = () -> {
							listener.onDownloadStausUpdate(id, "progress",
									perc, rate);
							return true;
						};
						queueWorkers.submit(task);

					}

					@Override
					public void onFinish() {
						if (getDownload(id) == null) {
							return;
						}
						downloadMap.get(id).setState(
								TransferState.FINISHED.name());
						// cache.removeDownloadFromCache(ConvertUtil.convert(d));
						// downloadMap.remove(id);
						DownloadListener listener = listenerRegistry.get(id);

						Callable<Boolean> task = () -> {
							listener.onDownloadStausUpdate(id, "finish", 100,
									100);
							return true;
						};
						queueWorkers.submit(task);

					}

					@Override
					public void onError(Throwable e) {
						if (getDownload(id) == null) {
							return;
						}
						downloadMap.get(id).setState(
								TransferState.ABORTED.name());
						if (downloadMap.get(id).getCurrentTransfer() != null) {
							try {
								downloadMap.get(id).getCurrentTransfer()
										.cancel();
							} catch (Exception ex) {

							}
							downloadMap.get(id).setCurrentTransfer(null);
							downloadMap.get(id).setState(
									TransferState.ABORTED.name());
						}
						// cache.removeDownloadFromCache(ConvertUtil.convert(d));
						// Download d = downloadMap.remove(id);
						DownloadListener listener = listenerRegistry.get(id);

						Callable<Boolean> task = () -> {
							listener.onDownloadStausUpdate(id, "error",
									d.getPercentage(), d.getRate());
							return true;
						};
						queueWorkers.submit(task);

					}

					@Override
					public void onStatusUpdate(String status) {
						DownloadListener listener = listenerRegistry.get(id);

						Callable<Boolean> task = () -> {
							listener.onDownloadStausUpdate(id, status,
									d.getPercentage(), d.getRate());
							return true;
						};
						queueWorkers.submit(task);

					}

					@Override
					public void onCancel() {
						DownloadListener listener = listenerRegistry.get(id);

						Callable<Boolean> task = () -> {
							listener.onDownloadStausUpdate(id, "cancel",
									d.getPercentage(), d.getRate());
							return true;
						};
						queueWorkers.submit(task);

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
		logger.info("cancelDownload: " + id);

		if (downloadMap.get(id) != null
				&& downloadMap.get(id).getCurrentTransfer() != null) {
			downloadMap.get(id).getCurrentTransfer().cancel();
		}
		downloadMap.get(id).setState(TransferState.ABORTED.name());

		/* commented should fix not removed downloads... */
		// cache.putDownloadInCache(ConvertUtil.convert(downloadMap.get(id)));
		/* end */

		// should fix issue for download recover in session
		DownloadListener listener = listenerRegistry.get(id);

		Callable<Boolean> task = () -> {
			listener.onDownloadStausUpdate(id, "start", 0, 0);
			return true;
		};
		queueWorkers.submit(task);
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
		logger.info("cleanSearch");
		int result = searchResult.size();
		searchResult.clear();
		return result;
	}

	@Override
	public Map<String, XdccRequest> cache() {
		logger.info("cache");
		return new HashMap<String, XdccRequest>(this.searchResult);
	}

	@Override
	public Collection<Download> getAllDownloads() {
		logger.info("getAllDownloads");
		Collection<DownloadBean> beans = cache.getDownloadsFromCache();
		if (beans == null) {
			beans = new ArrayList<DownloadBean>();
		}
		// if (beans.size() == downloadMap.values().size()) {
		// return downloadMap.values();
		// } else {
		// throw new RuntimeException(
		// "session and state are not synchornized: " + beans.size()
		// + " - " + downloadMap.values().size());
		// }
		return downloadMap.values();
	}

	@Override
	public boolean reindex(String channel, String user) {
		logger.info("reindex: " + channel + " - " + user);
		return searchService.reindex(this.currentServer, channel, user, false);
	}

	@Override
	public void addDownloadStatusListener(String downloadId,
			DownloadListener listener) {
		logger.info("addDownloadStatusListener: " + downloadId + " - "
				+ listener);

		listenerRegistry.put(downloadId, listener);

	}

	@Override
	public void removeDownloadStatusListener(String downloadId) {
		logger.info("addDownloadStatusListener: " + downloadId);
		listenerRegistry.remove(downloadId);

	}

	@Override
	public List<String> listChannels() {
		logger.info("listChannels");
		SearchBot bot = new SearchBot(false);
		BotClientConfig config = new BotClientConfig();
		config.setServer(this.currentServer);
		config.setNick("xdccBot" + UUID.randomUUID().toString().substring(0, 6));
		try {
			bot.start(config);
		} catch (BotException e) {
			return new ArrayList<String>();
		}
		List<String> list = bot.listChannels();
		bot.stop();
		return list;
	}

	@Override
	public List<String> listUsers(String channel) {
		logger.info("listUsers in: " + channel);

		SearchBot bot = new SearchBot(false);
		BotClientConfig config = new BotClientConfig();
		config.setServer(this.currentServer);
		config.setNick("xdccBot" + UUID.randomUUID().toString().substring(0, 6));
		try {
			bot.start(config);
		} catch (BotException e) {
			return new ArrayList<String>();
		}
		List<String> list = bot.listUsersInChannel(channel);
		bot.stop();
		return list;
	}

	@Override
	public String isConnected() {
		return "irc.crocmax.net";
	}

	@Override
	public boolean removeDownload(String id) {
		logger.info("removeDownload: " + id);
		Download d = getDownload(id);
		if (d == null) {
			return false;
		}
		boolean removed = false;
		if (d != null) {
			String state = d.getState();
			if (StringUtils.defaultIfEmpty(state, "").equals(
					TransferState.WORKING.name())) {
				logger.warn("for download with id: "
						+ id
						+ " there is a remove request discarded cause download status is incompatible: "
						+ state);
				return false;
			}

			Collection<DownloadBean> beans = cache.getDownloadsFromCache();
			for (DownloadBean downloadBean : beans) {
				if (downloadBean.getId().equals(id)) {
					try {
						// cache.removeDownloadFromCache(downloadBean);
						removed = cache.removeDownloadById(id);
					} catch (Exception e) {
						logger.error("error while removing: " + e.getMessage());
						removed = false;
					}

				}
			}
			if (removed) {
				downloadMap.remove(id);
			}
		}
		return removed;
	}

	@Override
	public int resumeAllDownloads() {
		Collection<Download> downloads = getAllDownloads();
		for (Download downloadBean : downloads) {
			if (!downloadBean.getState().equals(TransferState.RUNNABLE.name())
					&& !downloadBean.getState().equals(
							TransferState.WORKING.name())
					&& !downloadBean.getState().equals(
							TransferState.FINISHED.name())) {
				startDownload(downloadBean.getId());
			} else {
				logger.info("no need to resume download "
						+ downloadBean.getDescription());
			}

		}
		return downloads.size();
	}
}
