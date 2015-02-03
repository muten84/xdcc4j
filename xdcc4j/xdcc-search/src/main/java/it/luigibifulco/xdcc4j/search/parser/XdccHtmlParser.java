package it.luigibifulco.xdcc4j.search.parser;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;

import java.util.Set;

import org.jsoup.nodes.Document;

public interface XdccHtmlParser {

	public Set<XdccRequest> parseDocument(Document doc);

	public String getType();

}
