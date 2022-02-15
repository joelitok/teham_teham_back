package team.solution.teham.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import team.solution.teham.core.exceptions.ProcessNotInitializedException;
import team.solution.teham.core.utils.view.ViewEnventSnapshot;
import team.solution.teham.core.utils.xml.XMLDoc;


final class TehamProcess {

    private static TehamProcess instance;

    private List<JSONObject> data;

    private Iterator<JSONObject> dataIterator;

    private Map<String, List<String>> listeners;

    private XMLDoc xmlDoc;

    private TehamProcess(XMLDoc xmlDoc) {
        this.xmlDoc = xmlDoc;
        data = new ArrayList<>();
        dataIterator = data.iterator();
        listeners = new HashMap<>();
    }

    private static TehamProcess getInstance(XMLDoc xmlDoc) {
        if (instance == null) {
            if (xmlDoc != null) {
                instance = new TehamProcess(xmlDoc);
            } else {
                throw new ProcessNotInitializedException(); 
            }
        }
        return instance;
    }

    public static void init(XMLDoc xmlDoc) {
        getInstance(xmlDoc);
    }

    public static TehamProcess getInstance() {
        return getInstance(null);
    }


    public void handle(String id) {
        var element = xmlDoc.getElementById(id);
        var json = element.handle(dataIterator.hasNext() ? dataIterator.next() : null);
        data.add(json);
        
        if (element.getTarget() != null) {
            handle(element.getTarget());
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

    public void onEvent(ViewEnventSnapshot event) {
        var eventName = event.getName();
        if (listeners.containsKey(eventName)) {
            var ids = listeners.get(eventName);
            for (var id: ids) {
                data.add(event.getData());
                handle(id);
            }
        }
    }
}