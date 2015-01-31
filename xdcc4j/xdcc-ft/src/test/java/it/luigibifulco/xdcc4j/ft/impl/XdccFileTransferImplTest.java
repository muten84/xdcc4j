package it.luigibifulco.xdcc4j.ft.impl;

import java.util.Arrays;
import java.util.Iterator;

import it.biffi.jirc.bot.BotException;
import it.biffi.jirc.bot.util.FutureResult;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.common.util.XdccRequestCreator;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer.FileTransferStatusListener;
import it.luigibifulco.xdcc4j.search.XdccQueryBuilder;
import it.luigibifulco.xdcc4j.search.XdccSearch;
import it.luigibifulco.xdcc4j.search.impl.HttpXdccSearch;
import it.luigibifulco.xdcc4j.search.impl.HttpXdccSearchEngine;
import it.luigibifulco.xdcc4j.search.impl.XdccItParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XdccFileTransferImplTest {

	private XdccFileTransferImpl xdccFt;

	private XdccSearch search;

	@Before
	public void init() {
		search = new HttpXdccSearch();
		HttpXdccSearchEngine engine = new HttpXdccSearchEngine(
				Arrays.asList(new String[] { "q" }));
		engine.setParser(new XdccItParser());
		((HttpXdccSearch) search).setEngine(engine);
	}

	@Test
	public final void testFtStart() throws BotException, InterruptedException {
		Iterator<String> result = search.search(
				XdccQueryBuilder.create().to("http://xdcc.it")
						.params("mutant chronicles")).iterator();
		while (result.hasNext()) {
			String s = result.next();
			XdccRequest req = XdccRequestCreator.convertFromXdccItResult(s);
			req.setDestination("/Users/Luigi/Downloads/irc/");

			xdccFt = new XdccFileTransferImpl(req);
			System.out.println(xdccFt.getState());
			boolean started = xdccFt.start(new FileTransferStatusListener() {

				@Override
				public void onStart() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onProgress(int perc, int rate) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onError(Throwable e) {
					// TODO Auto-generated method stub

				}
			});
			System.out.println(xdccFt.getState());
			if (started) {
				break;

			} else {
				xdccFt.cancel();
			}
			// simulate wait for trasnfer finish

			// FutureResult<Boolean> lateRes = new FutureResult<Boolean>(false);

		}
		Thread.sleep(10000);
	}
}
