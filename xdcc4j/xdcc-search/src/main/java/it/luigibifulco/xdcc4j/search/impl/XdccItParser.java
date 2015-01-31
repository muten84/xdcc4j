package it.luigibifulco.xdcc4j.search.impl;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.luigibifulco.xdcc4j.search.XdccHtmlParser;

public class XdccItParser implements XdccHtmlParser {

	@Override
	public Set<String> parseDocument(Document doc) {
		Set<String> result = new HashSet<String>();
		Elements elems = doc.select("tr[onmouseover]");
		for (Element element : elems) {
			String s = element.attr("onmouseover").toString();
			s = s.replace("xdt(", "");
			s = s.replace(");", "");
			s = s.replace("'", "");
			result.add(s);
		}

		return result;

	}
}
