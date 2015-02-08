package it.biffi.jirc.bot;

import it.biffi.jirc.bot.event.ChannelInfoEvent;
import it.biffi.jirc.bot.event.ConnectionEvent;
import it.biffi.jirc.bot.event.FileTransferStartEvent;
import it.biffi.jirc.bot.event.FileTrasnferFinishEvent;
import it.biffi.jirc.bot.event.GenericEvent;
import it.biffi.jirc.bot.event.JoinEvent;
import it.biffi.jirc.bot.event.MessageEvent;
import it.biffi.jirc.bot.event.XdccEvent;
import it.biffi.jirc.bot.listener.AbstractListener;
import it.biffi.jirc.bot.listener.EventListenerManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			System.out.println("onConnect");
			ebus.addEvent(new ConnectionEvent());
		}

		@Override
		protected void onJoin(String channel, String sender, String login,
				String hostname) {
			System.out.println("onJoin: " + sender + " - " + login);
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
			// LIST stopped (221 lines deleted)
			System.out.println("onNotice: " + sourceNick + " - " + notice);
			if (notice.contains("LIST") && notice.contains("stopped")) {
				int start = notice.indexOf('(');
				int end = notice.indexOf("lines");
				String lines = notice.substring(start + 1, end).trim();
				XdccEvent event = new XdccEvent();
				event.putData(XdccEvent.XDCC_NOTICE_LINES_TO_READ, lines);
				ebus.addEvent(event);
			} else {
				MessageEvent event = new MessageEvent();
				event.putData(MessageEvent.CHANNEL, "");
				event.putData(MessageEvent.SENDER, sourceNick);
				event.putData(MessageEvent.LOGIN, sourceLogin);
				event.putData(MessageEvent.HOSTNAME, sourceHostname);
				event.putData(MessageEvent.MESSAGE, notice);
				ebus.addEvent(event);
			}
			super.onNotice(sourceNick, sourceLogin, sourceHostname, target,
					notice);
		}

		@Override
		protected void onMode(String channel, String sourceNick,
				String sourceLogin, String sourceHostname, String mode) {
			System.out.println("onMode: " + sourceNick);
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
			System.out.println("onUnknown: " + line);
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
			System.out.println("onVoice: " + sourceNick);
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
			System.out.println("onPart");
			super.onPart(channel, sender, login, hostname);
		}

		@Override
		protected void onInvite(String targetNick, String sourceNick,
				String sourceLogin, String sourceHostname, String channel) {
			System.out.println("onInvite: " + sourceNick);
			super.onInvite(targetNick, sourceNick, sourceLogin, sourceHostname,
					channel);
		}

		@Override
		protected void onUserList(String channel, User[] users) {
			System.out.println("onUserList: " + channel + " - " + users.length);
			ChannelInfoEvent event = new ChannelInfoEvent();
			List<String> userList = new ArrayList<String>();
			for (User user : users) {
				if (user.getPrefix() != null && user.getPrefix().length() > 0) {
					userList.add(user.getNick());
				}
			}
			event.putData(ChannelInfoEvent.USERS, userList);
			ebus.addEvent(event);
			super.onUserList(channel, users);
		}

		@Override
		protected void onServerResponse(int code, String response) {
			System.out.println("onServerResponse: " + code + " - " + response);
			switch (code) {
			case 323:
				ChannelInfoEvent ch_event = new ChannelInfoEvent();
				ch_event.putData(ChannelInfoEvent.STATUS, "FINISHED");
				ebus.addEvent(ch_event);
				break;
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
			System.out.println("onTopic: " + topic);
			super.onTopic(channel, topic, setBy, date, changed);
		}

		@Override
		protected void onAction(String sender, String login, String hostname,
				String target, String action) {
			System.out.println("onAction: " + sender + " - " + target + " - "
					+ action);
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
			System.out.println("onChannelInfo");
			ChannelInfoEvent event = new ChannelInfoEvent();
			event.putData(ChannelInfoEvent.CHANNEL, channel);
			event.putData(ChannelInfoEvent.HOSTNAME, bot.getServer());
			event.putData(ChannelInfoEvent.CHANNEL_TOPIC, topic);
			event.putData(ChannelInfoEvent.CHANNEL_USER_COUNT, "" + userCount);
			ebus.addEvent(event);
			super.onChannelInfo(channel, userCount, topic);
		}

		@Override
		protected void onPrivateMessage(String sender, String login,
				String hostname, String message) {
			System.out.println("onPrivateMessage: " + sender + " - " + message);
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
