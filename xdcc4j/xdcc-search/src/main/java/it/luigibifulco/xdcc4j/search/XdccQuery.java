package it.luigibifulco.xdcc4j.search;

import java.util.Map;

public interface XdccQuery {

	public XdccQuery to(String to);

	public XdccQuery params(String to);

	public Map<String, String> getQueryAsMap();

}
