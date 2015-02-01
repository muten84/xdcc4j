package it.luigibifulco.xdcc4j.search.impl;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.XdccQuery;
import it.luigibifulco.xdcc4j.search.XdccSearch;
import it.luigibifulco.xdcc4j.search.XdccSearchEngine;
import it.luigibifulco.xdcc4j.search.XdccSearchEngineFactory;
import it.luigibifulco.xdcc4j.search.XdccQuery.QueryCondition;
import it.luigibifulco.xdcc4j.search.XdccQuery.QueryFilter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HttpXdccSearch implements XdccSearch {

	protected XdccSearchEngine engine;

	public HttpXdccSearch(String searchDomain) {
		engine = XdccSearchEngineFactory.create(searchDomain);
	}

	@Override
	public Set<XdccRequest> search(XdccQuery query) throws RuntimeException {
		Set<XdccRequest> requests = engine.search(query);
		if (requests == null || requests.isEmpty()) {
			return new HashSet<XdccRequest>();
		}
		Map<String, String> map = query.getQueryAsMap();
		String what = map.get(QueryFilter.HOST.toString());
		if (what != null) {
			for (XdccRequest xdccRequest : requests) {
				if (xdccRequest == null) {
					continue;
				}
				if (xdccRequest.getHost().equals(what)) {
					xdccRequest
							.setHost(map.get(QueryCondition.HOST.toString()));
				}

			}
		}
		return requests;
	}
}
