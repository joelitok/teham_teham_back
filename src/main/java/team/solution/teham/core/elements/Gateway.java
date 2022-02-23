package team.solution.teham.core.elements;

import org.json.JSONObject;

import team.solution.teham.core.ProcessExecutor;

public class Gateway extends MultiTargetElement {

    public Gateway(String id, String name, String source, String target) {
        super(id, name, source, target);
    }

    @Override
    public JSONObject handle(ProcessExecutor processExecutor, JSONObject data) {
        if (data != null) {
            int statusCode = data.has("status") ? data.getInt("status") : 0;
            JSONObject body = data.has("body") ? data.getJSONObject("body") : null;
            for (var t: targets) {
                try {
                    if (Integer.parseInt(t.name) == statusCode) {
                        this.target = t.id;
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
    
}
