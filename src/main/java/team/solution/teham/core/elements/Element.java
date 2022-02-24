package team.solution.teham.core.elements;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import team.solution.teham.core.ProcessExecutor;

public abstract class Element {
    
    protected final String id;

    protected final List<String> sources;

    protected final String name;
    
    protected final List<String> targets;

    protected Element(String id, String name, String[] sources, String[] targets) {

        if (id == null || name == null || sources == null || sources.length == 0) {
            throw new NullPointerException("id, name and source must not be null or empty");
        }

        this.id = id;
        this.name = name;
        this.sources = List.of(sources);
        this.targets = targets != null ? Arrays.asList(targets) : null;
    }

    public String getId() {
        return id;
    }

    public List<String> getSources() {
        return sources;
    }

    public List<String> getTargets() {
        return targets;
    }

    public String getName() {
        return name;
    }

    public abstract JSONObject handle(ProcessExecutor processExecutor, JSONObject data);

    public boolean hasNext() {
        return targets != null && !targets.isEmpty();
    }

    /**
     * Get array of next elemets ids
     * @return String[] id of next elements
     */
    public String[] getNexts() {
        var empty = new String[]{};
        return hasNext() ? targets.toArray(empty) : empty;
    }


    private void checkTargetCount(int count, int sign) {
        if (count == 0 && targets == null) {
            return;
        }

        if (
            (sign == 0 && targets.size() != count) || 
            (sign < 0 && targets.size() <= count) ||
            (sign > 0 && targets.size() >= count)
        ) {
            throw new IllegalArgumentException("Invalid targets count for element " + getClass().getSimpleName());
        }
    }

    protected void assertExactTargetCount(int count) {
        checkTargetCount(count, 0);
    }

    protected void assertMoreOrEqualThanTargetCount(int count) {
        checkTargetCount(count, 1);
    }

    protected void assertLessOrEqualThanTargetCount(int count) {
        checkTargetCount(count, -1);
    }

}
