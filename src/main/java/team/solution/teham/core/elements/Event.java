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

    public Event(String id, String name, String[] sources, String[] targets, String type) {
        super(id, name, sources, targets);
        if (type.equalsIgnoreCase("Start")) {
            this.type = EventType.START;
            assertExactTargetCount(1);
        } else if (type.equalsIgnoreCase("End")) {
            this.type = EventType.END;
        } else {
            this.type = EventType.VIEW;
            assertExactTargetCount(1);
        }
    }

    @Override
    public JSONObject handle(ProcessExecutor processExecutor, JSONObject data) {
        if (type == EventType.START) {
            return null;
        }

        if (type == EventType.END) {
            Thread.currentThread().interrupt();
        }

        if (type == EventType.VIEW) {
            processExecutor.registerEventListener(name, targets.get(0));
        }

        return data;
    }
    
    @Override
    public boolean hasNext() {
        if (type != EventType.START) {
            return false;
        }
        return super.hasNext();
    }
}
