package team.solution.teham.api.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ProcessInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long theadID;

    private String host;

    private Integer port;

    private String topic;

    private String url;

    public ProcessInfo() {}

    public ProcessInfo(long theadID, String url, String host, int port, String topic) {
        this.theadID = theadID;
        this.url = url;
        this.host = host;
        this.port = port;
        this.topic = topic;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}