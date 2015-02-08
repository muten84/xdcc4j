package it.luigibifulco.xdcc4j.search.scan;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.search.XdccScanner;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class XdccScanTest {

	@Test
	public void testScan() {
		XdccScanner scanner = new XdccScanner();
		List<XdccRequest> list = scanner.scan("irc.uragano.org", "#SUNSHINE",
				"SUN|DVDRIP|01");
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() > 0);
		for (XdccRequest xdccRequest : list) {
			System.out.println(xdccRequest);
		}
	}

}
