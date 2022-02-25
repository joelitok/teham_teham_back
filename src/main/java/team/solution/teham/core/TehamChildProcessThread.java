package team.solution.teham.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.websocket.DeploymentException;
import javax.xml.parsers.ParserConfigurationException;

import org.glassfish.tyrus.server.Server;
import org.xml.sax.SAXException;

public class TehamChildProcessThread extends Thread {

    private Server server;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public TehamChildProcessThread(String host, int port, String topic, InputStream xmlFileIS) throws ParserConfigurationException, SAXException, IOException {
        ProcessExecutor.initialize(xmlFileIS);
        this.server = new Server(host, port, topic, ProcessServerEndpoint.class);
    }

    public void test() throws FailedToStartException {
        try {
            server.start();
            server.stop();
        } catch (Exception e) {
            throw new FailedToStartException(e);
        }
    }

    public synchronized void stopServer() {
        if (server != null) {
            server.stop();
            logger.info("Websocket Server stopped.");
            throw new ServerStoppedException();
        }
    }

    @Override
    public void run() {
        try {
            server.start();
        } catch (DeploymentException e) {
            e.printStackTrace();
            stopServer();
            throw new FailedToStartException("The server failed to start", e);
        }
    }

    public class ServerStoppedException extends RuntimeException {}

    public class FailedToStartException extends RuntimeException {
        
        FailedToStartException(Throwable e) {
            super(e);
        }

        FailedToStartException(String message, Throwable e) {
            super(message, e);
        }
    }
}
