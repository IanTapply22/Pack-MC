package me.iantapply.utils;

import com.sun.net.httpserver.HttpExchange;
import me.iantapply.PackMC;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class Utils {

    /**
     * Converts binary bytes to hexadecimal (for file naming)
     * @param bytes bytes containing binary
     * @return the hexadecimal name
     */
    public static String binarytoHexadecimal(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }

        return sb.toString();
    }

    /**
     * Serves a web page with replaced strings
     * @param exchange the exchange of the request
     * @param page the page you want
     * @param whatToReplace what string to replace
     * @param whatToReplaceWith what to replace it with
     * @throws IOException
     */
    public static void serveWebpage(HttpExchange exchange, String page, String whatToReplace, String whatToReplaceWith) throws IOException {
        String request = page; // ASCII string replaces "%20" with " " for example. subtring removes first "/"
        String response = PackMC.requestHandler.handle(exchange, request);
        String response2 = response.replace(whatToReplace, whatToReplaceWith);
        exchange.sendResponseHeaders(200, response2.getBytes().length);
        OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
        writer.write(response2);
        writer.close();
    }

    /**
     * Serves a webpage to the user
     * @param exchange the exchange of the request
     * @param page the page you want to display
     * @throws IOException
     */
    public static void serveWebpage(HttpExchange exchange, String page) throws IOException {
        String request = page; // ASCII string replaces "%20" with " " for example. subtring removes first "/"
        String response = PackMC.requestHandler.handle(exchange, request);
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
        writer.write(response);
        writer.close();
    }

    /**
     * Discards the input of the request (to prevent errors)
     * @param input the input to discard
     * @throws IOException
     */
    public static void discardInput(InputStream input) throws IOException {
        byte[] discardBuffer = new byte[10_000];
        while (input.read(discardBuffer) != -1) {

        }
        input.close();
    }
}
