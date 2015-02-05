package it.luigibifulco.xdcc4j.downloader.delegate.rest;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.downloader.XdccDownloader;
import it.luigibifulco.xdcc4j.downloader.model.Download;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.sun.jersey.spi.resource.Singleton;

@Path("/downloader")
@Singleton
public class DownloaderDelegate implements XdccDownloader {

	@Inject
	private XdccDownloader service;

	@Override
	public boolean setServer(String server) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Download getDownload(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int cleanSearch() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@GET
	@Path("search")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, XdccRequest> search(@QueryParam("where") String where,
			@QueryParam("what") String what) {
		return service.search(where, what);
	}

	@Override
	public String startDownload(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String startAnyAvailableFromList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cancelDownload(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> cancelAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, XdccRequest> cache() {
		// TODO Auto-generated method stub
		return null;
	}

}
