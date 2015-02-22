package it.luigibifulco.xdcc4j.ui.client.search;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;
import it.luigibifulco.xdcc4j.ui.client.Registry;
import it.luigibifulco.xdcc4j.ui.client.search.controller.DownloadRequestController;
import it.luigibifulco.xdcc4j.ui.client.search.controller.SearchController;
import it.luigibifulco.xdcc4j.ui.client.search.view.HeaderUi;
import it.luigibifulco.xdcc4j.ui.client.search.view.SearchUI;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class IrcMuleEntryPoint implements EntryPoint {
	// invoke http://127.0.0.1:8888/services/downloader/setServer?name=irc.ole
	public static interface DownloadBeanMapper extends
			ObjectMapper<DownloadBean> {
	}

	@Override
	public void onModuleLoad() {
		HeaderUi header = new HeaderUi();
		SearchUI searchui = new SearchUI();
		Registry.register("header", header);
		Registry.register("searchui", searchui);

		SearchController searchCtrl = new SearchController();
		header.addHandler(searchCtrl, HeaderUi.SEARCH);
		header.addHandler(searchCtrl, HeaderUi.CLEAR);
		searchui.addHandler(new DownloadRequestController(),
				SearchUI.DOWNLOAD_REQUEST_START);
		RootPanel.get("headerContainer").add(header);
		RootPanel.get("bodyContainer").add(searchui);
	}
}
