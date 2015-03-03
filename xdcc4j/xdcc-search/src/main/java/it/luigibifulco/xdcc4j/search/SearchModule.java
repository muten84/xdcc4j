package it.luigibifulco.xdcc4j.search;

import it.luigibifulco.xdcc4j.search.cache.XdccCache;
import it.luigibifulco.xdcc4j.search.engine.http.HttpXdccSearchEngine;
import it.luigibifulco.xdcc4j.search.engine.local.LocalSearch;
import it.luigibifulco.xdcc4j.search.impl.XdccSearchImpl;
import it.luigibifulco.xdcc4j.search.parser.ParserModule;
import it.luigibifulco.xdcc4j.search.parser.XdccFinderParser;
import it.luigibifulco.xdcc4j.search.parser.XdccItParser;
import it.luigibifulco.xdcc4j.search.scan.XdccScanner;
import it.luigibifulco.xdcc4j.search.service.SearchService;
import it.luigibifulco.xdcc4j.search.service.SearchServiceImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;

public class SearchModule extends AbstractModule {

	public SearchModule() {

	}

	@Override
	protected void configure() {
		install(new ParserModule());
		Map<String, String> props = new HashMap<String, String>();
		// "/Users/Default/AppData/Local/Temp"
		props.put("cache.dir", "/Users/" + System.getProperty("user.name")
				+ "/.xdcc-cache");

		// search engines types
		// MapBinder<String, XdccSearchEngine> searchEngineBinder = MapBinder
		// .newMapBinder(binder(), String.class, XdccSearchEngine.class);
		// searchEngineBinder.addBinding("xdcc.it").toInstance(
		// new HttpXdccSearchEngine(Arrays.asList(new String[] { "q" }),
		// "+", new XdccItParser()));
		// searchEngineBinder.addBinding("xdccfinder.it").toInstance(
		// new HttpXdccSearchEngine(Arrays
		// .asList(new String[] { "search" }), " ",
		// new XdccFinderParser()));
		// searchEngineBinder.addBinding("localhost").to(LocalSearch.class);

		// xdccsearch engine factory
		install(new FactoryModuleBuilder()//
				.implement(XdccSearchEngine.class, Names.named("http"),
						HttpXdccSearchEngine.class)//
				.implement(XdccSearchEngine.class, Names.named("local"),
						LocalSearch.class)//
				.build(SearchEngineFactory.class));

		// xdccsearchfactory
		install(new FactoryModuleBuilder()//
				.implement(XdccSearch.class, Names.named("searchengine"),
						XdccSearchImpl.class)//
				.build(XdccSearchFactory.class));

		// the cache
		Names.bindProperties(binder(), props);
		bind(XdccScanner.class).in(Singleton.class);
		bind(SearchService.class).to(SearchServiceImpl.class).in(
				Singleton.class);
		try {
			bind(XdccCache.class).toConstructor(
					XdccCache.class.getConstructor(String.class));
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* search impl types */
		// MapBinder<String, XdccSearch> mapbinder = MapBinder.newMapBinder(
		// binder(), String.class, XdccSearch.class);
		// mapbinder.addBinding("xdccit")
		// .toInstance(new XdccSearchImpl("xdcc.it"));
		// mapbinder.addBinding("xdccfinder").toInstance(
		// new XdccSearchImpl("xdccfinder.it"));
		// mapbinder.addBinding("localhost").toInstance(
		// new XdccSearchImpl("localhost"));

	}
}
