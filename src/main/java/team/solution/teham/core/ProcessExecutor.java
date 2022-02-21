package team.solution.teham.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import team.solution.teham.core.elements.MultiTargetElement;
import team.solution.teham.core.exceptions.MalFormatedDocumentException;
import team.solution.teham.core.utils.view.ViewData;
import team.solution.teham.core.utils.xml.DocParserXMLDocImpl;
import team.solution.teham.core.utils.xml.XMLDoc;


public final class ProcessExecutor {

    private List<JSONObject> data;

    private Iterator<JSONObject> dataIterator;

    private Map<String, List<String>> listeners;

    private Session webSocketSession;

    private static XMLDoc xmlDoc;

    public ProcessExecutor(Session webSocketSession) {

        if (xmlDoc == null) {
            throw new ProcessExecutorNotInitializedException();
        }

        this.webSocketSession = webSocketSession;
        data = new ArrayList<>();
        dataIterator = data.iterator();
        listeners = new HashMap<>();
        webSocketSession.addMessageHandler((MessageHandler.Whole<String>) message -> {
            try {
                var json = new JSONObject(message);
                onEvent(new ViewData(json));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public static void initialize(InputStream xmlInputStream) throws ParserConfigurationException, SAXException, IOException {
        xmlDoc = new DocParserXMLDocImpl(xmlInputStream);
    }

    public void start() {
        handle("0");
    }

    private void handle(String id) {
        if (webSocketSession.isOpen()) {
            var element = xmlDoc.getElementById(id);

            if (element == null) {
                throw new MalFormatedDocumentException("Element of id '" + id + "'' not found !");
            }

            var json = element.handle(this, dataIterator.hasNext() ? dataIterator.next() : null);
            data.add(json);
            
            if (element.getTarget() != null) {
                handle(element.getTarget());
            } else if (element instanceof MultiTargetElement) {    // multiple target
                for (var tId: ((MultiTargetElement) element).getTargetsAsIds()) {
                    handle(tId);
                }
            }
        }
    }

    public void registerEventListener(String eventName, String id) {
        if (listeners.containsKey(eventName)) {
            var list = listeners.get(eventName);
            list.add(id);
            listeners.replace(eventName, list);
        } else {
            var list = new ArrayList<String>();
            list.add(id);
            listeners.put(eventName, list);
        }
    }

    private void onEvent(ViewData event) {
        var eventName = event.getName();
        if (listeners.containsKey(eventName)) {
            var ids = listeners.get(eventName);
            for (var id: ids) {
                data.add(event.getData());
                handle(id);
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
