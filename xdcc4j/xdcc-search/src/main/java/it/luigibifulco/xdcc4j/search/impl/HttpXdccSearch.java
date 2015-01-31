package it.luigibifulco.xdcc4j.search.impl;

import it.luigibifulco.xdcc4j.search.XdccQuery;
import it.luigibifulco.xdcc4j.search.XdccSearch;

import java.util.Set;

public class HttpXdccSearch implements XdccSearch {

	protected HttpXdccSearchEngine engine;

	@Override
	public Set<String> search(XdccQuery query) throws RuntimeException {
		return engine.search(query);
	}

	public HttpXdccSearchEngine getEngine() {
		return engine;
	}

	public void setEngine(HttpXdccSearchEngine engine) {
		this.engine = engine;
	}

}
