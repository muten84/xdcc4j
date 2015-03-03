package it.luigibifulco.xdcc4j.search;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.query.XdccQuery;

import java.util.Set;

public interface XdccSearchEngine {

	public Set<XdccRequest> search(XdccQuery query) throws RuntimeException;

	public String getType();

}
