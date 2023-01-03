package me.iantapply.states;

import lombok.Getter;
import me.iantapply.PackMC;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ResourcePackState {

    // URL prefix for the download/upload server
    @Getter public static String resourcePackUrlPrefix = "http://" + PackMC.getConfiguration().getString("host-hostname") + ":" + PackMC.getConfiguration().getInt("host-port") + "/";
    public static FileConfiguration configuration = PackMC.getConfiguration();

    /**
     * Sets the resource pack ID for a world
     * @param worldName world name to set it for
     * @param packID ID of pack to set for that world
     * @param forcePack should we force them to accept the pack?
     * @throws IOException
     */
    public static void setResourcePack(String worldName, String packID, Boolean forcePack) throws IOException, NoSuchAlgorithmException {
        PackMC.getWorldData().set(worldName + ".pack-id", packID);
        PackMC.getWorldData().set(worldName + ".force-pack", forcePack);
        PackMC.getWorldData().save("plugins/PackMC/world-data.yml");
    }

    /**
     * Gets the resource pack URL for a world
     * @param worldName name of world to get resource pack for
     * @return the URL for the resource pack download
     */
    public static String getResourcePackUrlForWorld(String worldName) {
        String packID = PackMC.getWorldData().getString(worldName + ".pack-id");

        return resourcePackUrlPrefix + "get-resource-pack/" + packID;
    }

    // UNUSED METHOD DUE TO ERRORS | WILL FIX IN FUTURE
    public static byte[] getbinarySha1Hash(String worldName) throws IOException, NoSuchAlgorithmException {
        File resourcePackFile = new File(PackMC.getConfiguration().getString("uploaded-resource-pack-folder") + "/" + PackMC.getWorldData().getString(worldName + ".pack-id") + ".zip");
        InputStream source = Files.newInputStream(resourcePackFile.toPath());
        MessageDigest md = MessageDigest.getInstance("SHA-1");

        byte[] dataBytes = new byte[1024];

        int nread = 0;

        while ((nread = source.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        };

        byte[] mdbytes = md.digest();

        return mdbytes;
    }
}
