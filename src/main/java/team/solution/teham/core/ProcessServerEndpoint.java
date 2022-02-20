package team.solution.teham.core;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import team.solution.teham.core.utils.view.ViewEnventSnapshotJSONImpl;

@ServerEndpoint(value = "/teham/{key}")
public class ProcessServerEndpoint {
    
    Map<String, Session> sessions = new ConcurrentHashMap<>();

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @OnOpen
    public void onOpen(Session session, @PathParam("key") String key) {
        logger.info(String.format("Session '%s' Connected", session.getId()));
        sessions.put(session.getId(), session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        logger.info(String.format("Session '%s' new message: %s", session.getId(), message));
        try {
            var json = new JSONObject(message);
            ProcessExecutor.getInstance().onEvent(new ViewEnventSnapshotJSONImpl(json));
        } catch (JSONException e) {
            if (message != null && (message.equalsIgnoreCase("q") || message.equalsIgnoreCase("exit"))) {
                session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "quit message received"));
            }
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session '%s' closed because of %s", session.getId(), closeReason));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    public void sendMessage() throws IOException {
        sessions.get("")
            .getBasicRemote() // see also getAsyncRemote()
            .sendText("Message you want to send");
    }
}
