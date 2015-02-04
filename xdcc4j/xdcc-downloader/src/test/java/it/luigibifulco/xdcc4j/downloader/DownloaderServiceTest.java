package it.luigibifulco.xdcc4j.downloader;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;

import java.util.Collection;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules(DownloaderServiceModule.class)
public class DownloaderServiceTest {

	@Inject
	private XdccDownloader downloader;

	@Test
	public final void testSearch() {
		Map<String, XdccRequest> searchResult = downloader.search("xdccit",
				"divx ita");
		Assert.assertTrue(downloader.cache().size() == searchResult.size());
		Collection<XdccRequest> reqs = downloader.cache().values();
		for (XdccRequest xdccRequest : reqs) {
			System.out.println(xdccRequest);
		}
	}
}
