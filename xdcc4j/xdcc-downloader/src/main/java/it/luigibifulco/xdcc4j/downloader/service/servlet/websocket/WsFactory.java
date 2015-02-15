package it.luigibifulco.xdcc4j.downloader.service.servlet.websocket;

import it.luigibifulco.xdcc4j.downloader.core.XdccDownloader;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import com.google.inject.Inject;

public class WsFactory implements WebSocketCreator {

	@Inject
	private XdccDownloader core;

	private DownloaderEventNotifier socket;

	public WsFactory() {
		this.socket = new DownloaderEventNotifier(core);
	}

	@Override
	public Object createWebSocket(ServletUpgradeRequest req,
			ServletUpgradeResponse resp) {
		// this.socket = new EventSocket(core);
		this.socket.setCore(core);
		return socket;
	}

}
