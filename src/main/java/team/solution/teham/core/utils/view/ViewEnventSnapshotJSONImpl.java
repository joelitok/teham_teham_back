package team.solution.teham.core.utils.view;

import org.json.JSONObject;

public class ViewEnventSnapshotJSONImpl implements ViewEnventSnapshot {

    private final String name;

    private final JSONObject data;

    private static final String KEY_NAME = "name";
    private static final String KEY_DATA = "data";

    public ViewEnventSnapshotJSONImpl(JSONObject json) {
        if (json.has(KEY_NAME)) {
            this.name = json.getString(KEY_NAME);
            this.data = json.has(KEY_DATA) ? json.getJSONObject(KEY_DATA) : null;
        } else {
            throw new BadViewSnapshotJsonStructureException();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JSONObject getData() {
        return data;
    }
    
    
    class BadViewSnapshotJsonStructureException extends RuntimeException {
        @Override
        public String getMessage() {
            return "The json view event data must contains mandatory 'name' key and optional 'data' key";
        }
    }
}

