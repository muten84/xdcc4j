package it.luigibifulco.xdcc4j.search;

import com.google.inject.name.Named;

public interface XdccSearchFactory {

	public @Named("searchengine") XdccSearch create(XdccSearchEngine httpSearchEngine);

	// public @Named("local") XdccSearch local(String engineType);

}
