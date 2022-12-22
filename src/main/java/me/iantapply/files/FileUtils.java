package me.iantapply.files;

import me.iantapply.PackMC;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileUtils {

    /**
     * Copy all html files and folders to the plugin data folder
     */
    public static void copyFilesFromResources() {
        // Create "webpages" folder
        if (!new File("plugins/PackMC/webpages").exists() && !new File("plugins/PackMC/webpages").mkdirs()) {
            new File("plugins/PackMC/webpages");
        }

        // Create "uploaded-packs" folder
        if (!new File("plugins/PackMC/uploaded-packs").exists() && !new File("plugins/PackMC/uploaded-packs").mkdirs()) {
            new File("plugins/PackMC/uploaded-packs");
        }

        // Create "active-packs" folder
        if (!new File("plugins/PackMC/active-packs").exists() && !new File("plugins/PackMC/active-packs").mkdirs()) {
            new File("plugins/PackMC/active-packs");
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
        if (!new File("plugins/PackMC/webpages", "webpages/404.html").exists()) {
            PackMC.getPlugin(PackMC.class).saveResource("webpages/404.html", false);
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

    /**
     * Gets the 404 webpage
     * @return the 404 webpage file
     */
    public static File getWebsite404() {
        return new File("plugins/PackMc/webpages", "404.html");
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
