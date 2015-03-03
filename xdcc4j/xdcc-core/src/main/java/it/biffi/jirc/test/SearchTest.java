package it.biffi.jirc.test;

import it.biffi.jirc.bot.BotClientConfig;
import it.biffi.jirc.bot.BotException;
import it.biffi.jirc.bot.SearchBot;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SearchTest {

	public static void main(String[] args) throws BotException,
			InterruptedException, ExecutionException {
		SearchBot bot = new SearchBot(false);
		BotClientConfig config = new BotClientConfig();
		config.setServer("irc.uragano.org");
		config.setNick("xdccBot" + UUID.randomUUID().toString().substring(0, 6));
		bot.start(config);
		// System.out.println(bot.listChannels());
		// System.out.println(bot.listUsersInChannel("#SUNSHINE"));
		// System.out.println(">>>>>>>>>>>>>>LIST IS:"
		// + bot.scanUser("SUN|VARIE|06", "#SUNSHINE", 60000));

		int lines = bot.getUserListLines("SUN|VARIE|06", "#SUNSHINE");
		List<String> resources = bot.scanUser("SUN|VARIE|06", "#SUNSHINE",
				lines);
		for (String string : resources) {
			System.out.println("R: " + string);
		}

	}
}
