package me.iantapply.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.iantapply.utils.FileUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class UploadResourcePackHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Get the content length and byte arrays
        List<String> contentLengths = exchange.getRequestHeaders().get("Content-Length");
        String rawContentLengths = contentLengths.get(0);
        int contentLength = Integer.parseInt(rawContentLengths);

        // turn content length into the actual contents of the file
        byte[] content = new byte[contentLength];
        new DataInputStream(exchange.getRequestBody()).readFully(content);

        // Main file content
        byte[] fileContent = FileUtils.extractFileContent(content);

        // Extract the final zip file to the location
        FileUtils.extractToLocation(fileContent);

        // Print debug to console
        System.out.println("Received POST request with file and uploaded it to directory.");
    }
}
