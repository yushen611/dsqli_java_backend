import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqliApi {

    public static void main(String[] args) throws IOException {
        //获取日志记录器对象
        Logger logger= Logger.getLogger("日志");
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/ping", new MyHandler());
        server.createContext("/SQLiTest", new PostHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        logger.log(Level.INFO,"Server running on port " + port);
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "Hello, this is java SQLi detector !";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class PostHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if (method.equalsIgnoreCase("POST")) {
                // 获取请求body中的name字段
                String query = getPostParamStringValue(exchange, "query");
                //sqli检测
                Map<String,String> res = SQLParse.isSQLiAnalyzer(query);

                //结果封装
                JSONObject object = new JSONObject();
                for (Map.Entry<String, String> entry : res.entrySet()) {
                    object.put(entry.getKey(),entry.getValue());
                }

                // 构造响应内容
                String response = "" + object;
                exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(response.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // 返回405 Method Not Allowed
            }
        }
    }

    /**
     * 从POST请求的请求体中获取指定参数名的参数值
     */
    private static String getPostParamStringValue(HttpExchange exchange, String paramName)
            throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        byte[] buffer = new byte[2048];
        int len = inputStream.read(buffer);
        String requestBody = new String(buffer, 0, len, StandardCharsets.UTF_8);
        JSONObject object = JSONObject.parseObject(requestBody);
        try{
            return object.getString(paramName);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

