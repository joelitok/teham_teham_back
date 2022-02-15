package team.solution.teham.core.elements;

import org.json.JSONObject;

public class TaskView extends Task {

    protected TaskView(String id, String name, String source, String target) {
        super(id, name, source, target);
    }

    @Override
    public JSONObject handle(JSONObject data) {
        // sendViewName();

        if (target == null) {
            for (var t: targets) {
                // TehamProcess.getInstance().registerEventListener(t.name, t.id);
            }
        } 
        return null;
    }

}
