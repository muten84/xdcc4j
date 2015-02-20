package it.luigibifulco.xdcc4j.common.util;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;

import org.apache.commons.codec.digest.DigestUtils;

public class XdccRequestCreator {

	public static XdccRequest identify(XdccRequest r) {
		String s = new String(r.getChannel() + r.getPeer() + r.getResource());
		r.setId(DigestUtils.md5Hex(s));
		return r;
	}

	public static XdccRequest create(String channel, String peer,
			String resource) {
		XdccRequest r = new XdccRequest();
		r.setChannel(channel);
		r.setPeer(peer);
		r.setResource(resource);
		return r;
	}

	// The Imitation Game 2014 iTALiAN MD DVDSCR XviD-FREE.avi,LiC|NeWs|13,
	// #licantropo, DarkSin, 127, irc.darksin.it
	public static XdccRequest convertFromXdccItResult(String result) {

		String[] splitted = result.split(",");
		if (splitted.length == 6) {
			String peer = splitted[1].trim();
			String resource = splitted[4].trim();
			String channel = splitted[2].replace("#", "").trim();
			XdccRequest request = create(channel, peer, resource);
			request.setDescription(splitted[0]);
			request.setHost(splitted[5].trim());
			return identify(request);
		} else {
			return null;
		}

	}

	public static XdccRequest convertFromXdccFinderResult(String result) {

		String[] splitted = result.split(",");
		if (splitted.length == 5) {
			String channel = splitted[3].trim();
			String peer = splitted[4].trim();
			String resource = splitted[0].replace("#", "").trim();
			XdccRequest request = create(channel, peer, resource);
			request.setDescription(splitted[2]);
			return identify(request);
		}
		return null;
	}
}
