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
		downloader.setServer("irc.crocmax.net");
		String downloadId = null;
		Iterator<XdccRequest> requests = downloader.search("", "2013").values()
				.iterator();
		while (requests.hasNext()) {
			downloadId = requests.next().getId();
			downloadId = downloader.startDownload(downloadId);
			Thread.sleep(15000);
			Assert.assertNotNull(downloader.getDownload(downloadId));
			if (downloader.getDownload(downloadId).getState()
					.equals(TransferState.RUNNABLE.name())
					|| downloader.getDownload(downloadId).getState()
							.equals(TransferState.WORKING.name())) {
				downloadId = downloader.cancelDownload(downloadId);
				break;
			} else {
				continue;
			}

		}
		Thread.sleep(15000);
		Assert.assertTrue(downloader.getDownload(downloadId).getState()
				.equals(TransferState.ABORTED.name()));

	}
}
