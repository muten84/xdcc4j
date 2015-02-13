package it.luigibifulco.xdcc4j.search.http;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.SearchEngineFactory;
import it.luigibifulco.xdcc4j.search.SearchModule;
import it.luigibifulco.xdcc4j.search.XdccSearch;
import it.luigibifulco.xdcc4j.search.XdccSearchFactory;
import it.luigibifulco.xdcc4j.search.engine.SearchEngineType;
import it.luigibifulco.xdcc4j.search.parser.XdccItParser;
import it.luigibifulco.xdcc4j.search.query.XdccQuery;
import it.luigibifulco.xdcc4j.search.query.XdccQuery.QueryCondition;
import it.luigibifulco.xdcc4j.search.query.XdccQuery.QueryFilter;
import it.luigibifulco.xdcc4j.search.query.XdccQueryBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules(SearchModule.class)
public class HttpXdccSearchTest {

	@Inject
	private XdccSearchFactory factory;

	@Inject
	private SearchEngineFactory engineFacory;

	private SearchEngineType domain = SearchEngineType.xdcc_it;

	@Before
	public final void before() {

	}

	@Test
	public final void testHttpSearchWithXdccFinder() {
		XdccSearch xdccFinderSearch = factory.create(engineFacory.http(domain,
				Arrays.asList(new String[] { "q" }), "+", new XdccItParser()));
		Set<XdccRequest> result = xdccFinderSearch.search(XdccQueryBuilder
				.create().params("mutant chronicles"));
		Assert.assertTrue(result.size() > 0);
		for (XdccRequest r : result) {
			System.out.println(r);
		}

	}

	@Test
	public final void testHttpSearchWithXdccDotIt() {
		XdccSearch xdccItSearch = factory.create(engineFacory.http(domain,
				Arrays.asList(new String[] { "q" }), "+", new XdccItParser()));
		Set<XdccRequest> result = xdccItSearch.search(XdccQueryBuilder.create()
				.to("whfdsfsdf").params("mutant chronicles"));
		Assert.assertTrue(result.size() > 0);
		for (XdccRequest r : result) {
			System.out.println(r);
		}

	}

	@Test
	public final void testHttpSearchWithReplace() {
		XdccSearch xdccItSearch = factory.create(engineFacory.http(domain,
				Arrays.asList(new String[] { "q" }), "+", new XdccItParser()));
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
