package it.luigibifulco.xdcc4j.search;

import it.biffi.jirc.bot.BotClientConfig;
import it.biffi.jirc.bot.BotException;
import it.biffi.jirc.bot.SearchBot;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class XdccScanner {

	public List<XdccRequest> scan(String server, String channel, String user) {
		List<XdccRequest> requests = new ArrayList<XdccRequest>();
		SearchBot bot = new SearchBot(false);
		BotClientConfig config = new BotClientConfig();
		config.setServer(server);
		config.setNick("xdccBot" + UUID.randomUUID().toString().substring(0, 6));
		try {
			bot.start(config);
		} catch (BotException e) {
			return requests;
		}
		int lines = bot.getUserListLines(user, channel);
		try {
			List<String> packets = bot.scanUser(user, channel, lines);
			for (String p : packets) {
				XdccRequest request = templateFromPacket(p);
				if (request == null) {
					continue;
				}
				request.setChannel(channel);
				request.setHost(server);
				request.setPeer(user);

				requests.add(request);
			}
		} catch (InterruptedException | ExecutionException e) {
			return requests;
		}
		bot.stop();
		return requests;
	}

	public List<XdccRequest> scan(String server, String channel) {
		List<XdccRequest> requests = new ArrayList<XdccRequest>();
		SearchBot bot = new SearchBot(false);
		BotClientConfig config = new BotClientConfig();
		config.setServer(server);
		config.setNick("xdccBot" + UUID.randomUUID().toString().substring(0, 6));
		try {
			bot.start(config);
		} catch (BotException e) {
			return requests;
		}
		List<String> users = bot.listUsersInChannel(channel);
		for (String u : users) {
			int lines = bot.getUserListLines(u, channel);
			try {
				List<String> packets = bot.scanUser(u, channel, lines);
				for (String p : packets) {
					XdccRequest request = templateFromPacket(p);
					if (request == null) {
						continue;
					}
					request.setChannel(channel);
					request.setHost(server);
					request.setPeer(u);
					// request.setResource("");
					requests.add(request);
				}
			} catch (InterruptedException | ExecutionException e) {
				return requests;
			}
		}
		bot.stop();
		return requests;
	}

	public List<XdccRequest> scan(String server) {
		List<XdccRequest> requests = new ArrayList<XdccRequest>();
		SearchBot bot = new SearchBot(false);
		BotClientConfig config = new BotClientConfig();
		config.setServer(server);
		config.setNick("xdccBot" + UUID.randomUUID().toString().substring(0, 6));
		try {
			bot.start(config);
		} catch (BotException e) {
			return requests;
		}
		List<String> channels = bot.listChannels();
		for (String c : channels) {
			List<String> users = bot.listUsersInChannel(c);
			for (String u : users) {
				int lines = bot.getUserListLines(u, c);
				try {
					List<String> packets = bot.scanUser(u, c, lines);
					for (String p : packets) {
						XdccRequest request = templateFromPacket(p);
						if (request == null) {
							continue;
						}
						request.setChannel(c);
						request.setHost(server);
						request.setPeer(u);
						// request.setResource("");
						requests.add(request);
					}
				} catch (InterruptedException | ExecutionException e) {
					return requests;
				}
			}
		}
		bot.stop();
		return requests;
	}

	// #17 1x [1.4G] Le.Iene.Show.E02.01.10.2013.iTALiAN.PDTV.XviD-ACiD.avi
	private static XdccRequest templateFromPacket(String packet) {
		XdccRequest r = new XdccRequest();
		int start = packet.indexOf('#');
		String resource = packet.substring(start + 1, start + 3).trim();
		try {
			Integer.valueOf(resource);
		} catch (Exception e) {
			return null;
		}
		r.setResource(resource);
		start = packet.indexOf(']');
		String desc = packet.substring(start + 1, packet.length() - 1).trim();
		r.setDescription(desc);
		return r;

	}

	public static void main(String[] args) {
		String p = "#17 1x [1.4G] Le.Iene.Show.E02.01.10.2013.iTALiAN.PDTV.XviD-ACiD.avi";
		XdccRequest request = templateFromPacket(p);
		System.out.println(request);
	}
}
