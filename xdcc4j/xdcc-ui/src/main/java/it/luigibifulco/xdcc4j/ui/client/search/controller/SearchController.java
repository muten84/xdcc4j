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
				view.clearResult();
				view.showDownloads();

			}
		});
		try {
			builder.send();
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		websocket.start();
	}

	public void onDowloadUpdate(DownloadBean d) {

	}

	@Override
	public void onSearch(String inputText) {
		String searchType = Registry.get("searchType");
		if (searchType == null || searchType.isEmpty()) {
			header.alert("Seleziona un motore di ricerca");
			return;
		}
		searchType = searchType.trim();
		String where = "";
		if (searchType.equals("cm-plus")) {
			where = "cmplus_on_crocmax";
		} else if (searchType.equals("xdcc.it")) {
			where = "xdcc_it";
		} else if (searchType.equals("xdccfinder")) {
			where = "xdccfinder";
		}
		// header.alert("Cerco su: " + where + "--->" + searchType);
		String url = "services/downloader/search?where=" + where + "&";
		url += "what=";
		if (where.equals("cmplus_on_crocmax")) {
			url += "1,";
		}
		url += inputText;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.setCallback(new RequestCallback() {

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					view.clearResult();
					logger.info("" + response.getStatusCode());
					String jsonString = response.getText();
					Map<String, DownloadBean> searchResult = getData(jsonString);
					Registry.register("searchresult", searchResult);

					view.setSearchResult();
				}

				@Override
				public void onError(Request request, Throwable exception) {
					logger.info("error " + exception.getCause());
					view.clearResult();
				}
			});
			builder.send();

		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		websocket.stop();
		// view.setSearchResult(Arrays.asList(new String[] { "asdsd",
		// "asdsdsas",
		// "dsdsdddddddddddd" }));

	}

	@Override
	public void onClear() {
		view.clearResult();
		
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
