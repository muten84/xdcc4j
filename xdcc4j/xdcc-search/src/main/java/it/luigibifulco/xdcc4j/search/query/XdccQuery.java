package it.luigibifulco.xdcc4j.search.query;

import java.util.Map;

public interface XdccQuery {

	public XdccQuery to(String to);

	public XdccQuery excludeFilter(Map<QueryFilter, String> excludeMap);

	public XdccQuery replacefilter(Map<QueryCondition, String> replaceMap);

	public XdccQuery params(String to);

	public Map<String, String> getQueryAsMap();

	public static enum QueryFilter {
		HOST("hostFilter"), CHANNEL("channelFilter");

		private final String id;

		private QueryFilter(String id) {
			this.id = id;
		}

		@Override
		public String toString() {

			return this.id;
		}
	}

	public static enum QueryCondition {
		HOST("hostCondition");
		private final String id;

		private QueryCondition(String id) {
			this.id = id;
		}

		@Override
		public String toString() {

			return this.id;
		}
	}

}
