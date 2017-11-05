package view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import logic.Controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class HttpUI implements UserInterface {
    private Controller controller;

    public HttpUI(Controller controller) {
        this.controller = controller;
    }

    public void start() {
        try {
            System.out.println("Starting server..");
            controller.executeQuery("load");
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/top", new TopHandler());
            server.start();
            System.out.println("Server ready on port 8000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class TopHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Map<String, String> queryMap = queryToMap(httpExchange.getRequestURI().getQuery());
            String query = "top %s %s %s";
            String response = controller.executeQuery(String.format(query, queryMap.get("count"), queryMap.get("type"), queryMap.get("venue")));
            httpExchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
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
}
