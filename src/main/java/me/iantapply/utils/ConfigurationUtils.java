package me.iantapply.utils;

import me.iantapply.PackMC;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static me.iantapply.PackMC.configFile;

public class ConfigurationUtils {

    /**
     * Creates the config file with the contents of the one in the resource folder
     */
    public static void createConfig() {
        configFile = new File(PackMC.getPlugin(PackMC.class).getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            PackMC.getPlugin(PackMC.class).saveResource("config.yml", false);
        }

        PackMC.configuration = new YamlConfiguration();
        try {
            PackMC.configuration.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
