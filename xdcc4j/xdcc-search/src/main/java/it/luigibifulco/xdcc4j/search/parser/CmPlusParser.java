package it.luigibifulco.xdcc4j.search.parser;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.common.util.XdccRequestCreator;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CmPlusParser implements XdccHtmlParser {

	@Override
	public Set<XdccRequest> parseDocument(Document doc) {
		Elements els = doc.select("tr");
		Set<XdccRequest> result = new HashSet<XdccRequest>();

		for (Element element : els) {
			String[] s = element.text().split(" ");
			StringBuffer buff = new StringBuffer();
			buff.append(s[1].replace("#", ""));
			buff.append(",");
			buff.append(s[2]);
			buff.append(",");
			buff.append(s[4]);
			buff.append(",");
			buff.append("cm-plus");
			buff.append(",");
			buff.append(s[0]);
			result.add(XdccRequestCreator.convertFromXdccFinderResult(buff
					.toString()));
		}

		return result;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
