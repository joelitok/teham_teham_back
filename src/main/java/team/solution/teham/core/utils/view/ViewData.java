package team.solution.teham.core.utils.view;

import org.json.JSONObject;

public class ViewData {

    private final String name;

    private final JSONObject data;

    private static final String KEY_NAME = "name";
    private static final String KEY_DATA = "data";

    public ViewData(JSONObject json) {
        if (json.has(KEY_NAME)) {
            this.name = json.getString(KEY_NAME);
            this.data = json.has(KEY_DATA) ? json.getJSONObject(KEY_DATA) : null;
        } else {
            throw new BadViewDataJsonStructureException();
        }
    }

    public ViewData(String name, JSONObject data) {
        this.name = name;
        this.data = data;
    }

    
    public JSONObject toJSON() {
        var json = new JSONObject();
        json.put(KEY_NAME, name);
        json.put(KEY_DATA, data);
        return json;
    }

    public String getName() {
        return name;
    }

    public JSONObject getData() {
        return data;
    }
    
    public String toString() {
        return toJSON().toString();
    }
    
    class BadViewDataJsonStructureException extends RuntimeException {
        @Override
        public String getMessage() {
            return "The json view data must contains mandatory 'name' key and optional 'data' key";
        }
    }
}

