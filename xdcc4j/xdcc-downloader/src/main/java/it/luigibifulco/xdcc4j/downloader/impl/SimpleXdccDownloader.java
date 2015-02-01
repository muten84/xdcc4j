package it.luigibifulco.xdcc4j.downloader.impl;

import it.biffi.jirc.bot.BotException;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.common.util.XdccRequestCreator;
import it.luigibifulco.xdcc4j.downloader.XdccDownloader;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer.FileTransferStatusListener;
import it.luigibifulco.xdcc4j.ft.impl.XdccFileTransferImpl;
import it.luigibifulco.xdcc4j.search.XdccQueryBuilder;
import it.luigibifulco.xdcc4j.search.XdccSearch;
import it.luigibifulco.xdcc4j.search.impl.HttpXdccSearch;
import it.luigibifulco.xdcc4j.search.impl.HttpXdccSearchEngine;
import it.luigibifulco.xdcc4j.search.impl.XdccItParser;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleXdccDownloader implements XdccDownloader {

	Logger logger = LoggerFactory.getLogger(SimpleXdccDownloader.class);

	protected XdccSearch searcher;

	protected XdccFileTransfer ft;

	protected String searchDomain;

	private Map<String, XdccRequest> currentResult;

	private Map<String, Download> downloads = new HashMap<String, Download>();

	public SimpleXdccDownloader(String searchDomain) {
		this.searchDomain = searchDomain;
		currentResult = new HashMap<String, XdccRequest>();
		searcher = new HttpXdccSearch(searchDomain);

	}

	@Override
	public Map<String, XdccRequest> search(String text) {
		Map<String, XdccRequest> map = new HashMap<String, XdccRequest>();
		Set<XdccRequest> result = searcher.search(XdccQueryBuilder.create()
				.to(searchDomain).params(text));
		int cnt = 0;
		Iterator<XdccRequest> iter = result.iterator();
		while (iter.hasNext()) {
			map.put("" + cnt, iter.next());
			cnt++;
		}
		currentResult = map;
		return map;
	}

	@Override
	public String startDownload(String id) {
		return tryToStart(id) != null ? id : null;
	}

	protected Download tryToStart(final String k) {
		XdccRequest req = currentResult.get(k);
		try {

			req.setDestination("/Users/Luigi/Downloads/irc/");
			XdccFileTransfer ft = new XdccFileTransferImpl(req, 5000, 10000);
			FileTransferStatusListener l = new FileTransferStatusListener() {

				@Override
				public void onStart() {
					logger.info("Download " + k + " started: " + req);

				}

				@Override
				public void onProgress(int perc, int rate) {
					logger.info("onProgress for " + k + " : " + perc + " - "
							+ rate);

				}

				@Override
				public void onFinish() {
					logger.info("Download " + k + " finished: " + req);
					ft.cancel();

				}

				@Override
				public void onError(Throwable e) {
					logger.error("Download " + k + " aborted: " + req, e);
					ft.cancel();

				}
			};
			boolean result = ft.start(l);
			if (!result) {
				ft.cancel();
			} else {
				Download d = new Download(req.toString(), ft, l);
				downloads.put(k, d);
				return d;
			}
		} catch (BotException e) {
			logger.error("ERROR", e);
		}
		return null;

	}

	@Override
	public String startAnyAvailableFromList() {
		Map<String, XdccRequest> lastResult = new HashMap<String, XdccRequest>(
				currentResult);
		Collection<String> keys = lastResult.keySet();
		for (String k : keys) {
			if (tryToStart(k) != null)
				return k;
		}
		return null;
	}

	@Override
	public String cancelDownload(String id) {
		Download d = downloads.get(id);
		if (d != null) {
			d.getCurrentTransfer().cancel();
		}
		downloads.remove(id);
		return id;
	}

	@Override
	public List<String> cancelAll() {
		return null;
	}

	@Override
	public Download getDownload(String id) {
		return downloads.get(id);
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage download xdcc.it mutant_chronicles 7");
			return;
		}
		SimpleXdccDownloader d = new SimpleXdccDownloader("xdcc.it");
		d.search(args[0].replace("_", " "));
		if (args.length > 1) {
			d.startDownload(args[1]);
		} else {
			String id = d.startAnyAvailableFromList();
			System.out.println("Started >>>>> " + id);
		}
	}
}
