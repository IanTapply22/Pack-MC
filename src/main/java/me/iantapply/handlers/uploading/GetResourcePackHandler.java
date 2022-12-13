package me.iantapply.handlers.uploading;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Builder;
import me.iantapply.utils.ExtractingUtils;
import me.iantapply.utils.Utils;

import java.io.IOException;

public class GetResourcePackHandler implements HttpHandler {

    public static String GET_RESOURCE_PACK_PREFIX = "/get-resource-pack/";

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (exchange != null) {
            Utils.discardInput(exchange.getRequestBody());

            String hexHash = exchange.getRequestURI().getPath().substring(GET_RESOURCE_PACK_PREFIX.length());
            byte[] content = ExtractingUtils.getFileFromDirectory(hexHash);

            if (content != null) {
                if (exchange.getRequestMethod().equalsIgnoreCase("head")) {
                    exchange.sendResponseHeaders(200, -1);
                } else {
                    exchange.sendResponseHeaders(200, content.length);
                    exchange.getResponseBody().write(content);
                    exchange.getResponseBody().flush();
                }
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        }
    }
}
