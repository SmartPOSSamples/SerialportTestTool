package com.cloudpos.serialportopen;

import java.util.Locale;

/**
 * @author john
 * Convert byte[] to hex string
 */
public class ByteConvertStringUtil {

    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars).toUpperCase(Locale.ROOT);
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return "";
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv).append(" ");
        }
        return stringBuilder.toString().toUpperCase(Locale.ROOT);
    }

    public static String buf2StringCompact(byte[] buf) {
        int i, index;
        StringBuffer sBuf = new StringBuffer();
        sBuf.append("[");
        for (i = 0; i < buf.length; i++) {
            index = buf[i] < 0 ? buf[i] + 256 : buf[i];
            if (index < 16) {
                sBuf.append("0" + Integer.toHexString(index));
            } else {
                sBuf.append(Integer.toHexString(index).toUpperCase());
            }
            sBuf.append(" ");
        }
        String substring = sBuf.substring(0, sBuf.length() - 1);
        return substring + "]";
    }

    public static String byteToHexString(byte src) {
        StringBuilder stringBuilder = new StringBuilder("");
        int v = src & 0xFF;
        String hv = Integer.toHexString(v);
        return hv;
    }

    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    public static byte[] addFrameMarkers(byte[] input) {
        if (input == null) {
            return new byte[]{0x02, 0x03}; // return [0x02, 0x03] when input is null
        }

        // create new array with length = input.length + 2
        byte[] result = new byte[input.length + 2];

        // set first byte to 0x02 (frame start)
        result[0] = 0x02;

        // copy original data to the middle
        System.arraycopy(input, 0, result, 1, input.length);

        // set last byte to 0x03 (frame end)
        result[result.length - 1] = 0x03;

        return result;
    }

    /**
     * Extract frame data between 0x02 and 0x03 markers.
     * Finds the first 0x02, then the first 0x03 after it.
     * Returns the data between them, or empty array if no valid frame found.
     */
    public static byte[] extractDataSegments(byte[] input) {
        if (input == null || input.length < 3) {
            return new byte[0];
        }

        // find first 0x02 (frame start marker) in the buffer
        int startIndex = -1;
        for (int i = 0; i < input.length; i++) {
            if (input[i] == 0x02) {
                startIndex = i;
                break;
            }
        }
        if (startIndex == -1) return new byte[0];

        // find first 0x03 after the start marker (frame end marker)
        for (int i = startIndex + 1; i < input.length; i++) {
            if (input[i] == 0x03) {
                if (i - startIndex >= 2) { // at least one data byte between markers
                    int length = i - startIndex - 1;
                    byte[] segment = new byte[length];
                    System.arraycopy(input, startIndex + 1, segment, 0, length);
                    return segment;
                }
                // 0x02 0x03 with no data in between, keep searching for next 0x03
            }
        }
        return new byte[0];
    }

}
