package it.luigibifulco.xdcc4j.db;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.junit.Test;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;

public class TestHgDb {

	@Test
	public final void testInsert() {
		XdccRequest req = new XdccRequest();
		req.setChannel("channel");
		req.setDescription("description");
		req.setDestination("./");
		req.setHost("127.0.0.1");
		req.setPeer("peer");
		req.setResource("12");
		req.setTtl(324234234);
		HyperGraph db = new HyperGraph("./db");
		HGHandle handle = db.add(req);
		System.out.println(db.get(handle));
		db.close();
	}

}
