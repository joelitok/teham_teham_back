package team.solution.teham.core.exceptions;


public class ProcessNotInitializedException extends RuntimeException {

    @Override
    public String getMessage() {
        return "You must initialize Process with Process.init(XMLDoc) before use it";
    }
}
