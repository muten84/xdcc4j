package it.luigibifulco.xdcc4j.downloader.service.servlet.websocket;

import javax.websocket.ClientEndpoint;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@ClientEndpoint
@ServerEndpoint(value = "/echo")
@WebSocket
public class SimpleWebSocket {
	
	@OnWebSocketMessage
	 public void onText(Session session, String message) {
        if (session.isOpen()) {
            System.out.printf("Echoing back message [%s]%n", message);
            session.getRemote().sendString(message, null);
        }
    }

}
