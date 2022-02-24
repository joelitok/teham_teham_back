package team.solution.teham.core.elements;

import java.util.List;

import org.json.JSONObject;

import team.solution.teham.core.ProcessExecutor;
import team.solution.teham.core.utils.view.ViewData;

public class TaskView extends Element {

    private final List<String> events;

    public TaskView(String id, String name, String[] source, String[] target, String[] events) {
        super(id, name, source, target);
        this.events = List.of(events);
    }

    @Override
    public JSONObject handle(ProcessExecutor processExecutor, JSONObject data) {

        processExecutor.sendViewData(new ViewData(name, data));

        return null;
    }

}
