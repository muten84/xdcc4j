package it.luigibifulco.xdcc4j.ui.client.search.event;

import com.google.gwt.event.shared.EventHandler;

public interface SearchHandler extends EventHandler {
	public void onSearch(String inputText);

	public void onClear();

	public void onViewDownloads();
}
