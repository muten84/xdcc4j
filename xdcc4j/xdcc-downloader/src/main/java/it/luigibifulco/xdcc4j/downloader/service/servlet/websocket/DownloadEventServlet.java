package it.luigibifulco.xdcc4j.downloader.service.servlet.websocket;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@SuppressWarnings("serial")
@WebServlet(name = "MyEcho WebSocket Servlet", urlPatterns = { "/events" })
@Singleton
public class DownloadEventServlet extends WebSocketServlet {

	@Inject
	private WsFactory wsCreator;

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.getPolicy().setIdleTimeout(10000);
		factory.setCreator(wsCreator);
		// factory.register(EventSocket.class);

	}

}
