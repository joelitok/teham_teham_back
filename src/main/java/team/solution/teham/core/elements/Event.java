package team.solution.teham.core.elements;

import org.json.JSONObject;

import team.solution.teham.core.ProcessExecutor;

enum EventType { 
    START,
    VIEW,
    END
}

public class Event extends Element {

    private final EventType type;

    public Event(String id, String name, String source, String target, String type) {
       super(id, name, source, target);
        if (type.equalsIgnoreCase("Start")) {
            this.type = EventType.START;
        } else if (type.equalsIgnoreCase("End")) {
            this.type = EventType.END;
        } else {
            this.type = EventType.VIEW;
        }
    }

    @Override
    public JSONObject handle(JSONObject data) {
        if (type == EventType.START) {
            return null;
        }

        if (type == EventType.END) {
            target = null;
            Thread.currentThread().interrupt();
        }

        if (type == EventType.VIEW) {
            ProcessExecutor.getInstance().registerEventListener(name, target);
            target = null;
        }

        return data;
    }
    
}
