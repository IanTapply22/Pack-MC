package me.iantapply.utils;

import me.iantapply.PackMC;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static me.iantapply.PackMC.configFile;
import static me.iantapply.PackMC.worldDataFile;

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

    /**
     * Creates a world data file to store resource pack ID data
     */
    public static void createWorldDataConfig() {
        worldDataFile = new File(PackMC.getPlugin(PackMC.class).getDataFolder(), "world-data.yml");
        if (!worldDataFile.exists()) {
            worldDataFile.getParentFile().mkdirs();
            PackMC.getPlugin(PackMC.class).saveResource("world-data.yml", false);
        }

        PackMC.worldData = new YamlConfiguration();
        try {
            PackMC.worldData.load(worldDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
