package it.luigibifulco.xdcc4j.search.service;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.SearchModule;
import it.luigibifulco.xdcc4j.search.engine.SearchEngineType;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
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
	public final void searchAll() {
		String what = "gotham 1x01";
		List<XdccRequest> request = service.searchAll(what);
		Assert.assertNotNull(request);
		Assert.assertTrue(request.size() > 0);
		for (XdccRequest xdccRequest : request) {
			// if (StringUtils.isEmpty(xdccRequest.getHost()))
			System.out.println(xdccRequest);
		}
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
				SearchEngineType.cmplus_on_crocmax.name(), "1", "2013");
		for (XdccRequest xdccRequest : request) {
			System.out.println(xdccRequest);
		}
	}
}
