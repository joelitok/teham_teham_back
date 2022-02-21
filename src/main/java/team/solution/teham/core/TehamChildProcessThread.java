package team.solution.teham.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.glassfish.tyrus.server.Server;
import org.xml.sax.SAXException;

public class TehamChildProcessThread extends Thread {

    private Server server;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public TehamChildProcessThread(int port, InputStream xmlFileIS) throws ParserConfigurationException, SAXException, IOException {
        ProcessExecutor.initialize(xmlFileIS);
        this.server = new Server("localhost", port, "/", ProcessServerEndpoint.class);
    }

    public synchronized void stopServer() {
        if (server != null) {
            server.stop();
            server = null;
            logger.info("Websocket Server stopped.");
        }
    }

    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ) {
            server.start();
            logger.info("Please press a key to stop the server.");
            var s = reader.readLine();
            logger.log(Level.INFO, "User hint: {0} ", s);
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("The server failed to start");
        } finally {
            stopServer();
        }

    }
    
}
