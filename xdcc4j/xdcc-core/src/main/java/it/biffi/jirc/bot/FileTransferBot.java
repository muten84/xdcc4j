package it.biffi.jirc.bot;

import java.util.Map;

import javax.print.attribute.standard.Finishings;

import org.jibble.pircbot.DccFileTransfer;

import it.biffi.jirc.bot.event.FileTransferStartEvent;
import it.biffi.jirc.bot.event.FileTrasnferFinishEvent;
import it.biffi.jirc.bot.event.GenericEvent;
import it.biffi.jirc.bot.event.JoinEvent;
import it.biffi.jirc.bot.event.MessageEvent;
import it.biffi.jirc.bot.listener.AbstractListener;
import it.biffi.jirc.bot.listener.ConnectionListener;
import it.biffi.jirc.bot.listener.FileTransferListener;
import it.biffi.jirc.bot.listener.FileTransferStartListener;
import it.biffi.jirc.bot.listener.JoinListener;
import it.biffi.jirc.bot.listener.MessageListener;
import it.biffi.jirc.bot.util.FutureResult;

public class FileTransferBot implements IFileTransferBot {

	protected SynchBotConnector connector;

	private FileTransferConfig config;

	private JoinKey joinKey;

	public FileTransferBot(boolean verbose) {
		connector = new SynchBotConnector(verbose);
	}

	@Override
	public void addTransferStartListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTranferFinishListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTrasnferProgressListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(BotConfig config) throws BotException {
		this.config = (FileTransferConfig) config;
		joinKey.setChannel("#" + this.config.getSourceChannel().toUpperCase());
		final FutureResult<Boolean> result = new FutureResult<Boolean>(false);
		JoinEvent event = new JoinEvent();
		JoinListener listener = new JoinListener() {

			@Override
			public void onJoin(Map<String, String> data) {
				JoinKey maatchKey = new JoinKey(data.get(JoinEvent.SENDER),
						data.get(JoinEvent.CHANNEL));
				if (maatchKey.equals(joinKey)) {
					System.out.println("JOINED: " + joinKey);
					result.setResult(true);
				}

			}
		};
		addEventListener(event, listener);
		connector.getBot().joinChannel("#" + this.config.getSourceChannel());
		result.get(60000);
	}

	@Override
	public DccFileTransfer requestPacket(long timeout,
			final FileTransferListener ftListener) {
		final FutureResult<DccFileTransfer> future = new FutureResult<DccFileTransfer>(
				null);
		MessageListener listener = new MessageListener() {

			@Override
			public void onMessage(Map<String, String> data) {
				String serverResponse = data
						.get(MessageEvent.SERVER_RESPONSE_TYPE);
				if (serverResponse != null) {
					if (serverResponse.equals(MessageEvent._401)) {
						ftListener.onFinish(new RuntimeException(data
								.get(MessageEvent.MESSAGE)));
						future.setResult(null);
					} else {
						ftListener.onPreStartUpdate(data
								.get(MessageEvent.MESSAGE));
					}

				} else if (data.get(MessageEvent.SENDER).equalsIgnoreCase(
						config.getSourcePeer())) {
					System.out
							.println("RECEIVED MESSAGE FROM REQUESTED SOURCE PEER: "
									+ data.get(MessageEvent.MESSAGE));
					ftListener.onPreStartUpdate(data.get(MessageEvent.MESSAGE));
				}
			}
		};
		// remove prevoious listeners...
		removeListeners();
		addEventListener(new MessageEvent(), listener);

		addEventListener(new FileTransferStartEvent(),
				new FileTransferStartListener() {

					@Override
					public void onTransfer(DccFileTransfer transfer) {
						// System.out.println("onTransfer");
						// System.out.println(transfer.getNick());
						// System.out.println(transfer.getFile().getName());
						// System.out.println(transfer.getLogin());
						if (transfer.getNick().equalsIgnoreCase(
								config.getSourcePeer()))
							;
						future.setResult(transfer);

					}
				});
		addEventListener(new FileTrasnferFinishEvent(), ftListener);
		sendMessage(this.config.getSourcePeer(), this.config.getInputCommand());

		return future.get(timeout);

	}

	@Override
	public void stop() {
		try {
			connector.getBot().disconnect();
			connector.getBot().dispose();
		} catch (Exception e) {

		}
		connector.getEbus().removeAll();
		connector = null;
		joinKey = null;
		config = null;
	}

	@Override
	public void addEventListener(GenericEvent event,
			AbstractListener<?> listener) {
		connector.getEbus().addEventListener(event, listener);

	}

	private void removeListeners() {
		connector.getEbus().removeAll();

	}

	@Override
	public boolean connect(String withNick, String toServer, long timeout)
			throws BotException {
		boolean connected = connector.connect(withNick, toServer, timeout);
		if (connected) {
			joinKey = new JoinKey(withNick, null);
		}
		return connected;

	}

	private class JoinKey {
		private String sender;

		private String channel;

		public JoinKey(String sender, String channel) {
			this.channel = channel;
			this.sender = sender;
		}

		public String getSender() {
			return sender;
		}

		public void setSender(String sender) {
			this.sender = sender;
		}

		public String getChannel() {
			return channel;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			JoinKey other = (JoinKey) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (channel == null) {
				if (other.channel != null)
					return false;
			} else if (!channel.equalsIgnoreCase(other.channel))
				return false;
			if (sender == null) {
				if (other.sender != null)
					return false;
			} else if (!sender.equalsIgnoreCase(other.sender))
				return false;
			return true;
		}

		private FileTransferBot getOuterType() {
			return FileTransferBot.this;
		}

		@Override
		public String toString() {
			return "JoinKey [sender=" + sender + ", channel=" + channel + "]";
		}

	}

	@Override
	public void sendMessage(String to, String msg, MessageListener listener) {
		addEventListener(new MessageEvent(), listener);
		sendMessage(to, msg);

	}

	@Override
	public void sendMessage(String to, String msg) {
		connector.getBot().sendMessage(to, msg);

	}

}
