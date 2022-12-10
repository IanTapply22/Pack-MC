package me.iantapply.handlers.uploading;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.iantapply.utils.ExtractingUtils;
import me.iantapply.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class UploadResourcePackHandler implements HttpHandler {

    // The file size limit (100MB)
    private int FILE_SIZE_LIMIT = 100 * 1024 * 1024;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Pre determined valid content length variable
        boolean hasValidContentLength = false;

        // Get the content length and byte arrays
        List<String> contentLengths = exchange.getRequestHeaders().get("Content-Length");

        // If content lengths isn't null or the size is 1
        if (contentLengths != null && contentLengths.size() == 1) {

            // Try...
            try {
                // Get raw content lengths (in string variation)
                String rawContentLengths = contentLengths.get(0);
                // Get content length as an integer (amount of bytes)
                int contentLength = Integer.parseInt(rawContentLengths);

                // If the content length is greater than or is 0
                if  (contentLength >= 0) {

                    // Turn content length into the actual contents of the file
                    byte[] content = new byte[contentLength];
                    new DataInputStream(exchange.getRequestBody()).readFully(content);

                    // Main file content
                    byte[] fileContent = ExtractingUtils.extractFileContent(content);
                    // Validate the content
                    String fileError = ExtractingUtils.validateResourcePackContent(fileContent);

                    // If the size is less than the limit then...
                    if (contentLength <= FILE_SIZE_LIMIT) {
                        // Set valid content length as true
                        hasValidContentLength = true;

                        // If the resource pack IS A RESOURCE PACK...
                        if (fileError == null) {
                            // Try..
                            try {
                                // Extract the final zip file to the location
                                String hexHash = ExtractingUtils.extractToLocation(fileContent);

                                // Print debug to console
                                System.out.println("Received POST request with file and uploaded it to directory.");

                                // Redirect to upload successful page
                                Utils.serveWebpage(exchange, "successful-upload.html", "%RESOURCE_PACK_ID%", hexHash);
                            } catch (IOException writeFailed) {
                                // Send IO error console message if there is an IOException
                                Bukkit.getConsoleSender().sendMessage("An IO error occurred on the server.");
                            }
                        } else {
                            // If it's not a resource pack then redirect to error page
                            Utils.serveWebpage(exchange, "unsuccessful-upload.html", "%ERROR%", fileError);
                        }
                    } else {
                        // If it's bigger than the limit then set valid content lengths as true
                        hasValidContentLength = true;

                        // Read and discard the input (the server doesn't like if I don't do this)
                        Utils.discardInput(exchange.getRequestBody());
                        // Send file too large error message in console
                        Bukkit.getConsoleSender().sendMessage("This file is too large. It can be at most 100 MB.");
                    }
                }
            } catch (NumberFormatException badContentLength) {
                // No need to put anything here, it knows what to do
            } catch (IOException badContent) {
                // Send bad content error when an IOException occurs in console
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "badContent: " + badContent);
            }
        }

        // If there isn't a valid content length...
        if (!hasValidContentLength) {
            // Discard the input/request body
            Utils.discardInput(exchange.getRequestBody());
            // Return 400 error code with no response length
            exchange.sendResponseHeaders(400, -1);
        }
    }
}
