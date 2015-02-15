package it.luigibifulco.xdcc4j.downloader;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.downloader.core.XdccDownloader;
import it.luigibifulco.xdcc4j.downloader.core.service.DownloaderServiceModule;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer.TransferState;

import java.util.Collection;
import java.util.Iterator;
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

	@Test
	public final void testDownload() throws InterruptedException {
		downloader.setServer("irc.uragano.org");
		downloader.search("xdccit", "divx ita");
		Collection<XdccRequest> reqs = downloader.cache().values();
		for (XdccRequest xdccRequest : reqs) {
			System.out.println(xdccRequest);
			xdccRequest.setTtl(60000);
		}
		String downloadId = null;
		Iterator<String> iter = downloader.cache().keySet().iterator();
		while (iter.hasNext()) {
			downloadId = downloader.startDownload(iter.next());
			Assert.assertNotNull(downloader.getDownload(downloadId));

			if (downloader.getDownload(downloadId).getState()
					.equals(TransferState.RUNNABLE.name())) {
				break;
			}
			downloader.cancelDownload(downloadId);
		}
		if (downloader.getDownload(downloadId).getState()
				.equals(TransferState.WORKING.name())) {
			Assert.assertTrue(downloader.getDownload(downloadId).getRate() > 0);
		}
		Thread.sleep(15000);

	}
}
