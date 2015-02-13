package it.luigibifulco.xdcc4j.search.engine.local;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.db.XdccRequestStore;
import it.luigibifulco.xdcc4j.search.XdccSearchEngine;
import it.luigibifulco.xdcc4j.search.cache.XdccCache;
import it.luigibifulco.xdcc4j.search.query.XdccQuery;
import it.luigibifulco.xdcc4j.search.query.XdccQuery.QueryFilter;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

public class LocalSearch implements XdccSearchEngine {

	@Inject
	private XdccCache cache;

	@Override
	public Set<XdccRequest> search(XdccQuery query) throws RuntimeException {
		String desc = query.getQueryAsMap().get("params");
		XdccRequest filter = new XdccRequest();
		filter.setDescription(desc);
		return new HashSet<XdccRequest>(cache.search(filter));
	}

	@Override
	public String getType() {
		return "localhost";
	}

}
