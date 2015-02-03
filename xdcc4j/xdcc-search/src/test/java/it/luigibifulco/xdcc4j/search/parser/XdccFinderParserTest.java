package it.luigibifulco.xdcc4j.search.parser;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;

import java.io.IOException;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.google.inject.name.Named;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules(XdccParserTestModule.class)
public class XdccFinderParserTest {

	@Inject
	@Named("xdccfinder")
	private XdccHtmlParser parser;

	@Before
	public final void init() {

	}

	@Test
	public final void testPareOnXdccFinder() throws IOException {
		Connection conn = Jsoup
				.connect("http://xdccfinder.it/?search=the%20imitation%20game");
		Set<XdccRequest> result = parser.parseDocument(conn.get());
		Assert.assertTrue(result.size() > 0);
		for (XdccRequest string : result) {
			System.out.println(string);
		}

	}
}
