package it.luigibifulco.xdcc4j.search.http;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.XdccSearchEngine;
import it.luigibifulco.xdcc4j.search.parser.XdccHtmlParser;
import it.luigibifulco.xdcc4j.search.query.XdccQuery;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class HttpXdccSearchEngine implements XdccSearchEngine {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(HttpXdccSearchEngine.class);

	protected XdccHtmlParser parser;

	private final List<String> queryNameParameter;

	private String separator;

	public HttpXdccSearchEngine(List<String> queryNameParameter,
			String paramSeparator, XdccHtmlParser parser) {
		this.queryNameParameter = queryNameParameter;
		this.separator = paramSeparator;
		this.parser = parser;
	}

	@Override
	public Set<XdccRequest> search(XdccQuery query) throws RuntimeException {
		LOGGER.info("search: " + query.getQueryAsMap());
		try {
			Set<XdccRequest> result = parser
					.parseDocument(httpQuery(encodeQuery(query)));

			return result;
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
				p = p.replace(" ", separator);
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
		// url = URLEncoder.encode(url);
		LOGGER.info("httpQuery: " + url);
		Connection conn = Jsoup.connect(url);
		conn.timeout(30000);
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
