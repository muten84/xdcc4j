package it.luigibifulco.xdcc4j.search.engine;

public enum SearchEngineType {

	xdcc_it("xdcc.it"), //
	xdccfinder("xdccfinder.it"), //
	cmplus_on_crocmax("5.39.80.142/lista.php"), //
	puffolandia("puffolandia.oltreirc.net");

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
			if (t.name().trim().equals(type.trim())) {
				return t;
			}
		}
		switch (type) {
		case "cmplus_on_crocmax":
			return SearchEngineType.cmplus_on_crocmax;
		case "xdccfinder":
			return SearchEngineType.xdccfinder;
		case "xdccit":
			return SearchEngineType.xdcc_it;
		default:
			break;
		}
		return null;
	}

}
