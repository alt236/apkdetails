/*
 * Copyright (C) 2012 Prasanta Paul, http://prasanta-paul.blogspot.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.alt236.apkdetails.decoder.utils;

import java.io.ByteArrayInputStream;

/**
 * Byte <-> Java Premitive data types.
 *
 * @author Prasanta Paul
 */
public class Utils {

    private static final String[] HEX_LETTERS = {"A", "B", "C", "D", "E", "F"};

    /**
     * Enable minimum logging.
     */
    public static boolean isProduction = false;

    /**
     * Byte array to Hex info
     *
     * @param bytes
     * @return
     */
    public static String toHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0)
            return "";
        String hex = "";
        for (byte aByte : bytes) {
            int num = 0xFF & aByte;
            int div = num / 16;
            int rem = num % 16;

            // 0xF9
            if (div > 9) {
                div -= 10;
                hex += " 0x" + HEX_LETTERS[div];
            } else
                hex += " 0x" + div;

            if (rem > 9) {
                rem -= 10;
                hex += "" + HEX_LETTERS[rem];
            } else
                hex += "" + rem;
        }
        return hex;
    }

    /**
     * It will convert <= 4 bytes value to Int
     *
     * @param bytes
     * @param isBigEndian - is it Big or Little Endian
     * @return
     */
    public static int toInt(byte[] bytes, boolean isBigEndian) {
        int x = 0;
        int numOfBytes = bytes.length;

        if (numOfBytes > 4)
            numOfBytes = 4; // Can't consider more than 4 bytes

        for (int i = 0; i < numOfBytes; i++) {
            if (i == 0) {
                if (isBigEndian)
                    x = 0xFF & bytes[i]; // AND with 0xFF to ignore sign bit of
                    // bytes to Int assignment
                else
                    // Little Endian
                    x = 0xFF & bytes[numOfBytes - 1];
            } else {
                if (isBigEndian)
                    x = (x << 8) | (0xFF & bytes[i]);
                else
                    x = (x << 8) | (0xFF & bytes[numOfBytes - 1 - i]);
            }
        }

        return x;
    }

    /**
     * It will convert <= 8 bytes data into Long
     *
     * @param bytes
     * @param isBigEndian is it Big or Little Endian
     * @return
     */
    public static long toLong(byte[] bytes, boolean isBigEndian) {
        long x = 0;
        int numOfBytes = bytes.length;

        if (numOfBytes > 8)
            numOfBytes = 8; // Can't consider more than 8 bytes

        for (int i = 0; i < numOfBytes; i++) {
            if (i == 0) {
                if (isBigEndian)
                    x = 0xFF & bytes[i]; // AND with 0xFF to ignore sign bit of
                    // bytes to Long assignment
                else
                    // Little Endian
                    x = 0xFF & bytes[numOfBytes - 1];
            } else {
                if (isBigEndian)
                    x = (x << 8) | (0xFF & bytes[i]);
                else
                    x = (x << 8) | (0xFF & bytes[numOfBytes - 1 - i]);
            }
        }

        return x;
    }

    /**
     * Convert Chars (16-bit) to String. Terminated by 0x00 and Padding byte 0.
     *
     * @return
     */
    public static String toString(byte[] charBuf, boolean isBigEndian) throws Exception {
        StringBuilder strBuf = new StringBuilder();
        byte[] buf_2 = new byte[2];
        ByteArrayInputStream in = new ByteArrayInputStream(charBuf);

        while (in.read(buf_2) != -1) {
            int code = toInt(buf_2, isBigEndian);
            if (code == 0x00) // End of String
                break;
            else
                strBuf.append((char) code);
        }
        return strBuf.toString();
    }

//	public static void main(String[] args){
//		System.out.println("->"+ (byte)142);
//	}
}
