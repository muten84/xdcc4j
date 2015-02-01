package it.luigibifulco.xdcc4j.downloader;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.downloader.impl.Download;
import it.luigibifulco.xdcc4j.downloader.impl.SimpleXdccDownloader;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer.TransferState;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DownloaderTest {

	private SimpleXdccDownloader downloader;

	@Before
	public void init() {
		if (downloader == null) {
			downloader = new SimpleXdccDownloader("xdcc.it", "");
		}
	}

	@Test
	public final void testSearch() {
		Map<String, XdccRequest> result = downloader
				.search("mutant chronicles");
		Assert.assertTrue(result.size() > 0);

	}

	@Test
	public final void testStartDirect() {
		downloader.search("mutant chronicles");
		String id = downloader.startDownload("7");
		Assert.assertTrue(id != null);
		Download d = downloader.getDownload(id);
		TransferState state = d.getCurrentTransfer().getState();
		Assert.assertTrue(state == TransferState.WORKING);
	}

	@Test
	public final void testStartAnyDownload() {
		downloader.search("mutant chronicles");
		String id = downloader.startAnyAvailableFromList();
		Assert.assertTrue(id != null);
		Download d = downloader.getDownload(id);
		TransferState state = d.getCurrentTransfer().getState();
		Assert.assertTrue(state == TransferState.WORKING);
	}
}
