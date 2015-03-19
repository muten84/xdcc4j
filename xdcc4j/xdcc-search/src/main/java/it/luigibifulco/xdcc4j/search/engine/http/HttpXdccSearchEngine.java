package it.luigibifulco.xdcc4j.search.engine.http;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.SearchUtil;
import it.luigibifulco.xdcc4j.search.XdccSearchEngine;
import it.luigibifulco.xdcc4j.search.engine.SearchEngineType;
import it.luigibifulco.xdcc4j.search.parser.XdccHtmlParser;
import it.luigibifulco.xdcc4j.search.query.XdccQuery;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class HttpXdccSearchEngine implements XdccSearchEngine {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(HttpXdccSearchEngine.class);

	protected XdccHtmlParser parser;

	private final List<String> queryNameParameter;

	private String separator;

	private String searchDomain;

	@Inject
	@AssistedInject
	public HttpXdccSearchEngine(@Assisted SearchEngineType searchDomain,
			@Assisted List<String> queryNameParameter,
			@Assisted String paramSeparator, @Assisted XdccHtmlParser parser) {
		this.queryNameParameter = queryNameParameter;
		this.separator = paramSeparator;
		this.parser = parser;
		this.searchDomain = searchDomain.toString();
	}

	@Override
	public Set<XdccRequest> search(XdccQuery query) throws RuntimeException {
		LOGGER.info("search: " + query.getQueryAsMap());
		Set<XdccRequest> toReturn = new HashSet<XdccRequest>();
		try {
			Set<XdccRequest> result = parser
					.parseDocument(httpQuery(encodeQuery(query)));
			for (XdccRequest xdccRequest : result) {
				if (xdccRequest == null) {
					continue;
				}

				if (StringUtils.isEmpty(xdccRequest.getHost())) {
					xdccRequest.setHost(SearchUtil.getInstance()
							.getHostFromChannel(xdccRequest.getChannel()));
				}
				if (!StringUtils.isEmpty(xdccRequest.getHost())) {
					toReturn.add(xdccRequest);
				}
			}
			return toReturn;
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
		return searchDomain;
	}

	public XdccHtmlParser getParser() {
		return parser;
	}

	public void setParser(XdccHtmlParser parser) {
		this.parser = parser;
	}

}
