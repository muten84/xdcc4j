package it.biffi.jirc.test;

import java.io.File;
import java.util.UUID;

import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.PircBot;

public class ExampleBot extends PircBot {

	public ExampleBot() {
		String name = UUID.randomUUID().toString();
		this.setName("[Bot" + name.substring(0, 5) + "]");
		this.setAutoNickChange(true);
	}

	@Override
	protected void onConnect() {
		System.out.println("CONNECTED");
		super.onConnect();
	}

	@Override
	protected void onIncomingFileTransfer(final DccFileTransfer transfer) {
		super.onIncomingFileTransfer(transfer);
		System.out.println(transfer.isIncoming());
		transfer.receive(new File("transfer"), false);
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					double perc = transfer.getProgressPercentage();
					System.out.println(">>>>>Trasferimento File al " + perc
							+ "%");
					if (perc >= 100) {
						break;
					}
				}
			}
		}).start();
	}

	@Override
	protected void onJoin(String channel, String sender, String login,
			String hostname) {
		System.out.println("onjoin: " + channel + " - " + sender + " - "
				+ login + " - " + hostname);
		super.onJoin(channel, sender, login, hostname);
	}

	@Override
	protected void onPrivateMessage(String sender, String login,
			String hostname, String message) {
		System.out.println(">>>>>>>>>>>>>>>>>>onPrivateMessage: " + sender
				+ " - " + login + " - " + message);
		super.onPrivateMessage(sender, login, hostname, message);
	}

	@Override
	protected void onFileTransferFinished(DccFileTransfer transfer, Exception e) {
		if (e != null) {
			e.printStackTrace();
		}
		System.out.println(">>>>>>>>>>>>>>>>>>onFileTransferFinished: ");
		super.onFileTransferFinished(transfer, e);
	}

}
