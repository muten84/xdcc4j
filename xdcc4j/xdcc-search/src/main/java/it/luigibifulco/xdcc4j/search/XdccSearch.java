package it.luigibifulco.xdcc4j.search;

import java.util.Set;

public interface XdccSearch {

	public Set<String> search(XdccQuery query) throws RuntimeException;

}
