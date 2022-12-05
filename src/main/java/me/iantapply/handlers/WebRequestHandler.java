package me.iantapply.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface WebRequestHandler {
    // handles an HTTP request to the pack download link
    // (can handle folders for websites)
    String handle(HttpExchange exchange, String request) throws IOException;
}
