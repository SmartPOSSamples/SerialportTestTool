package com.cloudpos.serialportopen;

import java.nio.charset.StandardCharsets;

public class StringUtil {

    public static String stringToHexString(String str) {
        StringBuilder hexString = new StringBuilder();
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

}
