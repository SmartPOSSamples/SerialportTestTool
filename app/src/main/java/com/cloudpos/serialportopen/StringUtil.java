package com.cloudpos.serialportopen;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class StringUtil {

    public static String stringToHexString(String str) {
        StringBuilder hexString = new StringBuilder();
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String hexStringToString(String hexString) {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexString.length(); i += 2) {
            String str = hexString.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static byte[] hexStringToByteArray(String hex) {
        int length = hex.length();
        ByteBuffer buffer = ByteBuffer.allocate(length / 2);
        CharBuffer charBuffer = CharBuffer.wrap(hex.toCharArray());
        while (charBuffer.hasRemaining()) {
            int high = Character.digit(charBuffer.get(), 16);
            int low = Character.digit(charBuffer.get(), 16);
            buffer.put((byte) ((high << 4) + low));
        }
        return buffer.array();
    }

}

