package it.luigibifulco.xdcc4j.search;

import java.util.HashMap;
import java.util.Map;

public class XdccQueryBuilder {

	private static XdccQuery query;

	public static XdccQuery create() {

		query = new XdccQuery() {

			private String to;

			private String params;

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
				return map;
			}
		};

		return query;
	}

}
