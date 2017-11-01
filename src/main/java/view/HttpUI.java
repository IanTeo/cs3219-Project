package view;

import logic.Controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpUI implements UserInterface {
    private Controller controller;

    public HttpUI(Controller controller) {
        this.controller = controller;
    }

    public void start() {
        System.out.println("Loading data in JSON folder");
        System.out.println(controller.executeQuery("load json"));
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/top", new TopHandler());
            server.setExecutor(null);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> queryMap = new HashMap<>();
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            // We want to ensure the pair has both key and value, seperated by "="
            if (pair.length > 1) {
                queryMap.put(pair[0], pair[1]);
            }
        }
        return queryMap;
    }

    class TopHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Map<String, String> queryMap = queryToMap(t.getRequestURI().getQuery());
            System.out.println("venue=" + queryMap.get("venue"));
            String response = "Invalid Parameters";
            if (queryMap.containsKey("count") && queryMap.containsKey("venue") && queryMap.containsKey("type")) {
                response = controller.executeQuery(String.format("top %s %s %s",
                        queryMap.get("count"), queryMap.get("type"), queryMap.get("venue")));
                t.sendResponseHeaders(200, response.getBytes().length);
            } else {
                t.sendResponseHeaders(400, response.length());
            }
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}