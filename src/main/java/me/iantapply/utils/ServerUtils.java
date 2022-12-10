package me.iantapply.utils;

import com.sun.net.httpserver.HttpServer;
import me.iantapply.handlers.RequestHandler;
import me.iantapply.handlers.uploading.UploadResourcePackHandler;

import java.net.InetSocketAddress;

public class ServerUtils {

    public static HttpServer server;

    public static void startWebServer(int port) throws Exception {
        // Create a java HttpServer instance
        server = HttpServer.create(new InetSocketAddress(port), 0);

        // Create URI paths
        server.createContext("/", new RequestHandler());
        server.createContext("/upload-resource-pack/", new UploadResourcePackHandler());
        server.setExecutor(null);

        // Start it
        server.start();
    }

    public static void stopWebServer() {
        // Stop the server with a delay of 1
        server.stop(1);
    }
}
