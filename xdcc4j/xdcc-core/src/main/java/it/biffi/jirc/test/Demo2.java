package it.biffi.jirc.test;

import java.io.File;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.jibble.pircbot.DccFileTransfer;

import it.biffi.jirc.bot.BotClientConfig;
import it.biffi.jirc.bot.BotConnector;
import it.biffi.jirc.bot.BotException;
import it.biffi.jirc.bot.FileTransferBot;
import it.biffi.jirc.bot.FileTransferConfig;
import it.biffi.jirc.bot.IBotConnector;
import it.biffi.jirc.bot.event.MessageEvent;
import it.biffi.jirc.bot.listener.ConnectionListener;
import it.biffi.jirc.bot.listener.FileTransferListener;
import it.biffi.jirc.bot.listener.MessageListener;

public class Demo2 {

	public static void main(String[] args) throws BotException {

		FileTransferBot bot = new FileTransferBot(false);
		String name = UUID.randomUUID().toString();
		// xdcc send #38
		boolean connected = bot.connect("[Bot" + name.substring(0, 5) + "]",
				"irc.uragano.org", 30000);
		System.out.println("connected: " + connected);
		FileTransferConfig config = new FileTransferConfig();
		config.setSourceChannel("puffolandia");
		config.setSourcePeer("PuFFo|0|ReQuEsT-01");
		config.setInputCommand("xdcc send #38");
		bot.addEventListener(new MessageEvent(), new MessageListener() {

			@Override
			public void onMessage(Map<String, String> data) {
				System.out.println(">>>> on message: " + data);

			}
		});
		bot.start(config);
		System.out.println("bot started on requested channel: "
				+ config.getSourceChannel());
		System.out.println("REQUESTING PACKET");

		final DccFileTransfer transfer = bot.requestPacket(60000,
				new FileTransferListener() {

					@Override
					public void onFinish(Exception finishedWithThisError) {
						if (finishedWithThisError == null) {
							System.out.println("FINISH TRANSFER SUCCESS");
						} else {
							System.out.println("FINISH TRANSFER ERROR");
							finishedWithThisError.printStackTrace();
						}

					}

					@Override
					public void onPreStartUpdate(String data) {
						System.out.println("Received Message: " + data);
					}
				});

		if (transfer != null) {
			File file = new File("/Users/Luigi/Downloads/irc/"
					+ transfer.getFile().getName());
			System.out.println("Receiveing file: " + file.getAbsolutePath());
			System.out.println("Receiveing file: " + file.getAbsolutePath());
			transfer.receive(file, true);

			Timer t = new Timer("PollTrasnferState", true);
			t.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					int perc = (int) transfer.getProgressPercentage();
					int rate = (int) transfer.getTransferRate();
					System.out.println("Trasnfer state: " + perc + "%"
							+ "rate: " + rate);
				}
			}, 0, 5000);
		} else {
			System.out.println("Request packet failed");
		}
	}
}
