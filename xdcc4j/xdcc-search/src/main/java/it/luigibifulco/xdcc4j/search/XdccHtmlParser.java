package it.luigibifulco.xdcc4j.search;

import java.util.Set;

import org.jsoup.nodes.Document;

public interface XdccHtmlParser {

	public Set<String> parseDocument(Document doc);

}
