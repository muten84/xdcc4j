package it.luigibifulco.xdcc4j.search.local;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.SearchEngineFactory;
import it.luigibifulco.xdcc4j.search.SearchModule;
import it.luigibifulco.xdcc4j.search.XdccSearch;
import it.luigibifulco.xdcc4j.search.XdccSearchFactory;
import it.luigibifulco.xdcc4j.search.query.XdccQueryBuilder;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules(SearchModule.class)
public class SearchLocalTest {

	@Inject
	private XdccSearchFactory searchTypeFactory;

	@Inject
	private SearchEngineFactory factory;

	@Test
	public final void testSearchLocally() {
		XdccSearch search = searchTypeFactory.create(factory.local());
		Set<XdccRequest> result = search.search(XdccQueryBuilder.create()
				.params("divx"));
		for (XdccRequest xdccRequest : result) {
			System.out.println(xdccRequest);
		}
	}
}
