package it.luigibifulco.xdcc4j.search;

import java.util.Arrays;

import it.luigibifulco.xdcc4j.search.impl.HttpXdccSearchEngine;
import it.luigibifulco.xdcc4j.search.impl.XdccItParser;

public class XdccSearchEngineFactory {

	public static XdccSearchEngine create(String type) {
		switch (type) {
		case "xdcc.it":
			return new HttpXdccSearchEngine(
					Arrays.asList(new String[] { "q" }), new XdccItParser());

		default:
			return null;
		}
	}
}
