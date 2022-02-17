package team.solution.teham.core.exceptions;

public class MalFormatedDocumentException extends RuntimeException {
    
    public MalFormatedDocumentException(Throwable cause) {
        super(cause);
    }

    public MalFormatedDocumentException(String message) {
        super(message);
    }
}
