package it.luigibifulco.xdcc4j.ui.client.search.controller;

import it.luigibifulco.xdcc4j.ui.client.Registry;
import it.luigibifulco.xdcc4j.ui.client.search.event.SearchHandler;
import it.luigibifulco.xdcc4j.ui.client.search.view.SearchUI;

import java.util.Arrays;

public class SearchController implements SearchHandler {

	private SearchUI view;

	public SearchController() {
		this.view = Registry.get("searchui");
	}

	@Override
	public void onSearch(String inputText) {
		view.setSearchResult(Arrays.asList(new String[] { "asdsd", "asdsdsas",
				"dsdsdddddddddddd" }));

	}

	@Override
	public void onClear() {
		view.clearResult();

	}
}
