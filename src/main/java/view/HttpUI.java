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
    public static final int HTTP_OK = 200;
    public static final int HTTP_BAD_REQUEST = 400;
    
    private Controller controller;

    public HttpUI(Controller controller) {
        this.controller = controller;
    }

    public void start() {
        try {
            System.out.println("Loading data...");
            controller.loadData("/");

            int port = System.getenv().containsKey("PORT") ?
                    Integer.parseInt(System.getenv().get("PORT")) : 8000;

            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/top", new HttpRequestHandler("top"));
            server.createContext("/test", (exchange) -> {
                    String response = "HELLO!";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
            });
            server.start();
            System.out.println("Server is running on port " + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class HttpRequestHandler implements HttpHandler {
        String query;

        public HttpRequestHandler(String query) {
            this.query = query;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> queryMap = queryToMap(exchange.getRequestURI().getQuery());
            queryMap.put("query", query);

            String response;
            int responseType;
            try {
                response = controller.executeQuery(queryMap);
                responseType = HTTP_OK;
            } catch (Exception e) {
                response = e.getMessage();
                responseType = HTTP_BAD_REQUEST;
            }

            exchange.sendResponseHeaders(responseType, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
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
}