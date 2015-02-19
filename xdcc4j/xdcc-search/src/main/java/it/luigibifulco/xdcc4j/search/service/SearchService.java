package it.luigibifulco.xdcc4j.search.service;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;

import java.util.List;

public interface SearchService {

	public List<XdccRequest> search(String where, String... what);

	public boolean reindex(String server, String channel, String user,
			boolean synch);

}
