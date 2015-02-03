package it.luigibifulco.xdcc4j.search.parser;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.common.util.XdccRequestCreator;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class XdccItParser implements XdccHtmlParser {

	@Override
	public Set<XdccRequest> parseDocument(Document doc) {
		Set<XdccRequest> result = new HashSet<XdccRequest>();
		Elements elems = doc.select("tr[onmouseover]");
		for (Element element : elems) {
			String s = element.attr("onmouseover").toString();
			s = s.replace("xdt(", "");
			s = s.replace(");", "");
			s = s.replace("'", "");
			result.add(XdccRequestCreator.convertFromXdccItResult(s));
		}

		return result;

	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
