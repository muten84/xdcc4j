package it.luigibifulco.xdcc4j.db;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.common.util.XdccRequestCreator;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class XdccRequestStoreTest {

	@Test
	public final void testSearchRandom() {
		XdccRequestStore store = null;
		try {
			store = new XdccRequestStore("cache");
			XdccRequest newR = XdccRequestCreator.identify(XdccRequestCreator
					.create(UUID.randomUUID().toString().substring(0, 5), ""
							+ new Random().ints().findFirst().getAsInt(), UUID
							.randomUUID().toString().substring(0, 5)));
			store.insert(newR);
			List<XdccRequest> result = store.searchByExample(newR);
			Assert.assertNotNull(result);
			Assert.assertTrue(result.size() == 1);
			for (XdccRequest xdccRequest : result) {
				System.out.println(xdccRequest);
			}
		} finally {
			if (store != null) {
				store.clear();
				store.close();
			}

		}
	}

	@Test
	public final void testSearchNullDescription() {
		XdccRequestStore store = null;
		try {
			store = new XdccRequestStore("cache");
			XdccRequest newR = XdccRequestCreator.identify(XdccRequestCreator
					.create("channel", "peer", "12"));
			store.insert(newR);
			XdccRequest newR2 = XdccRequestCreator.identify(XdccRequestCreator
					.create("asdasd", "asdasd", "454545"));
			store.insert(newR2);
			XdccRequest filter = XdccRequestCreator.identify(XdccRequestCreator
					.create("", null, null));
			List<XdccRequest> result = store.searchByExample(filter);
			Assert.assertNotNull(result);
			for (XdccRequest xdccRequest : result) {
				System.out.println(xdccRequest);
			}
		} finally {
			if (store != null) {
				store.clear();
				store.close();
			}

		}
	}

	@Test
	public final void testClear() {
		XdccRequestStore store = null;
		try {
			store = new XdccRequestStore("cache");
			if (store.count() > 0) {
				List<XdccRequest> r = (List<XdccRequest>) store.getAll();
				for (XdccRequest xdccRequest : r) {
					System.out.println(xdccRequest);
				}
				store.clear();
				store.close();
			}
		} finally {
		}
	}

	@Test
	public final void testSearchSpecific() {
		XdccRequestStore store = null;
		try {
			store = new XdccRequestStore("cache");
			XdccRequest newR = XdccRequestCreator.identify(XdccRequestCreator
					.create("channel", "peer", "12"));
			store.insert(newR);
			XdccRequest newR2 = XdccRequestCreator.identify(XdccRequestCreator
					.create("asdasd", "asdasd", "454545"));
			store.insert(newR2);
			XdccRequest filter = XdccRequestCreator.identify(XdccRequestCreator
					.create("chan", null, null));
			List<XdccRequest> result = store.searchByExample(filter);
			Assert.assertNotNull(result);
			Assert.assertTrue("Size is: " + result.size(), result.size() == 1);
			Assert.assertTrue(result.get(0).getId().equals(newR.getId()));
			for (XdccRequest xdccRequest : result) {
				System.out.println(xdccRequest);
			}
		} finally {
			if (store != null) {
				store.clear();
				store.close();
			}

		}
	}
}
