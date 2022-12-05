package me.iantapply.utils;

import me.iantapply.PackMC;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static me.iantapply.PackMC.customConfigFile;

public class ConfigurationUtils {

    public static void createCustomConfig() {
        customConfigFile = new File(PackMC.getPlugin(PackMC.class).getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            PackMC.getPlugin(PackMC.class).saveResource("config.yml", false);
        }

        PackMC.customConfig = new YamlConfiguration();
        try {
            PackMC.customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        /* User Edit:
            Instead of the above Try/Catch, you can also use
            YamlConfiguration.loadConfiguration(customConfigFile)
        */
    }
}
