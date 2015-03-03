package it.luigibifulco.xdcc4j.search.parser;

import com.google.inject.AbstractModule;

public class XdccParserTestModule extends AbstractModule {

	@Override
	protected void configure() {
		// bind(XdccHtmlParser.class).to
		install(new ParserModule());
	}

	// @Provides
	// XdccHtmlParser get(String type) {
	// switch (type) {
	// case "xdcc.it":
	// return new XdccItParser();
	// case "xdccfinder.it":
	// return new XdccFinderParser();
	// default:
	// return null;
	// }
	// }
}
