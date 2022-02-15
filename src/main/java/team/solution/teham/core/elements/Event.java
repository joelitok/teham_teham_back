package team.solution.teham.core.elements;

import org.json.JSONObject;

import team.solution.teham.core.exceptions.MissingViewEventSnapshotException;
import team.solution.teham.core.utils.view.ViewEnventSnapshot;

enum EventType { 
    START,
    VIEW, 
    END
};

public class Event extends Element {

    private final EventType type;

    private final ViewEnventSnapshot viewEnventSnapshot;

    public Event(String id, String name, String source, String target, EventType type, ViewEnventSnapshot event) {
        super(id, name, source, target);
        this.type = type;
        this.viewEnventSnapshot = event;
    }

    public Event(String id, String name, String source, String target, EventType type) {
        this(id, name, source, target, type, null);
    }

    @Override
    public JSONObject handle(JSONObject data) {
        if (type == EventType.END) {
            Thread.currentThread().interrupt();
        }

        if (type == EventType.VIEW) {
            if (viewEnventSnapshot == null) {
                throw new MissingViewEventSnapshotException();
            }

            return viewEnventSnapshot.getData();
        }

        return data;
    }
    
}
