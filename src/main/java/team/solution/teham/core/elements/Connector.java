package team.solution.teham.core.elements;

import org.json.JSONObject;

import team.solution.teham.core.ProcessExecutor;

public class Connector extends Element {

    public Connector(String id, String name, String[] sources, String[] targets) {
        super(id, name, sources, targets);
        assertExactTargetCount(1);
    }

    @Override
    public JSONObject handle(ProcessExecutor processExecutor, JSONObject data) {
        return data;
    }
    
}
