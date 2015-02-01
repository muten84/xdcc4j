package it.luigibifulco.xdcc4j.search.parser;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.parser.XdccItParser;

import java.io.IOException;
import java.util.Set;

import org.jsoup.Connection;
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

		Set<XdccRequest> result = parser.parseDocument(Jsoup.connect(
				"http://xdcc.it/?q=The+imitation+game").get());
		Assert.assertTrue(result.size() > 0);
		for (XdccRequest string : result) {
			System.out.println(string);
		}
	}
}
