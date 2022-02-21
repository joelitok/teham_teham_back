package team.solution.teham.core.elements;

import org.json.JSONObject;

import team.solution.teham.core.ProcessExecutor;

public class Connector extends Element {

    public Connector(String id, String name, String source, String target) {
        super(id, name, source, target);
    }

    @Override
    public JSONObject handle(ProcessExecutor processExecutor, JSONObject data) {
        return data;
    }
    
}
