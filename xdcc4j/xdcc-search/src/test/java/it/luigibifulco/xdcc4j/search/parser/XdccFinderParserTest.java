package it.luigibifulco.xdcc4j.search.parser;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;

import java.io.IOException;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XdccFinderParserTest {

	private XdccFinderParser parser;

	@Before
	public final void init() {
		parser = new XdccFinderParser();
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
