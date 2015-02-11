package it.luigibifulco.xdcc4j.search.local;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.db.XdccRequestStore;
import it.luigibifulco.xdcc4j.search.XdccSearchEngine;
import it.luigibifulco.xdcc4j.search.query.XdccQuery;

import java.util.Set;

import com.google.inject.Inject;

public class LocalSearch implements XdccSearchEngine {

	@Inject
	private XdccRequestStore store;

	@Override
	public Set<XdccRequest> search(XdccQuery query) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		return "local";
	}

}
