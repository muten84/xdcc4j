package it.luigibifulco.xdcc4j.search.service;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.SearchModule;
import it.luigibifulco.xdcc4j.search.engine.SearchEngineType;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules(SearchModule.class)
public class SearchServiceTest {

	@Inject
	private SearchService service;

	@Test
	public final void testReindex() {
		service.reindex("irc.uragano.org", "#sunshine", "SUN|DVDRIP|23", true);
	}

	@Test
	public final void testSearchServiceLocally() {
		List<XdccRequest> request = service.search("local", "");
		for (XdccRequest xdccRequest : request) {
			System.out.println(xdccRequest.getDescription() + " - "
					+ xdccRequest.getPeer());
		}
	}

	@Test
	public final void testSearchServiceRemote() {
		List<XdccRequest> request = service.search(
				SearchEngineType.xdccfinder.name(), "amore");
		for (XdccRequest xdccRequest : request) {
			System.out.println(xdccRequest.getDescription());
		}
	}

	@Test
	public final void testSearchServiceCmPlus() {
		List<XdccRequest> request = service.search(
				SearchEngineType.cmplus_on_crocmax.toString(), "1", "amore");
		for (XdccRequest xdccRequest : request) {
			System.out.println(xdccRequest);
		}
	}
}
