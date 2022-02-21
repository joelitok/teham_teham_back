package team.solution.teham.core.elements;

import org.json.JSONObject;

import team.solution.teham.core.ProcessExecutor;
import team.solution.teham.core.utils.view.ViewData;

public class TaskView extends MultiTargetElement {

    public TaskView(String id, String name, String source, String target) {
        super(id, name, source, target);
    }

    @Override
    public JSONObject handle(ProcessExecutor processExecutor, JSONObject data) {

        processExecutor.sendViewData(new ViewData(name, data));

        return null;
    }

}
