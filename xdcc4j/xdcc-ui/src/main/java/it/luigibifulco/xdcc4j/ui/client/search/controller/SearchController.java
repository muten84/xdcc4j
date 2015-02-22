package it.luigibifulco.xdcc4j.ui.client.search.controller;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;
import it.luigibifulco.xdcc4j.ui.client.Registry;
import it.luigibifulco.xdcc4j.ui.client.search.IrcMuleEntryPoint.DownloadBeanMapper;
import it.luigibifulco.xdcc4j.ui.client.search.event.SearchHandler;
import it.luigibifulco.xdcc4j.ui.client.search.view.SearchUI;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class SearchController implements SearchHandler {
	Logger logger = Logger.getLogger("SearchController");
	private SearchUI view;

	private DownloadBeanMapper mapper;

	public SearchController() {
		this.view = Registry.get("searchui");
		mapper = GWT.create(DownloadBeanMapper.class);
	}

	@Override
	public void onSearch(String inputText) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				"services/downloader/search?where=cmplus_on_crocmax&what=1,"
						+ inputText);
		try {
			builder.setCallback(new RequestCallback() {

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					logger.info("" + response.getStatusCode());
					String jsonString = response.getText();
					Map<String, DownloadBean> searchResult = getData(jsonString);
					Registry.register("searchresult", searchResult);
					view.clearResult();
					view.setSearchResult();
				}

				@Override
				public void onError(Request request, Throwable exception) {
					logger.info("error " + exception.getCause());
					// GWT.log(response.getText());

				}
			});
			builder.send();

		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// view.setSearchResult(Arrays.asList(new String[] { "asdsd",
		// "asdsdsas",
		// "dsdsdddddddddddd" }));

	}

	@Override
	public void onClear() {
		view.clearResult();

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
