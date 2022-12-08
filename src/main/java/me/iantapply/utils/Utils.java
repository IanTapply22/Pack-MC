package me.iantapply.utils;

public class Utils {

    /**
     * Converts binary bytes to hexadecimal (for file naming)
     * @param bytes bytes containing binary
     * @return the hexadecimal name
     */
    public static String binarytoHexadecimal(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }

        return sb.toString();
    }
}
