package team.solution.teham.core.elements;

import org.json.JSONObject;

public class Connector extends Element {

    public Connector(String id, String name, String source, String target) {
        super(id, name, source, target);
    }

    @Override
    public JSONObject handle(JSONObject data) {
        return data;
    }
    
}
