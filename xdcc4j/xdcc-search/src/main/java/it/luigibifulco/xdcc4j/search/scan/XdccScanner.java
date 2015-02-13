package it.luigibifulco.xdcc4j.search.scan;

import it.biffi.jirc.bot.BotClientConfig;
import it.biffi.jirc.bot.BotException;
import it.biffi.jirc.bot.SearchBot;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.common.util.XdccRequestCreator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
				request = XdccRequestCreator.identify(request);
				requests.add(request);
			}
		} catch (InterruptedException | ExecutionException e) {
			return requests;
		}
		bot.stop();
		return requests;
	}

	public List<XdccRequest> scan(String server, String channel,
			boolean parallel) {
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
		if (!parallel) {
			for (String u : users) {
				System.out.println("scanning user: " + u);
				int lines = bot.getUserListLines(u, channel);
				System.out.println("user " + u + " will send " + lines);
				try {
					List<String> packets = bot.scanUser(u, channel, lines);
					System.out.println("user " + u + "sent " + packets.size()
							+ " packets");
					for (String p : packets) {
						XdccRequest request = templateFromPacket(p);
						if (request == null) {
							continue;
						}
						request.setChannel(channel);
						request.setHost(server);
						request.setPeer(u);
						request = XdccRequestCreator.identify(request);
						// request.setResource("");
						requests.add(request);
					}
					System.out.println("Current requests size is: "
							+ requests.size());
				} catch (InterruptedException | ExecutionException e) {
					return requests;
				}
			}
			bot.stop();
		} else {
			ExecutorService service = Executors.newCachedThreadPool();
			Collection<Future<List<XdccRequest>>> futures = new ArrayList<Future<List<XdccRequest>>>();
			Collection<Callable<List<XdccRequest>>> tasks = new ArrayList<Callable<List<XdccRequest>>>();
			System.out.println("scanning " + users.size() + " users...");
			for (String user : users) {
				Callable<List<XdccRequest>> task = () -> {
					SearchBot abot = new SearchBot(false);
					BotClientConfig aconfig = new BotClientConfig();
					aconfig.setServer(server);
					aconfig.setNick("xdccBot" + UUID.randomUUID().toString().substring(0, 6));
					try {
						abot.start(config);
					} catch (BotException e) {
						return requests;
					}
					int lines = abot.getUserListLines(user, channel);
					System.out.println("scanning user: " + user);
					List<String> packets = abot.scanUser(user, channel, lines);
					if (packets == null) {
						packets = new ArrayList<String>();
					}

					System.out.println("user " + user + " sent : "
							+ packets.size() + " packets");
					List<XdccRequest> reqs = new ArrayList<XdccRequest>();
					for (String p : packets) {
						XdccRequest request = templateFromPacket(p);
						if (request == null) {
							continue;
						}
						request.setChannel(channel);
						request.setHost(server);
						request.setPeer(user);
						// request.setResource("");
						reqs.add(request);
					}
					System.out.println("user " + user
							+ " scanned current cache size: " + reqs.size()
							+ " packets");
					return reqs;
				};
				// tasks.add(task);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				futures.add(service.submit(task));

			}

			// try {
			//
			// //futures = service.invokeAll(tasks);
			// } catch (InterruptedException e1) {
			// e1.printStackTrace();
			// }
			for (Future<List<XdccRequest>> future : futures) {
				try {
					requests.addAll(future.get());
				} catch (InterruptedException | ExecutionException e) {

				}
			}

		}

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
		List<String> channels = new ArrayList<String>(bot.listChannels());
		for (String c : channels) {
			List<String> users = bot.listUsersInChannel(c);
			for (String u : users) {
				int lines = bot.getUserListLines(u, c);
				try {
					if (lines <= 0) {
						continue;
					}
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
						request = XdccRequestCreator.identify(request);
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
