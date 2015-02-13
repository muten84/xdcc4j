package it.luigibifulco.xdcc4j.search;

import it.luigibifulco.xdcc4j.search.engine.SearchEngineType;
import it.luigibifulco.xdcc4j.search.parser.XdccHtmlParser;

import java.util.List;

import com.google.inject.name.Named;

public interface SearchEngineFactory {

	public @Named("http") XdccSearchEngine http(SearchEngineType searchDomain,
			List<String> queryNameParameter, String paramSeparator,
			XdccHtmlParser parser);

	public @Named("local") XdccSearchEngine local();

}
