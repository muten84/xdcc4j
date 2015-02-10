package it.luigibifulco.xdcc4j.search.cache;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.scan.XdccScanner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XdccCache {

	private XdccScanner scanner;

	private Map<String, XdccRequest> cache;

	public XdccCache() {
		cache = new HashMap<String, XdccRequest>();
	}

	public boolean cacheFrom(String server, String channel) {
		List<XdccRequest> requests = scanner.scan(server, channel, false);
		for (XdccRequest xdccRequest : requests) {
			cache.put(xdccRequest.getId(), xdccRequest);
		}
		return true;
	}

	public boolean cacheFrom(String server, String channel, String user) {
		List<XdccRequest> requests = scanner.scan(server, channel, user);
		for (XdccRequest xdccRequest : requests) {
			cache.put(xdccRequest.getId(), xdccRequest);
		}
		return true;
	}

}
