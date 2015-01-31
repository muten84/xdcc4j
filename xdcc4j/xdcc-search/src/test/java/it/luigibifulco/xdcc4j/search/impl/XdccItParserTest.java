package it.luigibifulco.xdcc4j.search.impl;

import java.io.IOException;
import java.util.Set;

import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XdccItParserTest {

	private XdccItParser parser;

	@Before
	public final void before() {
		if (parser == null) {
			parser = new XdccItParser();
		}
	}

	@Test
	public final void testXdccITResult() throws IOException {

		Set<String> result = parser.parseDocument(Jsoup.connect(
				"http://xdcc.it/?q=The+imitation+game").get());
		Assert.assertTrue(result.size() > 0);
		for (String string : result) {
			System.out.println(string);
		}
	}
}
