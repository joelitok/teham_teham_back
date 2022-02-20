package team.solution.teham.api.models;

class ProcessInfo {

    private final Integer pid;

    private final Integer port;

    public ProcessInfo(int pid, int port) {
        this.pid = pid;
        this.port = port;
    }

    public Integer getPid() {
        return pid;
    }

    public Integer getPort() {
        return port;
    }

}