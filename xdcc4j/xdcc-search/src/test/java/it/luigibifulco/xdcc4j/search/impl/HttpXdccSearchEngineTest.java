package it.luigibifulco.xdcc4j.search.impl;

import it.luigibifulco.xdcc4j.search.XdccHtmlParser;
import it.luigibifulco.xdcc4j.search.XdccQuery;
import it.luigibifulco.xdcc4j.search.XdccQueryBuilder;
import it.luigibifulco.xdcc4j.search.impl.HttpXdccSearchEngine;

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

	private HttpXdccSearchEngine searchEngine;

	private XdccHtmlParser parser;

	@Before
	public void before() {
		if (searchEngine == null) {
			searchEngine = new HttpXdccSearchEngine(new ArrayList<String>(
					Arrays.asList(new String[] { "q" })));
		}
		if (parser == null) {
			parser = EasyMock.createNiceMock(XdccHtmlParser.class);
		}
		EasyMock.reset(parser);
		EasyMock.expect(
				parser.parseDocument(EasyMock.anyObject(Document.class)))
				.andReturn(
						new HashSet<String>(Arrays
								.asList(new String[] { "Dummy Result" })));
		EasyMock.replay(parser);
		searchEngine.parser = parser;

	}

	@Test
	public final void testBaseSearch() {
		XdccQuery query = XdccQueryBuilder.create().to("xdcc.it")
				.params("The imitatation Game");
		Map<String, String> map = query.getQueryAsMap();
		Assert.assertTrue(map.size() > 0);
		Set<String> result = searchEngine.search(query);
		Assert.assertTrue(result.size() > 0);
	}
}
