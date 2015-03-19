package it.luigibifulco.xdcc4j.search.parser;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.common.util.XdccRequestCreator;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class PuffolandiaParser implements XdccHtmlParser {

	@Override
	public Set<XdccRequest> parseDocument(Document doc) {
		Elements elems = doc.select("#table5");
		Set<XdccRequest> result = new HashSet<XdccRequest>();
		String[] s = elems.get(0).text().split(" ");
		if (s == null || s.length == 0 || s.length < 4) {
			return result;
		}
		for (int j = 0; j < s.length; j += 4) {
			// for (int i = 0; i < 3; i++) {

			String channel = "puffolandia";

			String peer = s[j];
			String resource = s[j + 1].replace("#", "");

			XdccRequest req = XdccRequestCreator
					.create(channel, peer, resource);
			req.setHost("irc.oltreirc.net");
			req.setDescription(s[j + 3]);
			req = XdccRequestCreator.identify(req);
			result.add(req);
			// }
		}

		return result;

	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
