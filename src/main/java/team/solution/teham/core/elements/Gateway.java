package team.solution.teham.core.elements;

import java.util.List;

import org.json.JSONObject;

import team.solution.teham.core.ProcessExecutor;

public class Gateway extends Element {

    private final List<String> cases;

    private String next;

    public Gateway(String id, String name, String[] sources, String[] targets, String[] cases) {
        super(id, name, sources, targets);
        this.cases = List.of(cases);
        assertExactTargetCount(cases.length);
    }

    @Override
    public JSONObject handle(ProcessExecutor processExecutor, JSONObject data) {
        if (data != null) {
            int statusCode = data.has("status") ? data.getInt("status") : 0;
            JSONObject body = data.has("body") ? data.getJSONObject("body") : null;
            for (int i = 0; i < cases.size(); i++) {
                try {
                    if (Integer.parseInt(cases.get(i)) == statusCode) {
                        this.next = targets.get(i);
                        break;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            return body;
        }
        return null;
    }
    
    @Override
    public String[] getNexts() {
        return new String[]{next};
    }
}
