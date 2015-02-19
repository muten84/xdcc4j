package it.luigibifulco.xdcc4j.search.engine;

public enum SearchEngineType {

	xdcc_it("xdcc.it"), xdccfinder("xdccfinder.it"), cmplus_on_crocmax(
			"5.39.80.142/lista.php");
	/*cm-plus*/
	// http://pumpit.chickenkiller.com/
	// http://5.39.80.142/lista.php?func=1&q=
	/*sunshine*/
	//http://www.sunshinelist.eu/lista_aggiornata/SuNvcxv9u0342.php

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
		switch (type) {
		case "cmplus_on_crocmax":
			return SearchEngineType.cmplus_on_crocmax;

		default:
			break;
		}
		return null;
	}

}
