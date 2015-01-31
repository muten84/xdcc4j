package it.luigibifulco.xdcc4j.search.impl;

import it.luigibifulco.xdcc4j.search.XdccQueryBuilder;

import java.util.Arrays;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpXdccSearchTest {

	private HttpXdccSearch xdccSearch;

	@Before
	public final void before() {
		xdccSearch = new HttpXdccSearch();
		xdccSearch.engine = new HttpXdccSearchEngine(
				Arrays.asList(new String[] { "q" }));
		xdccSearch.engine.parser = new XdccItParser();
	}

	@Test
	public final void testHttSearch() {
		Set<String> result = xdccSearch.search(XdccQueryBuilder.create()
				.to("xdcc.it").params("mutant chronicles"));
		Assert.assertTrue(result.size() > 0);
		for (String string : result) {
			System.out.println(string);
		}

	}
}
