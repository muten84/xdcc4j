package it.luigibifulco.xdcc4j.search.query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class XdccQueryBuilder {

	private static XdccQuery query;

	public static XdccQuery create() {

		query = new XdccQuery() {

			private String to;

			private String[] params;

			private String hostExclude;

			private String hostReplace;

			private String descriptionInclude;

			@Override
			public XdccQuery to(String to) {
				this.to = to;
				return this;
			}

			@Override
			public XdccQuery params(String... params) {
				this.params = params;
				return this;
			}

			@Override
			public Map<String, String> getQueryAsMap() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("to", this.to);
				String params = Arrays.toString(this.params);
				params = params.replace("[", "");
				params = params.replace("]", "");
				map.put("params", params);
				map.put(QueryFilter.HOST.toString(), this.hostExclude);
				map.put(QueryCondition.HOST.toString(), this.hostReplace);
				map.put(QueryFilter.DESCRIPTION.toString(),
						this.descriptionInclude);
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

			@Override
			public XdccQuery includeFilter(Map<QueryFilter, String> includeMap) {
				this.descriptionInclude = includeMap
						.get(QueryFilter.DESCRIPTION);
				return this;
			}

		};

		return query;
	}
}
