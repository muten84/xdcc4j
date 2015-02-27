package it.luigibifulco.xdcc4j.search.engine;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.search.SearchEngineFactory;
import it.luigibifulco.xdcc4j.search.SearchModule;
import it.luigibifulco.xdcc4j.search.XdccSearch;
import it.luigibifulco.xdcc4j.search.XdccSearchEngine;
import it.luigibifulco.xdcc4j.search.XdccSearchFactory;
import it.luigibifulco.xdcc4j.search.parser.XdccItParser;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules(SearchModule.class)
public class SearchEngineFactoryTest {

	@Inject
	private XdccSearchFactory searchTypeFactory;

	@Inject
	private SearchEngineFactory factory;

	// private String domain;

	@Test
	public final void test() {

		XdccSearchEngine xdccit = factory.http(
				SearchEngineType.typeOf("xdcc_it"),
				Arrays.asList(new String[] { "q" }), "+", new XdccItParser());
		Assert.assertNotNull(xdccit);
		XdccSearch search = searchTypeFactory.create(xdccit);
		Assert.assertNotNull(search);
	}

}
