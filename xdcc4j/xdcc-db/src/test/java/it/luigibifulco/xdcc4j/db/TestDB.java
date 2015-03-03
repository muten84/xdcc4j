package it.luigibifulco.xdcc4j.db;

import java.util.UUID;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.common.util.XdccRequestCreator;

import org.junit.Test;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

public class TestDB {

	@Test
	public final void testInsert() {

		// HyperGraph db = new HyperGraph();
		// db.open("./db");
		// HGHandle handle = db.add("awe");
		// System.out.println(db.get(handle));
		// db.close();
		ODB odb = ODBFactory.open("test.neodatis");
		for (int i = 0; i < 1000; i++) {
			XdccRequest req = new XdccRequest();
			req.setChannel(UUID.randomUUID().toString());
			req.setDescription("description");
			req.setDestination("./");
			req.setHost("127.0.0.1");
			req.setPeer("peer");
			req.setResource("12");
			req.setTtl(324234234);
			req=XdccRequestCreator.identify(req);
			odb.store(req);
			// odb.commit();
		}

		odb.close();
		odb = ODBFactory.open("test.neodatis");
		IQuery query = new CriteriaQuery(XdccRequest.class, Where.ilike(
				"description", "desc"));
		Objects<XdccRequest> requests = odb.getObjects(query);
		System.out.println("SIZE IS>>>>>: " + requests.size());
		for (XdccRequest xdccRequest : requests) {
			System.out.println(xdccRequest);
		}

	}
}
