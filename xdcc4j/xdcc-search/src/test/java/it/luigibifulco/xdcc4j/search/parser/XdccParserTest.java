package it.luigibifulco.xdcc4j.search.parser;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.parser.annotations.XdccFinder;
import it.luigibifulco.xdcc4j.search.parser.annotations.XdccIt;

import java.io.IOException;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules(XdccParserTestModule.class)
public class XdccParserTest {

	@Inject
	@XdccFinder
	private XdccHtmlParser parser1;

	@Inject
	@XdccIt
	private XdccHtmlParser parser2;

	@Before
	public final void init() {

	}

	@Test
	public final void testPareOnXdccFinder() throws IOException {
		Connection conn = Jsoup
				.connect("http://xdccfinder.it/?search=the%20imitation%20game");
		conn.timeout(10000);
		Set<XdccRequest> result = parser1.parseDocument(conn.get());
		Assert.assertTrue(result.size() > 0);
		for (XdccRequest string : result) {
			System.out.println(string);
		}

	}

	@Test
	public final void testXdccITResult() throws IOException {
		System.out.println("parser instance: " + this.parser2.getClass());
		Set<XdccRequest> result = parser2.parseDocument(Jsoup
				.connect("http://xdcc.it/?q=The+imitation+game").timeout(10000)
				.get());
		Assert.assertTrue(result.size() > 0);
		for (XdccRequest string : result) {
			System.out.println(string);
		}
	}
}