package it.luigibifulco.xdcc4j.ui.client.search.controller;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;
import it.luigibifulco.xdcc4j.ui.client.Registry;
import it.luigibifulco.xdcc4j.ui.client.search.IrcMuleEntryPoint.DownloadBeanMapper;
import it.luigibifulco.xdcc4j.ui.client.search.event.SearchHandler;
import it.luigibifulco.xdcc4j.ui.client.search.event.XdccWebSocket;
import it.luigibifulco.xdcc4j.ui.client.search.view.HeaderUi;
import it.luigibifulco.xdcc4j.ui.client.search.view.SearchUI;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class SearchController implements SearchHandler {
	Logger logger = Logger.getLogger("SearchController");
	private SearchUI view;

	private HeaderUi header;

	private DownloadBeanMapper mapper;

	private XdccWebSocket websocket;

	private HandlerManager ebus;

	public SearchController() {
		this.view = Registry.get("searchui");
		this.header = Registry.get("header");
		mapper = GWT.create(DownloadBeanMapper.class);
		ebus = new HandlerManager(this);
		websocket = new XdccWebSocket(this.ebus);
		Registry.register("websocket", websocket);
		ebus.addHandler(SearchUI.DOWNLOAD_REQUEST_START,
				Registry.<DownloadRequestController> get("downloadsController"));
	}

	@Override
	public void onViewDownloads() {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				"services/downloader/getAllDownloads");
		builder.setCallback(new RequestCallback() {
			@Override
			public void onError(Request request, Throwable exception) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseReceived(Request request, Response response) {
				String jsonString = response.getText();
				Map<String, DownloadBean> downloads = getDownloads(jsonString);
				Registry.register("downloads", downloads);
				//view.clearSearchResult();
				view.showDownloads();

			}
		});
		try {
			builder.send();
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			websocket.start();
		} catch (Exception e) {

		}
	}

	public void onDowloadUpdate(DownloadBean d) {

	}

	@Override
	public void onSearch(String inputText) {
		header.wait("Ricerca in corso...");
		String url = "services/downloader/search?what=";

		url += inputText;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.setCallback(new RequestCallback() {

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					Registry.register("searchresult", new HashMap<String, DownloadBean>());
					view.clearSearchResult();
					header.closeWait();
					logger.info("" + response.getStatusCode());
					String jsonString = response.getText();
					Map<String, DownloadBean> searchResult = getData(jsonString);
					Registry.register("searchresult", searchResult);

					view.showSearchResult();
				}

				@Override
				public void onError(Request request, Throwable exception) {
					logger.info("error " + exception.getCause());
					view.clearSearchResult();
				}
			});
			builder.send();

		} catch (RequestException e) {
			header.closeWait();
			e.printStackTrace();
		}
		// header.closeAlert();
		// websocket.stop();
		// view.setSearchResult(Arrays.asList(new String[] { "asdsd",
		// "asdsdsas",
		// "dsdsdddddddddddd" }));

	}

	@Override
	public void onClear() {
		view.clearSearchResult();

	}

	private Map<String, DownloadBean> getDownloads(String json) {
		Map<String, DownloadBean> data = new HashMap<String, DownloadBean>();
		JSONValue value = JSONParser.parseLenient(json);
		JSONArray array = value.isArray();
		int cnt = array.size();
		for (int i = 0; i < cnt; i++) {
			JSONValue cv = array.get(i);
			String dJson = cv.toString();
			DownloadBean bean = mapper.read(dJson);
			data.put(bean.getId(), bean);
		}
		return data;
	}

	private Map<String, DownloadBean> getData(String json) {
		Map<String, DownloadBean> data = new HashMap<String, DownloadBean>();
		JSONValue value = JSONParser.parseLenient(json);
		JSONObject obj = value.isObject();
		Set<String> keys = obj.keySet();

		for (String key : keys) {
			String dJson = obj.get(key).toString();
			DownloadBean bean = mapper.read(dJson);
			data.put(key, bean);
		}
		return data;
	}

}
