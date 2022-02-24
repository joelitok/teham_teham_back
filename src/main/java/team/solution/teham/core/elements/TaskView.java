package team.solution.teham.core.elements;

import org.json.JSONObject;

import team.solution.teham.core.ProcessExecutor;
import team.solution.teham.core.utils.view.ViewData;

public class TaskView extends Element {

    public TaskView(String id, String name, String[] source, String[] target) {
        super(id, name, source, target);
        assertMoreOrEqualThanTargetCount(1);
    }

    @Override
    public JSONObject handle(ProcessExecutor processExecutor, JSONObject data) {

        processExecutor.sendViewData(new ViewData(name, data));

        return null;
    }

}
