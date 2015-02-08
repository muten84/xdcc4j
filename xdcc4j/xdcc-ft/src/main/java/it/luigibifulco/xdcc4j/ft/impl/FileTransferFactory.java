package it.luigibifulco.xdcc4j.ft.impl;

import it.biffi.jirc.bot.BotException;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer;

public class FileTransferFactory {

	public static XdccFileTransfer createFileTransfer(XdccRequest request,
			int connectTimeout, int requestTTL) {
		try {
			return new XdccFileTransferImpl(request, connectTimeout, requestTTL);
		} catch (BotException e) {
			return null;
		}
	}

	public static XdccFileTransfer createFileTransfer(XdccRequest request) {
		try {
			return new XdccFileTransferImpl(request, 10000, Long.MAX_VALUE);
		} catch (BotException e) {
			return null;
		}
	}
}
