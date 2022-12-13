package me.iantapply;

import com.sun.net.httpserver.HttpExchange;
import lombok.Getter;
import me.iantapply.handlers.BasicRequestHandler;
import me.iantapply.handlers.WebRequestHandler;
import me.iantapply.listeners.SendResourcePackOnJoinListener;
import me.iantapply.utils.ConfigurationUtils;
import me.iantapply.utils.FileUtils;
import me.iantapply.utils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public final class PackMC extends JavaPlugin {

    public static WebRequestHandler requestHandler;

    @Getter public static FileConfiguration configuration;
    public static File configFile;


    public PackMC() {
        requestHandler = new BasicRequestHandler();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        ConfigurationUtils.createConfig();
        FileUtils.copyFilesFromResources();

        // Start the web server for uploading resource packs
        try {
            ServerUtils.startWebServer(getConfiguration().getInt("host-port"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Upload server has successfully been started!");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Cannot start resource pack server due to an error!");
            throw new RuntimeException(e);
        }

        // Send startup message to console
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "PackMC has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ServerUtils.stopWebServer();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Upload server has successfully been stopped!");

        // Send shutdown message to console
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "PackMC has been disabled!");
    }


    public void handle(HttpExchange exchange, File output) throws IOException {
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
