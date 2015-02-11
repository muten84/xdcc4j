package it.luigibifulco.xdcc4j.db;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.common.util.XdccRequestCreator;

public class XdccRequestStore extends ObjectStore<XdccRequest> {

	public XdccRequestStore(String name) {
		super(name, XdccRequest.class);
	}

	@Override
	public XdccRequest insert(XdccRequest e) {
		e = XdccRequestCreator.identify(e);
		return super.insert(e);
	}

}
