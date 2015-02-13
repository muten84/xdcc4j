package it.luigibifulco.xdcc4j.search.service;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.SearchEngineFactory;
import it.luigibifulco.xdcc4j.search.XdccSearchEngine;
import it.luigibifulco.xdcc4j.search.XdccSearchFactory;
import it.luigibifulco.xdcc4j.search.cache.XdccCache;
import it.luigibifulco.xdcc4j.search.engine.SearchEngineType;
import it.luigibifulco.xdcc4j.search.parser.XdccFinderParser;
import it.luigibifulco.xdcc4j.search.parser.XdccItParser;
import it.luigibifulco.xdcc4j.search.query.XdccQueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SearchServiceImpl implements SearchService {

	@Inject
	private XdccSearchFactory searchTypeFactory;

	@Inject
	private SearchEngineFactory engineFactory;

	@Inject
	private XdccCache cache;

	private ExecutorService worker;

	public SearchServiceImpl() {
		worker = Executors.newFixedThreadPool(1);
	}

	@Override
	public List<XdccRequest> search(String where, String what) {
		XdccSearchEngine engine = null;
		SearchEngineType type = SearchEngineType.typeOf(where);
		if (type == null) {
			XdccRequest req = new XdccRequest();
			req.setDescription(what);
			return cache.search(req);
			// cache.search(like)
		} else {
			// remote search
			switch (type) {
			case xdcc_it:
				engine = engineFactory.http(type,
						Arrays.asList(new String[] { "q" }), "+",
						new XdccItParser());
				break;
			case xdccfinder:
				engine = engineFactory.http(type,
						Arrays.asList(new String[] { "search" }), " ",
						new XdccFinderParser());
			default:
				throw new RuntimeException("Search type not supported: "
						+ where + " - " + what);

			}

		}
		if (engine != null) {
			return new ArrayList<XdccRequest>(searchTypeFactory.create(engine)
					.search(XdccQueryBuilder.create().params(what)));
		}
		return null;
	}

	@Override
	public boolean reindex(final String server, final String channel,
			final String user, boolean synch) {
		Future f = worker.submit(new Runnable() {

			@Override
			public void run() {
				if (StringUtils.isEmpty(user)) {
					cache.cacheFrom(server, channel);
				} else {
					boolean cached = cache.cacheFrom(server, channel, user);
				}

				cache.persistCache();
			}
		});
		if (synch) {
			try {
				f.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;

	}
}
