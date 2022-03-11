package team.solution.teham.core.elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;

import team.solution.teham.core.ProcessExecutor;

public class TaskApi extends Element {

    private String uri;
    
    private String method;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public TaskApi(
        String id, 
        String name, 
        String[] source, 
        String[] target,

        String uri,
        String method
    ) {
        super(id, name, source, target);
        this.uri = uri;
        this.method = method != null ? method : "GET";
        assertExactTargetCount(1);
    }

    public TaskApi(
        String id, 
        String name, 
        String[] source, 
        String[] target,

        String uri
    ) {
        this(id, name, source, target, uri, null);
    }

    @Override
    public JSONObject handle(ProcessExecutor processExecutor, JSONObject data) {
        logger.info("Inside API task");
        
        // parsing path param url
        var uriStr = uri;
        if (data != null) {
            var map = data.toMap();
            for (var entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    var keyParam = "{" + key + "}";
                    if (uriStr.contains(keyParam)) {
                        uriStr = uriStr.replace(keyParam, value.toString());
                    }
                }
            }
        }
        
        var requestBuilder = HttpRequest.newBuilder(URI.create(uriStr)).timeout(Duration.ofSeconds(30));
        
        if (data != null && ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method))) {
            requestBuilder.headers("Content-Type", "application/json");
            requestBuilder.method(method, HttpRequest.BodyPublishers.ofString(data.toString()));
        } else {
            requestBuilder.method(method, HttpRequest.BodyPublishers.noBody());        
        }

        HttpRequest request = requestBuilder.build();
        JSONObject json = new JSONObject();

        logger.info("URL: " + request.uri().toString());
        logger.info("Method: " + request.method());
        String dataStr = data != null ? data.toString() : "";
        logger.info("data: " + dataStr);
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
            String body = response.body();
            logger.info("response status: " + response.statusCode());
            logger.info("response body: " + body);
            json.put("status", response.statusCode());
            JSONObject jsonBody = null;
            try {
                jsonBody = new JSONObject(body);
            } catch (JSONException e) {}
            json.put("body", jsonBody);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
    
}
