package view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import logic.Controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpUI implements UserInterface {
    private Controller controller;

    public HttpUI(Controller controller) {
        this.controller = controller;
    }

    public void start() {
        try {
            int port = System.getenv().containsKey("PORT") ?
                    Integer.parseInt(System.getenv().get("PORT")) : 8000;

            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new HttpRequestHandler());
            server.start();
            System.out.println("Server ready on port " + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class HttpRequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI uri = httpExchange.getRequestURI();
            Map<String, String> queryMap = queryToMap(uri.getQuery());
            queryMap.put("command", uri.getPath().substring(1));
            String response = controller.executeQuery(queryMap);

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
