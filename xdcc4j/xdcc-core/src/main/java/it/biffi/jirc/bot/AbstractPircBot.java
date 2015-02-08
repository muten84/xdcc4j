package it.biffi.jirc.bot;

import it.biffi.jirc.bot.event.ConnectionEvent;
import it.biffi.jirc.bot.event.FileTransferStartEvent;
import it.biffi.jirc.bot.event.FileTrasnferFinishEvent;
import it.biffi.jirc.bot.event.GenericEvent;
import it.biffi.jirc.bot.event.JoinEvent;
import it.biffi.jirc.bot.event.MessageEvent;
import it.biffi.jirc.bot.listener.AbstractListener;
import it.biffi.jirc.bot.listener.EventListenerManager;

import java.io.IOException;

import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

public class AbstractPircBot implements Bot {

	private InnerBot bot;

	private EventListenerManager ebus;

	private BotClientConfig config;

	public AbstractPircBot() {
		ebus = new EventListenerManager();
	}

	@Override
	public synchronized void start(BotConfig config) throws BotException {

		this.config = (BotClientConfig) config;
		if (bot == null) {
			bot = new InnerBot();
		}
		bot.setAutoNickChange(true);
		bot.setVerbose(this.config.isVerbose());

		// Connect to the IRC server.
		try {
			bot.connect(this.config.getServer());
		} catch (NickAlreadyInUseException e) {
			throw new BotException(e.getMessage(), e);
		} catch (IOException e) {
			throw new BotException(e.getMessage(), e);
		} catch (IrcException e) {
			throw new BotException(e.getMessage(), e);
		}

	}

	@Override
	public synchronized void stop() {
		// TODO: clean ebus
		bot.disconnect();
		ebus.removeAll();

	}

	protected final class InnerBot extends PircBot {

		public InnerBot() {
			this.setName(AbstractPircBot.this.config.getNick());
			this.setAutoNickChange(true);
		}

		@Override
		protected void onConnect() {
			ebus.addEvent(new ConnectionEvent());
		}

		@Override
		protected void onJoin(String channel, String sender, String login,
				String hostname) {
			JoinEvent event = new JoinEvent();
			event.putData(JoinEvent.CHANNEL, channel);
			event.putData(JoinEvent.SENDER, sender);
			event.putData(JoinEvent.LOGIN, login);
			event.putData(JoinEvent.HOSTNAME, hostname);
			ebus.addEvent(event);
			super.onJoin(channel, sender, login, hostname);
		}

		@Override
		protected void onIncomingFileTransfer(DccFileTransfer transfer) {
			System.out.println("<<<<<<onIncomingFileTransfer>>>>>>");
			FileTransferStartEvent event = new FileTransferStartEvent();
			ebus.addEvent(event, transfer);
			super.onIncomingFileTransfer(transfer);
		}

		@Override
		protected void onMessage(String channel, String sender, String login,
				String hostname, String message) {
			System.out.println("onMessage: " + sender + " - " + message);
			MessageEvent event = new MessageEvent();
			event.putData(MessageEvent.CHANNEL, channel);
			event.putData(MessageEvent.SENDER, sender);
			event.putData(MessageEvent.LOGIN, login);
			event.putData(MessageEvent.HOSTNAME, hostname);
			event.putData(MessageEvent.MESSAGE, message);
			ebus.addEvent(event);
			super.onMessage(channel, sender, login, hostname, message);
		}

		@Override
		protected void onNotice(String sourceNick, String sourceLogin,
				String sourceHostname, String target, String notice) {
			// MessageEvent event = new MessageEvent();
			// event.putData(MessageEvent.CHANNEL, "");
			// event.putData(MessageEvent.SENDER, sourceNick);
			// event.putData(MessageEvent.LOGIN, sourceLogin);
			// event.putData(MessageEvent.HOSTNAME, sourceHostname);
			// event.putData(MessageEvent.MESSAGE, notice);
			// ebus.addEvent(event);
			super.onNotice(sourceNick, sourceLogin, sourceHostname, target,
					notice);
		}

		@Override
		protected void onMode(String channel, String sourceNick,
				String sourceLogin, String sourceHostname, String mode) {
			// MessageEvent event = new MessageEvent();
			// event.putData(MessageEvent.CHANNEL, channel);
			// event.putData(MessageEvent.SENDER, sourceNick);
			// event.putData(MessageEvent.LOGIN, sourceLogin);
			// event.putData(MessageEvent.HOSTNAME, sourceHostname);
			// event.putData(MessageEvent.MESSAGE, mode);
			// ebus.addEvent(event);
			super.onMode(channel, sourceNick, sourceLogin, sourceHostname, mode);
		}

