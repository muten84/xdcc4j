package it.luigibifulco.xdcc4j.ui.client.search.event;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;

import com.google.gwt.event.shared.EventHandler;

public interface DownloadRequestHandler extends EventHandler {

	public void onDownloadRequest(DownloadBean download);

	public void onDownloadCancel(DownloadBean download);

	public void onDownloadRemove(DownloadBean download);

	public void onDownloadStatusUpdate(DownloadBean download);

}
