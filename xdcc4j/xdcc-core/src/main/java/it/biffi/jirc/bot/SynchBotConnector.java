package it.biffi.jirc.bot;

import it.biffi.jirc.bot.AbstractPircBot.InnerBot;
import it.biffi.jirc.bot.listener.ConnectionListener;
import it.biffi.jirc.bot.listener.EventListenerManager;
import it.biffi.jirc.bot.util.FutureResult;

public class SynchBotConnector {

	private BotConnector connector;
	private boolean verbose;

	public SynchBotConnector(boolean verbose) {
		connector = new BotConnector();
		this.verbose = verbose;
	}

	public boolean connect(String withNick, String toServer, long timeout)
			throws BotException {
		final BotClientConfig config = new BotClientConfig();
		config.setNick(withNick);
		config.setServer(toServer);
		config.setVerbose(verbose);

		final FutureResult<Boolean> future = new FutureResult<Boolean>(false);
		connector.addConnectionListener(new ConnectionListener() {

			@Override
			public void onConnect() {
				System.out.println("CONNECTED");
				future.setResult(true);

			}
		});
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					connector.start(config);
					future.setResult(true);
				} catch (BotException e) {
					future.setResult(false);
					try {
						connector.getBot().disconnect();
						connector.getBot().dispose();
					} catch (Exception e2) {
					}
				}

			}
		}).start();

		Boolean result = future.get(timeout);
		if (result == null) {
			connector.getBot().disconnect();
			connector.getBot().dispose();
		}
		return result;
	}

	public InnerBot getBot() {
		return connector.getBot();
	}

	public EventListenerManager getEbus() {
		return connector.getEbus();
	}

}
