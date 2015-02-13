package it.luigibifulco.xdcc4j.search.engine;

public enum SearchEngineType {

	xdcc_it("xdcc.it"), xdccfinder("xdccfinder.it");

	private String domain;

	private SearchEngineType(String domain) {
		this.domain = domain;
	}

	@Override
	public String toString() {
		return domain;
	}

	public static SearchEngineType typeOf(String type) {
		for (SearchEngineType t : SearchEngineType.values()) {
			if (t.toString().equals(type)) {
				return t;
			}
		}
		return null;
	}

}
