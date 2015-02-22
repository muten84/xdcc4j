package it.luigibifulco.xdcc4j.ui.client.search.controller;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Window;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;
import it.luigibifulco.xdcc4j.ui.client.search.event.DownloadRequestHandler;

public class DownloadRequestController implements DownloadRequestHandler {

	@Override
	public void onDownloadRequest(DownloadBean download) {
		Window.alert("Starting download of: " + download);
		RequestBuilder connectedRequest = new RequestBuilder(
				RequestBuilder.GET, "services/downloader/isConnected");
		RequestBuilder startDownloadRequest = new RequestBuilder(
				RequestBuilder.GET, "services/downloader/startDownload?id="
						+ download.getId());
	}

}
