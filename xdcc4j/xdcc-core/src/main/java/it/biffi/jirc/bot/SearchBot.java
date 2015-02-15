package it.biffi.jirc.bot;

import it.biffi.jirc.bot.event.ChannelInfoEvent;
import it.biffi.jirc.bot.event.GenericEvent;
import it.biffi.jirc.bot.event.MessageEvent;
import it.biffi.jirc.bot.event.XdccEvent;
import it.biffi.jirc.bot.listener.AbstractListener;
import it.biffi.jirc.bot.listener.ChannelInfoListener;
import it.biffi.jirc.bot.listener.MessageListener;
import it.biffi.jirc.bot.listener.UserListListener;
import it.biffi.jirc.bot.listener.XdccListener;
import it.biffi.jirc.bot.util.FutureResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SearchBot implements Bot {

	protected SynchBotConnector connector;

	public SearchBot(boolean verbose) {
		connector = new SynchBotConnector(verbose);

	}

	public int getUserListLines(String user, String channel) {
		FutureResult<Integer> result = new FutureResult<Integer>(0);
		connector.getEbus().addEventListener(new XdccEvent(),
				new XdccListener() {

					@Override
					public void onLineCount(int lines) {
						// connector.getEbus().removeAll();
						System.out.println("user sent lines :" + lines);
						result.setResult(lines);

					}
				});
		connector.getBot().joinChannel(channel);
		connector.getBot().sendMessage(user, "XDCC LIST");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connector.getBot().sendMessage(user, "XDCC STOP");

		return result.get(5000);
	}

	public List<String> scanUser(String user, String channel, int lines)
			throws InterruptedException, ExecutionException {
		List<String> entries = new ArrayList<String>();
		if (lines <= 1) {
			return entries;
		}

		FutureResult<List<String>> result = new FutureResult<List<String>>(
				entries);
		connector.getEbus().addEventListener(new MessageEvent(),
				new MessageListener() {

					@Override
					public void onMessage(Map<String, String> data) {
						if (entries.size() >= lines) {
							// connector.getEbus().removeAll();
							result.setResult(entries);
						}
						if (data.get(MessageEvent.SENDER).equals(user)) {
							entries.add(data.get(MessageEvent.MESSAGE));
						}

					}
				});
		connector.getBot().joinChannel(channel);
		Thread.sleep(1000);
		connector.getBot().sendMessage(user, "ciao a tutti!!");
		Thread.sleep(1000);
		// connector.getBot().sendMessage(user, "!list");
		connector.getBot().sendMessage(user, "XDCC LIST");
		long wait = 1000 * 60 * 10;
		return result.get(wait);

	}

	public List<String> listUsersInChannel(String channel) {
		List<String> s = new ArrayList<String>();
		FutureResult<List<String>> channelsFuture = new FutureResult<List<String>>(
				s);
		connector.getEbus().addEventListener(new ChannelInfoEvent(),
				new UserListListener() {

					@Override
					public void onUserList(List<String> users) {
						// connector.getEbus().removeAll();
						channelsFuture.setResult(users);

					}

				});
		if (!channel.startsWith("#")) {
			channel = "#" + channel;
		}
		connector.getBot().joinChannel(channel);

		return channelsFuture.get(120000);
	}

	public List<String> listChannels() {
		List<String> s = new ArrayList<String>();
		FutureResult<List<String>> channelsFuture = new FutureResult<List<String>>(
				s);
		connector.getEbus().addEventListener(new ChannelInfoEvent(),
				new ChannelInfoListener() {

					@Override
					public void onChannelInfo(Map<String, String> data) {
						// System.out.println(">>>onChannelInfo: " + data);
						s.add(data.get(ChannelInfoEvent.CHANNEL));

					}

					@Override
					public void onEndOf() {
						// connector.getEbus().removeAll();
						channelsFuture.setResult(s);

					}
				});
		connector.getBot().sendRawLineViaQueue("LIST");
		return channelsFuture.get(60000);
	}

	@Override
	public void start(BotConfig config) throws BotException {
		BotClientConfig aConfig = (BotClientConfig) config;
		// connector.getBot().setAutoNickChange(true);
		boolean connected = connector.connect(aConfig.getNick(),
				aConfig.getServer(), 6000);

	}

	@Override
	public void stop() {
		connector.getBot().disconnect();
		connector.getBot().dispose();
		connector.getEbus().removeAll();

	}

	@Override
	public void sendMessage(String to, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addEventListener(GenericEvent event,
			AbstractListener<?> listener) {
		connector.getEbus().addEventListener(event, listener);

	}

}
