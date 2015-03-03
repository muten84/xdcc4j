package it.luigibifulco.xdcc4j.downloader.service.servlet.websocket;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;
import it.luigibifulco.xdcc4j.downloader.core.XdccDownloader;
import it.luigibifulco.xdcc4j.downloader.core.XdccDownloader.DownloadListener;
import it.luigibifulco.xdcc4j.downloader.core.model.Download;
import it.luigibifulco.xdcc4j.downloader.core.util.ConvertUtil;

import java.io.IOException;
import java.util.Collection;

import javax.websocket.ClientEndpoint;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@ClientEndpoint
@ServerEndpoint(value = "/socket")
@WebSocket
public class DownloaderEventNotifier {

	private XdccDownloader core;

	private ObjectMapper mapper = new ObjectMapper();

	public DownloaderEventNotifier() {

	}

	public DownloaderEventNotifier(XdccDownloader core) {
		this.core = core;
	}

	@SuppressWarnings("unused")
	private Session session;

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		System.out.printf("Connection closed: %d - %s%n", statusCode, reason);

	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		System.out.printf("Got connect: %s%n", session);
		System.out.println("you can now receive notification from --->: "
				+ this.core);
		this.session = session;
		Collection<Download> downloads = core.getAllDownloads();
		if (downloads != null) {
			for (Download download : downloads) {
				addDownloadListener(ConvertUtil.convert(download));
			}
		}
	}

	@OnWebSocketMessage
	public void onMessage(String msg) {
		System.out.printf("Got msg: %s%n", msg);
		try {
			DownloadBean d = mapper.reader(DownloadBean.class).readValue(msg);
			if (d != null) {
				addDownloadListener(d);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public XdccDownloader getCore() {
		return core;
	}

	public void setCore(XdccDownloader core) {
		this.core = core;
	}

	private void addDownloadListener(DownloadBean d) {
		core.addDownloadStatusListener(d.getId(), new DownloadListener() {

			@Override
			public void onDownloadStausUpdate(String id, String updateMessage,
					int percentage, int rate) {
				String json;
				try {

					json = mapper.writer().writeValueAsString(d);
					session.getRemote().sendString(json);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	
}
