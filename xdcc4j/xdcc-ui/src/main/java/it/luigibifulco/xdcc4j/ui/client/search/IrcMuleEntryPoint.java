package it.luigibifulco.xdcc4j.ui.client.search;

import it.luigibifulco.xdcc4j.ui.client.Registry;
import it.luigibifulco.xdcc4j.ui.client.search.controller.SearchController;
import it.luigibifulco.xdcc4j.ui.client.search.view.HeaderUi;
import it.luigibifulco.xdcc4j.ui.client.search.view.SearchUI;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class IrcMuleEntryPoint implements EntryPoint {
//invoke http://127.0.0.1:8888/services/downloader/setServer?name=irc.ole
	@Override
	public void onModuleLoad() {
		HeaderUi header = new HeaderUi();
		SearchUI searchui = new SearchUI();
		Registry.register("header", header);
		Registry.register("searchui", searchui);
		
		SearchController searchCtrl = new SearchController();
		header.addHandler(searchCtrl, HeaderUi.SEARCH);
		header.addHandler(searchCtrl, HeaderUi.CLEAR);
		RootPanel.get("headerContainer").add(header);
		RootPanel.get("bodyContainer").add(searchui);
	}
}
