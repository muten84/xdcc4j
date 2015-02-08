package it.biffi.jirc.bot;

import it.biffi.jirc.bot.listener.FileTransferListener;
import it.biffi.jirc.bot.listener.MessageListener;

import org.jibble.pircbot.DccFileTransfer;

public interface IFileTransferBot extends Bot {

	public boolean connect(String withNick, String toServer, long timeout)
			throws BotException;

	public DccFileTransfer requestPacket(long timeout,
			FileTransferListener listener);

	public void addTransferStartListener();

	public void addTranferFinishListener();

	public void addTrasnferProgressListener();
	
	public void sendMessage(String to, String msg, MessageListener listener);
	
	
}
