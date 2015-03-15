package it.luigibifulco.xdcc4j.search;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SearchUtil {

	public Map<String, List<String>> networkChannels;

	private static SearchUtil INSTANCE;

	private SearchUtil() {
		networkChannels = new HashMap<String, List<String>>();
		networkChannels.put("irc.1andallirc.net",
				Arrays.asList("cosmic", "viet-power", "xboxland-mp3"));
		networkChannels.put("irc.420-hightimes.com",
				Arrays.asList("comedy", "movies", "music", "tv"));
		networkChannels.put("ger0nim0.4ntrim.co.uk",
				Arrays.asList("ham-radio", "warehouse"));
		networkChannels
				.put("irc.abjects.net", Arrays.asList("1warez", "airborne",
						"beast-xdcc", "elitemusic", "evil", "krautz-warez",
						"liberty", "moviegods", "overflow", "xdcc",
						"[rm]rolys-movies"));

		networkChannels.put("irc.absoluty-irc.org", Arrays.asList("xdcc"));
		networkChannels.put("alphairc.com",
				Arrays.asList("hidd3n-xdcc", "panamamusic507"));
		networkChannels.put("irc.chlame.net", Arrays.asList("cinemax",
				"ferrari", "music", "fusion", "heroes"));
		networkChannels
				.put("irc.darksin.it", Arrays.asList("drakon", "licantropo",
						"lord_empire", "supremo"));
		networkChannels.put("irc.devilirc.org",
				Arrays.asList("PapRika", "shareit_news"));
		networkChannels.put("irc.oceanirc.net",
				Arrays.asList("ocean", "oce@n", "oce@n-music"));
		networkChannels.put("irc.uragano.org", Arrays.asList("sunshine"));
		networkChannels.put("irc.oltreirc.net",
				Arrays.asList("puffolandia", "BLaCKCaVe"));
		networkChannels.put("irc.openjoke.org",
				Arrays.asList("enjoy", "A-R-E-S"));
		networkChannels.put("irc.crocmax.net",
				Arrays.asList("cm-plus"));

	}

	public synchronized static SearchUtil getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SearchUtil();
		}
		return INSTANCE;
	}

	public String getHostFromChannel(String channel) {
		Set<Entry<String, List<String>>> entries = networkChannels.entrySet();
		for (Entry<String, List<String>> entry : entries) {
			List<String> values = entry.getValue();
			for (String string : values) {
				if (string.equalsIgnoreCase(channel)) {
					return entry.getKey();
				}
			}
		}
		return null;
	}
}
