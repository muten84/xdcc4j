package it.luigibifulco.xdcc4j.search.parser;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.common.util.XdccRequestCreator;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class XdccFinderParser implements XdccHtmlParser {

	@Override
	public Set<XdccRequest> parseDocument(Document doc) {
		Elements els = doc.select(".tableRow");
		Set<XdccRequest> result = new HashSet<XdccRequest>();
		for (Element element : els) {
			StringBuffer buff = new StringBuffer();
			buff.append(element.select("#packNumber").get(0).ownText());
			buff.append(",");
			buff.append(element.select("#pSize").get(0).ownText());
			buff.append(",");
			buff.append(element.select(".pName*").get(0).ownText());
			buff.append(",");
			buff.append(element.select("#channelName").get(0).ownText());
			buff.append(",");
			buff.append(element.select("#botName").get(0).ownText());

			result.add(XdccRequestCreator.convertFromXdccFinderResult(buff
					.toString()));
		}
		return result;
	}
}
