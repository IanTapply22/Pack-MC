package me.iantapply;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import me.iantapply.handlers.BasicRequestHandler;
import me.iantapply.handlers.RequestHandler;
import me.iantapply.handlers.WebRequestHandler;
import me.iantapply.utils.ConfigurationUtils;
import me.iantapply.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.util.List;

public final class PackMC extends JavaPlugin {

    public static WebRequestHandler requestHandler;

    @Getter public static FileConfiguration customConfig;
    public static File customConfigFile;


    private PackMC() {
        this.requestHandler = new BasicRequestHandler();
    }

    @Override
    public void onEnable() {
        ConfigurationUtils.createCustomConfig();

        try {
            start(getConfig().getInt("host-port"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "PackMC has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("PackMC has been disabled!");
    }


    public void start(int port) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RequestHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private void handle(HttpExchange exchange, File output) throws IOException {
        List<String> lines = FileUtils.readFile(output);
        String response = "";
        for (String line : lines)
            response += line;

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
        writer.write(response);
        writer.close();
    }
}
