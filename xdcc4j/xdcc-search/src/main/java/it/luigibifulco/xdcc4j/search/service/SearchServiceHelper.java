package it.luigibifulco.xdcc4j.search.service;

import java.util.Arrays;

import it.luigibifulco.xdcc4j.search.SearchEngineFactory;
import it.luigibifulco.xdcc4j.search.XdccSearchEngine;
import it.luigibifulco.xdcc4j.search.engine.SearchEngineType;
import it.luigibifulco.xdcc4j.search.parser.CmPlusParser;
import it.luigibifulco.xdcc4j.search.parser.PuffolandiaParser;
import it.luigibifulco.xdcc4j.search.parser.XdccFinderParser;
import it.luigibifulco.xdcc4j.search.parser.XdccItParser;

public class SearchServiceHelper {

	public static XdccSearchEngine getEngineByType(SearchEngineType type,
			SearchEngineFactory engineFactory) {
		XdccSearchEngine engine = null;
		switch (type) {
		case xdcc_it:
			engine = engineFactory.http(type,
					Arrays.asList(new String[] { "q" }), "+",
					new XdccItParser());
			return engine;

		case xdccfinder:
			engine = engineFactory.http(type,
					Arrays.asList(new String[] { "search" }), " ",
					new XdccFinderParser());
			return engine;

		case cmplus_on_crocmax:
			engine = engineFactory.http(type,
					Arrays.asList(new String[] { "func", "q" }), "+",
					new CmPlusParser());
			return engine;
		case puffolandia:
			engine = engineFactory.http(type,
					Arrays.asList(new String[] { "q" }), "+",
					new PuffolandiaParser());
			return engine;
		default:
			break;

		}
		return null;
	}

}
