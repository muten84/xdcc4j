package it.luigibifulco.xdcc4j.ui.client.search.event;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;
import it.luigibifulco.xdcc4j.ui.client.Registry;
import it.luigibifulco.xdcc4j.ui.client.search.IrcMuleEntryPoint.DownloadBeanMapper;
import it.luigibifulco.xdcc4j.ui.client.search.view.HeaderUi;
import it.luigibifulco.xdcc4j.ui.client.search.view.SearchUI.DownloadRequestEvent;

import org.realityforge.gwt.websockets.client.WebSocket;
import org.realityforge.gwt.websockets.client.WebSocketListenerAdapter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;

public class XdccWebSocket {

	final WebSocket webSocket = WebSocket.newWebSocketIfSupported();
	private DownloadBeanMapper mapper;
	private HandlerManager ebus;

	public XdccWebSocket(HandlerManager ebus) {
		this.ebus = ebus;
		mapper = GWT.create(DownloadBeanMapper.class);

	}

	public void stop() {
		try {
			webSocket.close();
		} catch (Exception e) {

		}

	}

	public void subscribeDownloadStatus(DownloadBean d) {
		try {
			String json = mapper.write(d);
			webSocket.send(json);
		} catch (Exception e) {

		}
	}

	public void start() {
		try {
			if (webSocket != null) {
				webSocket.setListener(new WebSocketListenerAdapter() {
					@Override
					public void onClose(WebSocket webSocket, boolean wasClean,
							int code, String reason) {
						try {
							webSocket.close();
							super.onClose(webSocket, wasClean, code, reason);
						} catch (Exception e) {

						}
					}

					public void onError(WebSocket webSocket) {
						try {
							webSocket.close();
						} catch (Exception e) {

						}
					};

					public void onMessage(WebSocket webSocket,
							com.google.gwt.typedarrays.shared.ArrayBuffer data) {
					};

					@Override
					public void onMessage(WebSocket webSocket, String data) {
						try {
							String json = data;
							DownloadBean bean = mapper.read(json);
							ebus.fireEvent(new DownloadRequestEvent(bean,
									"update"));
						} catch (Exception e) {

						}
					}

					@Override
					public void onOpen(WebSocket webSocket) {
						try {
							HeaderUi header = Registry.<HeaderUi> get("header");
							// header.alert("Benvenuto nella pagina dei downloads");
							super.onOpen(webSocket);
						} catch (Exception e) {

						}
					}
				});
			}
			String baseURL = GWT.getHostPageBaseURL();
			baseURL.replace("http", "");
			webSocket.connect("ws://127.0.0.1:8888/xdcc4j/events/downloads");// echo"
																				// );
		} catch (Exception e) {

		}
	}
}
