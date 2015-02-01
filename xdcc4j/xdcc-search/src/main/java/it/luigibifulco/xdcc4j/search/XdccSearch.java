package it.luigibifulco.xdcc4j.search;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;

import java.util.Set;

public interface XdccSearch {

	public Set<XdccRequest> search(XdccQuery query) throws RuntimeException;

}
