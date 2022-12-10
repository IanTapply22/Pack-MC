package me.iantapply.utils;

import me.iantapply.PackMC;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public class FileUtils {

    // The folder that contains all the uploaded resource packs
    public static final File PACK_FOLDER = new File(Objects.requireNonNull(PackMC.getConfiguration().getString("resource-pack-folder")));

    /**
     * Copy all html files and folders to the plugin data folder
     */
    public static void copyFilesFromResources() {
        // Create "webpages" folder
        if (!new File("plugins/PackMC/webpages").exists() && !new File("plugins/PackMC/webpages").mkdirs()) {
            new File("plugins/PackMC/webpages");
        }

        // Create html files
        if (!new File("plugins/PackMC/webpages", "webpages/index.html").exists()) {
            PackMC.getPlugin(PackMC.class).saveResource("webpages/index.html", false);
        }
        if (!new File("plugins/PackMC/webpages", "webpages/successful-upload.html").exists()) {
            PackMC.getPlugin(PackMC.class).saveResource("webpages/successful-upload.html", false);
        }
        if (!new File("plugins/PackMC/webpages", "webpages/unsuccessful-upload.html").exists()) {
            PackMC.getPlugin(PackMC.class).saveResource("webpages/unsuccessful-upload.html", false);
        }

        // Create "packs" folder
        if (!new File("plugins/PackMC/packs").exists() && !new File("plugins/PackMC/packs").mkdirs()) {
            new File("plugins/PackMC/packs");
        }
    }

    /**
     * Reads the contents of a specified file
     * @param file file to read the contents of
     * @return a list of lines of the file
     * @throws IOException
     */
    public static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    // FIX THIS
    public static File getWebsite404() {
        return new File(PACK_FOLDER, "404.html");
    }

    /**
     * Returns the website page requested
     * @param request request file
     * @return the page requested
     */
    public static File getWebsitePage(String request) {
        if (request.endsWith("/")) // ends with folder directory
            request += "webpages/index.html";
        File file = new File("plugins/PackMC/webpages", request);
        if (file.exists() && file.isDirectory())
            return getWebsitePage(request + "index.html");
        if (!file.exists())
            return getWebsite404();
        return file;
    }
}
