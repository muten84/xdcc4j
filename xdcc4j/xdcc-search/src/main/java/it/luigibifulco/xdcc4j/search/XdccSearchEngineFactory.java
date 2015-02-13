package it.luigibifulco.xdcc4j.search;

import it.luigibifulco.xdcc4j.search.engine.http.HttpXdccSearchEngine;
import it.luigibifulco.xdcc4j.search.engine.local.LocalSearch;
import it.luigibifulco.xdcc4j.search.parser.XdccFinderParser;
import it.luigibifulco.xdcc4j.search.parser.XdccItParser;

import java.util.Arrays;

//@Singleton
public class XdccSearchEngineFactory {

	public static XdccSearchEngine _create(String type) {
		switch (type) {
		case "xdcc.it":
			return new HttpXdccSearchEngine(null,
					Arrays.asList(new String[] { "q" }), "+",
					new XdccItParser());
		case "xdccfinder.it":
			return new HttpXdccSearchEngine(null,
					Arrays.asList(new String[] { "search" }), " ",
					new XdccFinderParser());
		case "local":
		case "localhost":
			return new LocalSearch();
		default:
			return null;
		}
	}
}
