package team.solution.teham.core.utils.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;

import org.glassfish.tyrus.server.Server;
import org.json.JSONException;
import org.json.JSONObject;

@ServerEndpoint(value = "/teham")
public class ProcessServerEndpoint {
    
    private static Logger logger = Logger.getLogger(ProcessServerEndpoint.class.getName());

    @OnOpen
    public void onOpen(Session session) {
        logger.info(String.format("Session '%s' Connected", session.getId()));
        // Get session and WebSocket connection
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        // Handle new messages
        logger.info(String.format("Session '%s' new message: %s", session.getId(), message));
        try {
            var json = new JSONObject(message);
        } catch (JSONException e) {
            if (message != null && (message.equalsIgnoreCase("q") || message.equalsIgnoreCase("exit"))) {
                session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "quit message received"));
            }
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        // WebSocket connection closes
        logger.info(String.format("Session '%s' closed because of %s", session.getId(), closeReason));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    public static void runServer(int port) {

        Server server = new Server("localhost", port, "/", ProcessServerEndpoint.class);

        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ) {
            server.start();
            logger.info("Please press a key to stop the server.");
            reader.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }

    }
}
