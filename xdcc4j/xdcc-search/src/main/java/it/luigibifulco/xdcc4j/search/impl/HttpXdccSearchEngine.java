package it.luigibifulco.xdcc4j.search.impl;

import it.luigibifulco.xdcc4j.search.XdccHtmlParser;
import it.luigibifulco.xdcc4j.search.XdccQuery;
import it.luigibifulco.xdcc4j.search.XdccSearchEngine;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpXdccSearchEngine implements XdccSearchEngine {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(HttpXdccSearchEngine.class);

	protected XdccHtmlParser parser;

	private final List<String> queryNameParameter;

	public HttpXdccSearchEngine(List<String> queryNameParameter) {
		this.queryNameParameter = queryNameParameter;
	}

	@Override
	public Set<String> search(XdccQuery query) throws RuntimeException {
		LOGGER.info("search: " + query.getQueryAsMap());
		try {
			return parser.parseDocument(httpQuery(encodeQuery(query)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected String encodeQuery(XdccQuery query) {
		Map<String, String> map = query.getQueryAsMap();
		StringBuffer buff = new StringBuffer();
		String to = map.get("to");
		if (!to.contains("http")) {
			to = "http://" + to;
		}
		buff.append(to);
		String params = map.get("params");

		if (params != null && params.length() > 0) {
			buff.append("?");
			String[] _params = params.split(",");
			int cnt = 0;
			for (String p : _params) {
				p = p.replace(" ", "+");
				if (cnt == 0) {
					buff.append(queryNameParameter.get(cnt) + "=" + p);
				} else {
					buff.append("&");
					buff.append(queryNameParameter.get(cnt) + "=" + p);
				}
				cnt++;
			}

		}
		return buff.toString();
	}

	protected Document httpQuery(String url) throws IOException {
		LOGGER.info("httpQuery: " + url);
		Connection conn = Jsoup.connect(url);
		Document doc = conn.get();
		return doc;
	}

	@Override
	public String getType() {
		return this.getClass().toString();
	}

	public XdccHtmlParser getParser() {
		return parser;
	}

	public void setParser(XdccHtmlParser parser) {
		this.parser = parser;
	}

}
