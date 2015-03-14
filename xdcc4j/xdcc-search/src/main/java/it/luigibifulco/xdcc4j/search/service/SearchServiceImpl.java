package it.luigibifulco.xdcc4j.search.service;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.SearchEngineFactory;
import it.luigibifulco.xdcc4j.search.XdccSearchEngine;
import it.luigibifulco.xdcc4j.search.XdccSearchFactory;
import it.luigibifulco.xdcc4j.search.cache.XdccCache;
import it.luigibifulco.xdcc4j.search.engine.SearchEngineType;
import it.luigibifulco.xdcc4j.search.parser.CmPlusParser;
import it.luigibifulco.xdcc4j.search.parser.XdccFinderParser;
import it.luigibifulco.xdcc4j.search.parser.XdccItParser;
import it.luigibifulco.xdcc4j.search.query.XdccQueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	private Map<String, XdccSearchEngine> engines;

	@Inject
	public SearchServiceImpl(SearchEngineFactory engineFactory) {
		this.engineFactory = engineFactory;
		worker = Executors.newFixedThreadPool(1);
		engines = new HashMap<String, XdccSearchEngine>();
		SearchEngineType[] values = SearchEngineType.values();
		for (SearchEngineType type : values) {
			XdccSearchEngine engine = null;
			switch (type) {
			case xdcc_it:
				engine = engineFactory.http(type,
						Arrays.asList(new String[] { "q" }), "+",
						new XdccItParser());
				engines.put(type.name(), engine);
				continue;
			case xdccfinder:
				engine = engineFactory.http(type,
						Arrays.asList(new String[] { "search" }), " ",
						new XdccFinderParser());
				engines.put(type.name(), engine);
				continue;
			case cmplus_on_crocmax:
				engine = engineFactory.http(type,
						Arrays.asList(new String[] { "func", "q" }), "+",
						new CmPlusParser());
				engines.put(type.name(), engine);
				continue;
			}
		}
	}

	@Override
	public List<XdccRequest> searchAll(String what) {
		List<XdccRequest> result = new ArrayList<XdccRequest>();
		Collection<XdccSearchEngine> searchEngines = engines.values();
		for (XdccSearchEngine xdccSearchEngine : searchEngines) {
			if (xdccSearchEngine.getType().equals(
					SearchEngineType.cmplus_on_crocmax.name())
					|| xdccSearchEngine.getType().equals(
							SearchEngineType.cmplus_on_crocmax.toString())) {
				result.addAll(new ArrayList<XdccRequest>(searchTypeFactory
						.create(xdccSearchEngine).search(
								XdccQueryBuilder.create().params("1", what))));
			} else {
				result.addAll(new ArrayList<XdccRequest>(searchTypeFactory
						.create(xdccSearchEngine).search(
								XdccQueryBuilder.create().params(what))));
			}
		}
		Collections.sort(result, new Comparator<XdccRequest>() {
			@Override
			public int compare(XdccRequest o1, XdccRequest o2) {
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});
		return result;
	}

	@Override
	public List<XdccRequest> search(String where, String... what) {
		XdccSearchEngine engine = null;
		SearchEngineType type = SearchEngineType.typeOf(where);
		if (type == null) {
			XdccRequest req = new XdccRequest();
			req.setDescription(what[0]);
			return cache.search(req);
			// cache.search(like)
		} else {
			// remote search
			engine = engines.get(where);
		}
		if (engine != null) {
			return new ArrayList<XdccRequest>(searchTypeFactory.create(engine)
					.search(XdccQueryBuilder.create().params(what)));
		}
		return null;
	}

	@Override
	// TODO: provide a callback for this method!
	public boolean reindex(final String server, final String channel,
			final String user, boolean synch) {
		Future f = worker.submit(new Runnable() {

			@Override
			public void run() {
				if (StringUtils.isEmpty(user)) {
					boolean cached = cache.cacheFrom(server, channel);
				} else {
					boolean cached = cache.cacheFrom(server, channel, user);
				}
				System.out
						.println(">>>> cache operation complete<<<< persisting....");
				boolean persisted = cache.persistCache();
				System.out.println("persisted: " + persisted);
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
