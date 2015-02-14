package it.luigibifulco.xdcc4j.downloader.service.delegate.rest;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.downloader.core.XdccDownloader;
import it.luigibifulco.xdcc4j.downloader.core.model.Download;
import it.luigibifulco.xdcc4j.downloader.service.XdccDownloaderService;
import it.luigibifulco.xdcc4j.downloader.service.model.DownloadBean;
import it.luigibifulco.xdcc4j.downloader.util.ConvertUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.sun.jersey.spi.resource.Singleton;

@Path("/downloader")
@Singleton
public class DownloaderDelegate implements XdccDownloaderService {

	@Inject
	private XdccDownloader service;

	Logger logger = LoggerFactory.getLogger(DownloaderDelegate.class);

	@Override
	@GET
	@Path("reindex")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean reindex(@QueryParam("channel") String channel,
			@QueryParam("user") String user) {
		logger.info("reindex: " + user + "@" + channel);
		return service.reindex("#" + channel, user);
	}

	@Override
	@GET
	@Path("setServer")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean setServer(@QueryParam("server") String server) {
		return service.setServer(server);
	}

	@Override
	@GET
	@Path("getDownload")
	@Produces(MediaType.APPLICATION_JSON)
	public DownloadBean getDownload(@QueryParam("id") String id) {
		Download d = service.getDownload(id);
		return new DownloadBean(d.getId(), d.getDescription());
	}

	@Override
	public int cleanSearch() {
		return service.cleanSearch();
	}

	@Override
	@GET
	@Path("search")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, DownloadBean> search(@QueryParam("where") String where,
			@QueryParam("what") String what) {
		Map<String, XdccRequest> map = service.search(where, what);
		Map<String, DownloadBean> result = new HashMap<String, DownloadBean>();
		Set<String> keys = map.keySet();
		for (String s : keys) {
			XdccRequest req = map.get(s);
			result.put(s, ConvertUtil.convertFromXdccRequest(req));
		}
		return result;
	}

	@Override
	@GET
	@Path("startDownload")
	@Produces(MediaType.APPLICATION_JSON)
	public String startDownload(@QueryParam("id") String id) {
		return service.startDownload(id);
	}

	@Override
	@GET
	@Path("startAnyAvailableFromList")
	@Produces(MediaType.TEXT_PLAIN)
	public String startAnyAvailableFromList() {
		return service.startAnyAvailableFromList();
	}

	@Override
	@GET
	@Path("cancelDownload")
	@Produces(MediaType.TEXT_PLAIN)
	public String cancelDownload(String id) {
		return service.cancelDownload(id);
	}

	@Override
	@GET
	@Path("cancelAll")
	@Produces(MediaType.TEXT_PLAIN)
	public List<String> cancelAll() {
		return service.cancelAll();
	}

	@Override
	@GET
	@Path("cancelAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, DownloadBean> cache() {
		Map<String, XdccRequest> map = service.cache();
		Map<String, DownloadBean> result = new HashMap<String, DownloadBean>();
		Set<String> keys = map.keySet();
		for (String s : keys) {
			XdccRequest req = map.get(s);
			result.put(s, ConvertUtil.convertFromXdccRequest(req));
		}
		return result;
	}

	@Override
	@GET
	@Path("getAllDownloads")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<DownloadBean> getAllDownloads() {
		Collection<Download> list = service.getAllDownloads();
		Collection<DownloadBean> dbs = new ArrayList<DownloadBean>();
		for (Download d : list) {
			dbs.add(ConvertUtil.convertFromXdccRequest(d));
		}
		return dbs;
	}

}
