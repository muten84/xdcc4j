package it.luigibifulco.xdcc4j.search;

import java.util.HashMap;
import java.util.Map;

public class XdccQueryBuilder {

	private static XdccQuery query;

	public static XdccQuery create() {

		query = new XdccQuery() {

			private String to;

			private String params;

			private String hostExclude;

			private String hostReplace;

			@Override
			public XdccQuery to(String to) {
				this.to = to;
				return this;
			}

			@Override
			public XdccQuery params(String params) {
				this.params = params;
				return this;
			}

			@Override
			public Map<String, String> getQueryAsMap() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("to", this.to);
				map.put("params", this.params);
				map.put(QueryFilter.HOST.toString(), this.hostExclude);
				map.put(QueryCondition.HOST.toString(), this.hostReplace);
				return map;
			}

			@Override
			public XdccQuery excludeFilter(Map<QueryFilter, String> excludeMap) {
				this.hostExclude = excludeMap.get(QueryFilter.HOST);
				return this;
			}

			@Override
			public XdccQuery replacefilter(
					Map<QueryCondition, String> replaceMap) {
				this.hostReplace = replaceMap.get(QueryCondition.HOST);
				return this;
			}

		};

		return query;
	}
}
