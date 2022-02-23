package team.solution.teham.core.elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import org.json.JSONObject;
import org.springframework.http.HttpMethod;

import team.solution.teham.core.ProcessExecutor;

public class TaskApi extends Element {

    private URI uri;
    
    private HttpMethod method;

    public TaskApi(
        String id, 
        String name, 
        String source, 
        String target,

        URI uri,
        HttpMethod method
    ) {
        super(id, name, source, target);
        this.uri = uri;
        this.method = method != null ? method : HttpMethod.GET;
    }

    public TaskApi(
        String id, 
        String name, 
        String source, 
        String target,

        URI uri
    ) {
        this(id, name, source, target, uri, null);
    }

    @Override
    public JSONObject handle(ProcessExecutor processExecutor, JSONObject data) {
        var requestBuilder = HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(30));

        if (data != null && (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH)) {
            requestBuilder.headers("Content-Type", "application/json");
            requestBuilder.method(method.name(), HttpRequest.BodyPublishers.ofString(data.toString()));
        } else {
            requestBuilder.method(method.name(), null);        
        }

        HttpRequest request = requestBuilder.build();
        JSONObject json = new JSONObject();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
            String body = response.body();
            json.put("status", response.statusCode())
                .put("body", (body != null) ? new JSONObject(body) : null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
    
}