		@Override
		protected void onUnknown(String line) {
			// MessageEvent event = new MessageEvent();
			// event.putData(MessageEvent.CHANNEL, "");
			// event.putData(MessageEvent.SENDER, "");
			// event.putData(MessageEvent.LOGIN, "");
			// event.putData(MessageEvent.HOSTNAME, "");
			// event.putData(MessageEvent.MESSAGE, line);
			// ebus.addEvent(event);
			super.onUnknown(line);
		}

		@Override
		protected void onVoice(String channel, String sourceNick,
				String sourceLogin, String sourceHostname, String recipient) {
			// MessageEvent event = new MessageEvent();
			// event.putData(MessageEvent.CHANNEL, channel);
			// event.putData(MessageEvent.SENDER, sourceNick);
			// event.putData(MessageEvent.LOGIN, sourceLogin);
			// event.putData(MessageEvent.HOSTNAME, sourceHostname);
			// event.putData(MessageEvent.MESSAGE, recipient);
			// ebus.addEvent(event);
			super.onVoice(channel, sourceNick, sourceLogin, sourceHostname,
					recipient);
		}

		@Override
		protected void onPart(String channel, String sender, String login,
				String hostname) {
			// TODO Auto-generated method stub
			super.onPart(channel, sender, login, hostname);
		}

		@Override
		protected void onInvite(String targetNick, String sourceNick,
				String sourceLogin, String sourceHostname, String channel) {
			// TODO Auto-generated method stub
			super.onInvite(targetNick, sourceNick, sourceLogin, sourceHostname,
					channel);
		}

		@Override
		protected void onUserList(String channel, User[] users) {
			// TODO Auto-generated method stub
			super.onUserList(channel, users);
		}

		@Override
		protected void onServerResponse(int code, String response) {
			switch (code) {
			case 401:
				MessageEvent event = new MessageEvent();
				event.putData(MessageEvent.CHANNEL, "Global");
				event.putData(MessageEvent.SENDER, "Global");
				event.putData(MessageEvent.LOGIN, getLogin());
				event.putData(MessageEvent.HOSTNAME, getServer());
				event.putData(MessageEvent.MESSAGE, response);
				event.putData(MessageEvent.SERVER_RESPONSE_TYPE, "" + code);
				ebus.addEvent(event);
				break;

			default:
				break;
			}
		}

		@Override
		protected void onTopic(String channel, String topic, String setBy,
				long date, boolean changed) {
			// TODO Auto-generated method stub
			super.onTopic(channel, topic, setBy, date, changed);
		}

		@Override
		protected void onAction(String sender, String login, String hostname,
				String target, String action) {
			// MessageEvent event = new MessageEvent();
			// event.putData(MessageEvent.CHANNEL, "");
			// event.putData(MessageEvent.SENDER, sender);
			// event.putData(MessageEvent.LOGIN, login);
			// event.putData(MessageEvent.HOSTNAME, hostname);
			// event.putData(MessageEvent.MESSAGE, action + "-" + target);
			// ebus.addEvent(event);
			super.onAction(sender, login, hostname, target, action);

		}

		@Override
		protected void onChannelInfo(String channel, int userCount, String topic) {
			MessageEvent event = new MessageEvent();
			event.putData(MessageEvent.CHANNEL, channel);
			event.putData(MessageEvent.SENDER, channel);
			event.putData(MessageEvent.LOGIN, channel);
			event.putData(MessageEvent.HOSTNAME, "");
			event.putData(MessageEvent.MESSAGE, topic);
			ebus.addEvent(event);
			super.onChannelInfo(channel, userCount, topic);
		}

		@Override
		protected void onPrivateMessage(String sender, String login,
				String hostname, String message) {
			MessageEvent event = new MessageEvent();
			event.putData(MessageEvent.SENDER, sender);
			event.putData(MessageEvent.LOGIN, login);
			event.putData(MessageEvent.HOSTNAME, hostname);
			event.putData(MessageEvent.MESSAGE, message);
			ebus.addEvent(event);
			super.onPrivateMessage(sender, login, hostname, message);
		}

		@Override
		protected void onFileTransferFinished(DccFileTransfer transfer,
				Exception e) {
			System.out.println("<<<<<<onFileTransferFinished>>>>>>");
			FileTrasnferFinishEvent event = new FileTrasnferFinishEvent();
			ebus.addEvent(event, e);
			super.onFileTransferFinished(transfer, e);
		}
	}

	public InnerBot getBot() {
		return bot;
	}

	@Override
	public void addEventListener(GenericEvent event,
			AbstractListener<?> listener) {
		ebus.addEventListener(event, listener);

	}

	public EventListenerManager getEbus() {
		return ebus;
	}

	@Override
	public void sendMessage(String to, String msg) {
		bot.sendMessage(to, msg);

	}

}
