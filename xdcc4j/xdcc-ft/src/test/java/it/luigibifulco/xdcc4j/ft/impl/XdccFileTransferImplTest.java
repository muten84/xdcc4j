package it.luigibifulco.xdcc4j.ft.impl;

import it.biffi.jirc.bot.BotException;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer.FileTransferStatusListener;
import it.luigibifulco.xdcc4j.search.XdccSearch;
import it.luigibifulco.xdcc4j.search.impl.XdccSearchImpl;
import it.luigibifulco.xdcc4j.search.query.XdccQuery.QueryCondition;
import it.luigibifulco.xdcc4j.search.query.XdccQuery.QueryFilter;
import it.luigibifulco.xdcc4j.search.query.XdccQueryBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class XdccFileTransferImplTest {

	private XdccFileTransferImpl xdccFt;

	private XdccSearch search;

	private String domain = "xdcc.it";

	@Before
	public void init() {
		// TODO: GUICE!!?!?? search = new XdccSearchImpl(domain);

	}

	@Test
	public final void testFtStart() throws BotException, InterruptedException {
		Iterator<XdccRequest> result = search.search(
				XdccQueryBuilder.create().to(domain)
						.params("mutant chronicles")).iterator();
		while (result.hasNext()) {
			XdccRequest req = result.next();
			if (req.getHost() == null || req.getHost().isEmpty()) {
				req.setHost("irc.chlame.net");
			}
			req.setDestination("/Users/Luigi/Downloads/irc/");

			xdccFt = new XdccFileTransferImpl(req, 30000, 60000);
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

				@Override
				public void onStatusUpdate(String status) {
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

	}

	@Test
	public final void testFtStartWithReplace() throws BotException,
			InterruptedException {
		Map<QueryFilter, String> map = new HashMap<QueryFilter, String>();
		map.put(QueryFilter.HOST, "irc.darksin.it");
		Map<QueryCondition, String> map2 = new HashMap<QueryCondition, String>();
		map2.put(QueryCondition.HOST, "irc.crocmax.net");
		Iterator<XdccRequest> result = search.search(
				XdccQueryBuilder.create().to("xdcc.it").params("divx ita")
						.excludeFilter(map).replacefilter(map2)).iterator();
		while (result.hasNext()) {
			XdccRequest req = result.next();
			if (req == null) {
				continue;
			}
			req.setDestination("/Users/Luigi/Downloads/irc/");

			xdccFt = new XdccFileTransferImpl(req, 1000, 1000);
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

				@Override
				public void onStatusUpdate(String status) {
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

	}
}
