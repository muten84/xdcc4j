package it.luigibifulco.xdcc4j.common.util;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;

public class XdccRequestCreator {

	// The Imitation Game 2014 iTALiAN MD DVDSCR XviD-FREE.avi,LiC|NeWs|13,
	// #licantropo, DarkSin, 127, irc.darksin.it
	public static XdccRequest convertFromXdccItResult(String result) {
		XdccRequest request = new XdccRequest();
		String[] splitted = result.split(",");
		request.setChannel(splitted[2].replace("#", "").trim());
		request.setHost(splitted[5].trim());
		request.setPeer(splitted[1].trim());
		request.setResource(splitted[4].trim());
		return request;
	}
}
