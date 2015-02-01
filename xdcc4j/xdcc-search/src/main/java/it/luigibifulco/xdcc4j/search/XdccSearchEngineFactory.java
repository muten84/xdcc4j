package it.luigibifulco.xdcc4j.search;

import java.util.Arrays;

import it.luigibifulco.xdcc4j.search.http.HttpXdccSearchEngine;
import it.luigibifulco.xdcc4j.search.parser.XdccFinderParser;
import it.luigibifulco.xdcc4j.search.parser.XdccItParser;

public class XdccSearchEngineFactory {

	public static XdccSearchEngine create(String type) {
		switch (type) {
		case "xdcc.it":
			return new HttpXdccSearchEngine(
					Arrays.asList(new String[] { "q" }), "+",
					new XdccItParser());
		case "xdccfinder.it":
			return new HttpXdccSearchEngine(
					Arrays.asList(new String[] { "search" }), " ",
					new XdccFinderParser());

		default:
			return null;
		}
	}
}
