package team.solution.teham.core.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MultiTargetElement extends Element {
    
    protected List<Target> targets;

    protected MultiTargetElement(String id, String name, String source, String target) {
        super(id, name, source, target);
    }

    public MultiTargetElement addTarget(String id, String name) {
        if (targets == null) {
            target = null;
            targets = new ArrayList<>();
        }

        targets.add(new Target(id, name));

        return this;
    }

    public List<String> getTargetsAsIds() {
        return targets.stream().map(t -> t.id).collect(Collectors.toList());
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
