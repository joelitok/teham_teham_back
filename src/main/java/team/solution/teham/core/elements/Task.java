package team.solution.teham.core.elements;

import java.util.ArrayList;
import java.util.List;

public abstract class Task extends Element {
    
    protected List<Target> targets;

    protected Task(String id, String name, String source, String target) {
        super(id, name, source, target);
    }

    public Task addTarget(String id, String name) {
        if (targets == null) {
            target = null;
            targets = new ArrayList<>();
        }

        targets.add(new Target(id, name));

        return this;
    }

    protected class Target {
        
        final String id;

        final String name;

        Target(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

}
