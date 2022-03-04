package team.solution.teham.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import team.solution.teham.core.exceptions.MalFormatedDocumentException;
import team.solution.teham.core.utils.view.ViewData;
import team.solution.teham.core.utils.xml.DocParserXMLDocImpl;
import team.solution.teham.core.utils.xml.XMLDoc;


public final class ProcessExecutor {

    private ConcurrentMap<String, Set<String>> listeners;

    private Session webSocketSession;

    private static XMLDoc xmlDoc;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public ProcessExecutor(Session webSocketSession) {

        if (xmlDoc == null) {
            throw new ProcessExecutorNotInitializedException();
        }

        this.webSocketSession = webSocketSession;
        listeners = new ConcurrentHashMap<>();
        webSocketSession.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                try {
                    logger.info(String.format("Session '%s' new message: %s", webSocketSession.getId(), message));
                    if (message != null && (message.equalsIgnoreCase("q") || message.equalsIgnoreCase("exit"))) {
                        webSocketSession.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "quit message received"));
                    }
                    var json = new JSONObject(message);
                    onEvent(new ViewData(json));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    public static void initialize(InputStream xmlInputStream) throws ParserConfigurationException, SAXException, IOException {
        xmlDoc = new DocParserXMLDocImpl(xmlInputStream);
    }

    public void start() {
        handle(xmlDoc.getStartEventElement().getId(), null);
    }

    private void handle(String id, JSONObject data) {
        if (webSocketSession.isOpen()) {
            var element = xmlDoc.getElementById(id);

            if (element == null) {
                throw new MalFormatedDocumentException("Element of id '" + id + "'' not found !");
            }

            logger.info("****** ABOUT TO HANDLE " + element.getName());

            var json = element.handle(this, data);

            logger.info("****** HANLDING " + element.getName() + " FINISHED WITH DATA: " + (json != null ? json.toString() : null));
            
            if (element.hasNext()) {
                for (var target: element.getNexts()) {
                    handle(target, json);
                }
            }
        }
    }

    public void registerEventListener(String eventName, String id) {
        if (listeners.containsKey(eventName)) {
            var list = new HashSet<>(listeners.get(eventName));
            list.add(id);
            listeners.replace(eventName, list);
        } else {
            var list = new HashSet<String>();
            list.add(id);
            listeners.put(eventName, list);
        }
    }

    private void onEvent(ViewData event) {
        var eventName = event.getName();
        if (listeners.containsKey(eventName)) {
            var ids = listeners.get(eventName);
            listeners.remove(eventName);
            for (var id: ids) {
                handle(id, event.getData());
            }
        }
    }

    public void sendViewData(ViewData viewData) {
        try {
            webSocketSession.getBasicRemote().sendText(viewData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class ProcessExecutorNotInitializedException extends RuntimeException {
        @Override
        public String getMessage() {
            return "You must first call ProcessExecutor.initialize before any instantiation";
        }
    }
}
