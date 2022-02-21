package team.solution.teham.core;

import java.io.IOException;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(value = "/teham")
public class ProcessServerEndpoint {
    
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @OnOpen
    public void onOpen(Session session) {
        logger.info(String.format("Session '%s' Connected", session.getId()));
        new ProcessExecutor(session).start();
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        logger.info(String.format("Session '%s' new message: %s", session.getId(), message));
        if (message != null && (message.equalsIgnoreCase("q") || message.equalsIgnoreCase("exit"))) {
            session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "quit message received"));
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session '%s' closed because of %s", session.getId(), closeReason));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

}
