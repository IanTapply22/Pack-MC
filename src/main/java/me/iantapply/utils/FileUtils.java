package me.iantapply.utils;

import me.iantapply.PackMC;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    // the folder inside the base server folder which contains website files
    public static final File PACK_FOLDER = new File(PackMC.getCustomConfig().getString("resource-pack-folder"));

    /**
     * Extracts file content from the content and headers
     * @param contentPlusHeaders content and header byte arrays
     * @return the bytes for the file content
     */
    public static byte[] extractFileContent(byte[] contentPlusHeaders) {
        int contentStartIndex = 0;
        boolean hadLineBreak = false;

        int contentBoundIndex;
        char currentValue;
        while(contentStartIndex < contentPlusHeaders.length - 1) {
            contentBoundIndex = (char)contentPlusHeaders[contentStartIndex];
            currentValue = (char)contentPlusHeaders[contentStartIndex + 1];
            if (contentBoundIndex == 13 && currentValue == '\n') {
                contentStartIndex += 2;
                if (hadLineBreak) {
                    break;
                }

                hadLineBreak = true;
            } else {
                hadLineBreak = false;
                ++contentStartIndex;
            }
        }

        for(contentBoundIndex = contentPlusHeaders.length - 4; contentBoundIndex >= contentStartIndex; --contentBoundIndex) {
            currentValue = (char)contentPlusHeaders[contentBoundIndex];
            char nextValue = (char)contentPlusHeaders[contentBoundIndex + 1];
            if (currentValue == '\r' && nextValue == '\n') {
                break;
            }
        }

        return Arrays.copyOfRange(contentPlusHeaders, contentStartIndex, contentBoundIndex);
    }

    /**
     * Extracts the file content bytes to the destination folder
     * @param fileContent file content bytes generates from request
     * @return the hexHash (hexadecimal name)
     * @throws IOException
     */
    public static String extractToLocation(byte[] fileContent) throws IOException {
        MessageDigest sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        sha256.update(fileContent);

        byte[] rawHash = sha256.digest();
        String hexHash = Utils.binarytoHexadecimal(rawHash);

        File file = new File("plugins/PackMC/packs/uploadFiles/" + hexHash + ".zip");
        if (!file.exists()) {
            Files.write(file.toPath(), fileContent);
        }

        return hexHash;
    }

    // if the file does not exist, create it
    public static File createFileIfNecessary(File file) {
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.err.println("Could not create file " + file.getName() + "!");
                    return null;
                }
                return file;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else
            return file;
    }

    // if the file does not exist, create it
    public static File createFolderIfNecessary(File folder) {
        if (!folder.exists() && !folder.mkdirs()) {
            System.err.println("Could not create folder " + folder.getName() + "!");
            return null;
        }
        return folder;
    }


    public static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    public static File getPackFolder() {
        return createFolderIfNecessary(PACK_FOLDER);
    }

    public static File getWebsite404() {
        return createFileIfNecessary(new File(getPackFolder(), "404.html"));
    }

    // always assumes index.html
    public static File getWebsitePage(String request) {
        if (request.endsWith("/")) // ends with folder directory
            request += "index.html";
        File file = new File(getPackFolder(), request);
        if (file.exists() && file.isDirectory())
            return getWebsitePage(request + "/index.html");
        if (!file.exists())
            return getWebsite404();
        return file;
    }
}
