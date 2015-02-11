package it.luigibifulco.xdcc4j.search.impl;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.XdccSearch;
import it.luigibifulco.xdcc4j.search.XdccSearchEngine;
import it.luigibifulco.xdcc4j.search.XdccSearchEngineFactory;
import it.luigibifulco.xdcc4j.search.query.XdccQuery;
import it.luigibifulco.xdcc4j.search.query.XdccQuery.QueryCondition;
import it.luigibifulco.xdcc4j.search.query.XdccQuery.QueryFilter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Luigi
 *
 */
public class XdccSearchImpl implements XdccSearch {

	protected XdccSearchEngine engine;

	private final String searchDomain;

	public XdccSearchImpl(String searchDomain) {
		engine = XdccSearchEngineFactory.create(searchDomain);
		this.searchDomain = searchDomain;
	}

	@Override
	public Set<XdccRequest> search(XdccQuery query) throws RuntimeException {
		if (searchDomain != null) {
			query = query.to(searchDomain);
		}
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
