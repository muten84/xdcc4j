package it.luigibifulco.xdcc4j.search;

import it.luigibifulco.xdcc4j.search.http.HttpXdccSearch;
import it.luigibifulco.xdcc4j.search.parser.ParserModule;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

public class SearchModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ParserModule());
		MapBinder<String, XdccSearch> mapbinder = MapBinder.newMapBinder(
				binder(), String.class, XdccSearch.class);
		mapbinder.addBinding("xdccit")
				.toInstance(new HttpXdccSearch("xdcc.it"));
		mapbinder.addBinding("xdccfinder").toInstance(
				new HttpXdccSearch("xdccfinder.it"));

	}

}
