package it.luigibifulco.xdcc4j.search.cache;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.db.XdccRequestStore;
import it.luigibifulco.xdcc4j.search.scan.XdccScanner;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class XdccCache {

	private String cacheDir;

	@Inject
	private XdccScanner scanner;

	private XdccRequestStore store;

	private Map<String, XdccRequest> cache;

	public XdccCache(@Named("cache.dir") String cacheDir) {
		this.cacheDir = cacheDir;

		if (StringUtils.isEmpty(cacheDir)) {
			throw new RuntimeException("cache dir can not be empty");
		}

		File f = new File(cacheDir);
		if (!f.exists()) {
			f.mkdirs();
		}
		cache = new ConcurrentHashMap<String, XdccRequest>();
		store = new XdccRequestStore(cacheDir + "/cache.db");
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

	public boolean persistCache() {
		Set<String> keys = cache.keySet();
		for (String s : keys) {
			store.insert(cache.get(s));
		}
		return true;
	}

	public boolean clearCache() {
		cache.clear();
		return true;
	}

	public boolean cacheFromLocal() {
		int currentSize = cache.size();
		Collection<XdccRequest> reqs = store.getAll();
		for (XdccRequest xdccRequest : reqs) {
			cache.put(xdccRequest.getId(), xdccRequest);
		}
		return cache.size() > currentSize;
	}

	public Map<String, XdccRequest> cache() {
		return new HashMap<String, XdccRequest>(cache);
	}

	public List<XdccRequest> search(XdccRequest like) {
		return store.searchByExample(like);
	}

	public String getCacheDir() {
		return cacheDir;
	}

	public void setCacheDir(String cacheDir) {
		this.cacheDir = cacheDir;
	}

}
