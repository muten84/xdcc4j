package it.luigibifulco.xdcc4j.search.http;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.XdccSearchEngineFactory;
import it.luigibifulco.xdcc4j.search.http.HttpXdccSearchEngine;
import it.luigibifulco.xdcc4j.search.parser.XdccHtmlParser;
import it.luigibifulco.xdcc4j.search.query.XdccQuery;
import it.luigibifulco.xdcc4j.search.query.XdccQueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpXdccSearchEngineTest {

	private HttpXdccSearchEngine xdccItSearchEngine;

	private HttpXdccSearchEngine xdccFinderSearchEngine;

	private XdccHtmlParser parser;

	@Before
	public void before() {
		if (xdccItSearchEngine == null) {
			xdccItSearchEngine = (HttpXdccSearchEngine) XdccSearchEngineFactory
					.create("xdcc.it");
		}
		if (xdccFinderSearchEngine == null) {
			xdccFinderSearchEngine = (HttpXdccSearchEngine) XdccSearchEngineFactory
					.create("xdccfinder.it");
		}
		if (parser == null) {
			parser = EasyMock.createNiceMock(XdccHtmlParser.class);
		}
		EasyMock.reset(parser);
		XdccRequest r = new XdccRequest();
		r.setChannel("");

		EasyMock.expect(
				parser.parseDocument(EasyMock.anyObject(Document.class)))
				.andReturn(
						new HashSet<XdccRequest>(Arrays
								.asList(new XdccRequest[] {})));
		EasyMock.replay(parser);
		xdccItSearchEngine.parser = parser;

	}

	@Test
	public final void testXdccFinderSearch() {
		XdccQuery query = XdccQueryBuilder.create().to("xdccfinder.it")
				.params("imitation");
		Set<XdccRequest> result = xdccFinderSearchEngine.search(query);
		Assert.assertTrue(result.size() > 0);
	}

	@Test
	public final void testXdccItSearch() {
		XdccQuery query = XdccQueryBuilder.create().to("xdcc.it")
				.params("The imitation Game");
		Map<String, String> map = query.getQueryAsMap();
		Assert.assertTrue(map.size() > 0);
		Set<XdccRequest> result = xdccItSearchEngine.search(query);
		Assert.assertTrue(result.size() == 0);
	}
}
