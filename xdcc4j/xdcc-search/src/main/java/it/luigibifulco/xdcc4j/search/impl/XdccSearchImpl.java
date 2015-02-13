package it.luigibifulco.xdcc4j.search.impl;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.XdccSearch;
import it.luigibifulco.xdcc4j.search.XdccSearchEngine;
import it.luigibifulco.xdcc4j.search.query.XdccQuery;
import it.luigibifulco.xdcc4j.search.query.XdccQuery.QueryCondition;
import it.luigibifulco.xdcc4j.search.query.XdccQuery.QueryFilter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 * @author Luigi
 *
 */
public class XdccSearchImpl implements XdccSearch {

	private final String searchDomain;

	private XdccSearchEngine engine;

	@Inject
	public XdccSearchImpl(@Assisted XdccSearchEngine engine) {
		this.engine = engine;
		// switch (searchDomain) {
		// case "xdcc.it":
		// engine = factory.html(Arrays.asList(new String[] { "q" }), "+",
		// new XdccItParser());
		// break;
		// case "xdccfinder.it":
		// engine = factory.html(Arrays.asList(new String[] { "search" }),
		// " ", new XdccFinderParser());
		// break;
		// default:
		// engine = null;
		// break;
		// }

		this.searchDomain = engine.getType();
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
				if (StringUtils.defaultIfEmpty(xdccRequest.getHost(), "")
						.equals(what)) {
					xdccRequest
							.setHost(map.get(QueryCondition.HOST.toString()));
				}

			}
		}
		return requests;
	}
}
