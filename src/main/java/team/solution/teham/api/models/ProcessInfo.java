package team.solution.teham.api.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ProcessInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long theadID;

    private Integer port;

    public ProcessInfo(long theadID, int port) {
        this.theadID = theadID;
        this.port = port;
    }
 

    public void setId(Long id) {
        this.id = id;
    }

    public void setTheadID(Long theadID) {
        this.theadID = theadID;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Long getId() {
        return id;
    }

    public Long getTheadID() {
        return theadID;
    }

    public Integer getPort() {
        return port;
    }

}