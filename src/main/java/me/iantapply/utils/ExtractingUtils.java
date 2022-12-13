package me.iantapply.utils;

import me.iantapply.PackMC;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

public class ExtractingUtils {

    // The folder that contains all the uploaded resource packs
    public static final File PACK_FOLDER = new File(Objects.requireNonNull(PackMC.getConfiguration().getString("resource-pack-folder")));

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
    public static String putFileInDirectory(byte[] fileContent) throws IOException {
        MessageDigest sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        sha256.update(fileContent);

        byte[] rawHash = sha256.digest();
        String hexHash = Utils.binarytoHexadecimal(rawHash);

        File file = new File(PACK_FOLDER + "/" + hexHash + ".zip");
        if (!file.exists()) {
            Files.write(file.toPath(), fileContent);
        }

        return hexHash;
    }

    /**
     * Validates that a zips bytes contain the file "packs.mcmeta"
     * @param content bytes/files to check
     * @return true or false if it contains it
     */
    public static String validateResourcePackContent(byte[] content) {
        ZipInputStream zipInput = new ZipInputStream(new ByteArrayInputStream(content));
        boolean hasPackMcmeta = false;

        while(true) {
            try {
                ZipEntry currentEntry = zipInput.getNextEntry();
                if (currentEntry == null) {
                    return hasPackMcmeta ? null : "This resource pack doesn't have a pack.mcmeta file in the root directory";
                }

                if (currentEntry.getName().equals("pack.mcmeta")) {
                    hasPackMcmeta = true;
                }
            } catch (ZipException var4) {
                return "This is not a valid ZIP file.";
            } catch (IOException var5) {
                return "This ZIP file ended unexpectedly";
            }
        }
    }

    public static byte[] getFileFromDirectory(String hexHash) {
        File file = new File(PACK_FOLDER + "/" + hexHash + ".zip");

        if (!file.exists()) {
            return null;
        }

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException ioTrouble) {
            file.delete();
            return null;
        }
    }
}
