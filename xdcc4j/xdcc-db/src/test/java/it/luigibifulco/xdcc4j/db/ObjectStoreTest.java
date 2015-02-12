package it.luigibifulco.xdcc4j.db;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.common.util.XdccRequestCreator;

import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

public class ObjectStoreTest {

	XdccRequest setId(XdccRequest r) {
		String s = new String(r.getChannel() + r.getPeer() + r.getResource());
		r.setId(DigestUtils.md5Hex(s));
		return r;
	}

	@Test
	public final void testRemove() {
		ObjectStore<XdccRequest> store = new ObjectStore<XdccRequest>(
				"requests.neodatis2", XdccRequest.class) {
			@Override
			public List<XdccRequest> searchByExample(XdccRequest e) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected Class<XdccRequest> getEntityType() {
				return XdccRequest.class;
			}
		};
		try {
			XdccRequest req = new XdccRequest();
			req.setChannel(UUID.randomUUID().toString());
			req.setDescription("description");
			req.setDestination("./");
			req.setHost("127.0.0.1");
			req.setPeer("peer");
			req.setResource("12");
			req.setTtl(324234234);
			req = XdccRequestCreator.identify(req);
			Assert.assertNotNull(store.insert(req));
			long value = store.count();
			Assert.assertTrue("Value is " + value, value == 1);
			Assert.assertNotNull(store.get(req.getId()));
			Assert.assertNotNull(store.remove(req));
			Assert.assertTrue(store.count() == 0);
		} finally {
			store.clear();
			Assert.assertTrue(store.close());
		}

	}

	@Test
	public final void testInsert() {

		ObjectStore<XdccRequest> store = new ObjectStore<XdccRequest>(
				"requests.neodatis", XdccRequest.class) {
			@Override
			public List<XdccRequest> searchByExample(XdccRequest e) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected Class<XdccRequest> getEntityType() {
				return XdccRequest.class;
			}
		};
		try {
			XdccRequest req = new XdccRequest();
			req.setChannel(UUID.randomUUID().toString());
			req.setDescription("description");
			req.setDestination("./");
			req.setHost("127.0.0.1");
			req.setPeer("peer");
			req.setResource("12");
			req.setTtl(324234234);
			req = XdccRequestCreator.identify(req);
			Assert.assertNotNull(store.insert(req));
			long value = store.count();
			Assert.assertTrue("Value is " + value, value == 1);
			store.clear();
			Assert.assertTrue(store.count() == 0);

		} finally {

			Assert.assertTrue(store.close());
		}
	}

}
