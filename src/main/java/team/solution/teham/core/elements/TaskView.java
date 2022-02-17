package team.solution.teham.core.elements;

import org.json.JSONObject;

import team.solution.teham.core.ThreadProcess;

public class TaskView extends MultiTargetElement {

    public TaskView(String id, String name, String source, String target) {
        super(id, name, source, target);
    }

    @Override
    public JSONObject handle(JSONObject data) {

        if (target == null) {
            for (var t: targets) {
                ThreadProcess.getInstance().registerEventListener(t.name, t.id);
            }
        } 

        sendDisplayViewMessage(data);

        return null;
    }


    private void sendDisplayViewMessage(JSONObject data) {
        // sendViewName(this.name, JSONObject data);
    }

}
