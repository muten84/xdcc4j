package it.luigibifulco.xdcc4j.search.http;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.http.HttpXdccSearch;
import it.luigibifulco.xdcc4j.search.query.XdccQuery;
import it.luigibifulco.xdcc4j.search.query.XdccQueryBuilder;
import it.luigibifulco.xdcc4j.search.query.XdccQuery.QueryCondition;
import it.luigibifulco.xdcc4j.search.query.XdccQuery.QueryFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpXdccSearchTest {

	private HttpXdccSearch xdccItSearch;
	private HttpXdccSearch xdccFinderSearch;

	@Before
	public final void before() {
		xdccItSearch = new HttpXdccSearch("xdcc.it");
		xdccFinderSearch = new HttpXdccSearch("xdccfinder.it");

	}

	@Test
	public final void testHttpSearchWithXdccFinder() {
		Set<XdccRequest> result = xdccFinderSearch.search(XdccQueryBuilder
				.create().params("mutant chronicles"));
		Assert.assertTrue(result.size() > 0);
		for (XdccRequest r : result) {
			System.out.println(r);
		}

	}

	@Test
	public final void testHttpSearch() {
		Set<XdccRequest> result = xdccItSearch.search(XdccQueryBuilder.create()
				.to("whfdsfsdf").params("mutant chronicles"));
		Assert.assertTrue(result.size() > 0);
		for (XdccRequest r : result) {
			System.out.println(r);
		}

	}

	@Test
	public final void testHttpSearchWithReplace() {
		XdccQuery query = XdccQueryBuilder.create().to("xdcc.it")
				.params("mutant chronicles");
		Map<QueryFilter, String> tmp1 = new HashMap<QueryFilter, String>();
		tmp1.put(QueryFilter.HOST, "irc.darksin.it");
		query.excludeFilter(tmp1);
		Map<QueryCondition, String> tmp2 = new HashMap<QueryCondition, String>();
		tmp2.put(QueryCondition.HOST, "irc.crocmax.net");
		query.replacefilter(tmp2);
		Set<XdccRequest> result = xdccItSearch.search(query);

		Assert.assertTrue(result.size() > 0);

		for (XdccRequest r : result) {
			Assert.assertTrue(r.getHost(), !r.getHost()
					.equals("irc.darksin.it"));
			System.out.println(r);
		}

	}
}
