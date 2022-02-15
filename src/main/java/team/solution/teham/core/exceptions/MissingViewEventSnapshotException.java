package team.solution.teham.core.exceptions;

public class MissingViewEventSnapshotException extends RuntimeException {
    
    @Override
    public String getMessage() {
        return "ViewEventSnapshot must not be null when Event type is VIEW";
    }

}
