package it.biffi.jirc.test;

import java.io.IOException;

import org.jibble.pircbot.DccChat;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ExampleBot bot = new ExampleBot();

		// Enable debugging output.
		bot.setVerbose(true);

		// Connect to the IRC server.
		try {
			bot.connect("irc.crocmax.net");
		} catch (NickAlreadyInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Join the #pircbot channel.
		bot.joinChannel("#cm-plus");
		// /msg SUN|SERIETV-NEWS|02 xdcc send #921
		bot.sendMessage("CM|SeRiE|TV|08", "xdcc send #74");

	}

}
