package it.luigibifulco.xdcc4j.search.parser;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.parser.ParserFactoryModule.XdccParserType;

import java.io.IOException;
import java.util.Set;

import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.google.inject.name.Named;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules(XdccParserTestModule.class)
public class XdccItParserTest {

	@Inject
	@Named("xdccit")
	private XdccHtmlParser parser;

	@Before
	public final void before() {

	}

	@Test
	public final void testXdccITResult() throws IOException {
		System.out.println("parser instance: " + this.parser.getClass());
		Set<XdccRequest> result = parser.parseDocument(Jsoup.connect(
				"http://xdcc.it/?q=The+imitation+game").get());
		Assert.assertTrue(result.size() > 0);
		for (XdccRequest string : result) {
			System.out.println(string);
		}
	}
}
