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

package com.pras.utils;

import java.io.File;
import java.io.PrintStream;

/**
 * Based on your target platform- edit this file and add appropriate Log console
 * e.g.
 * for Android, android.util.Log
 * for J2ME, Desktop File based logging.
 * </br>
 * By default Logging will be enabled
 *
 * @author Prasanta Paul
 */
public class Log {

    private static final boolean LOG_TO_FILE_ENABLED = false;
    public static final int DEBUG_LEVEL = 1 << 0;
    public static final int PRODUCTION_LEVEL = 1 << 1;

    /**
     * Set to false if you don't need Log output
     * Default values.
     */
    private static boolean isLogEnabled = true;
    private static int logLevel = DEBUG_LEVEL;

    private static final String logFile = System.getProperty("user.dir") + File.separator + "apkExtractor_log.txt";

    private static PrintStream out = null;

    private static final StringBuffer buf = new StringBuffer();

    static {
        if (LOG_TO_FILE_ENABLED) {
            System.out.println("Log File: " + logFile);
        }
    }

    /**
     * Enable logging
     */
    public static void enableLog() {
        isLogEnabled = true;
    }

    /**
     * Disable logging. Good for production release.
     */
    public static void disableLog() {
        isLogEnabled = false;
    }

    /**
     * Enable production mode
     */
    public static void setLogLevel(int level) {
        logLevel = level;
    }

    /**
     * Production log
     *
     * @param msg
     */
    public static void d(String msg) {
        if (logLevel == DEBUG_LEVEL)
            write(null, msg);
    }

    /**
     * Production Log
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (logLevel == DEBUG_LEVEL)
            write(null, msg);
    }

    /**
     * Production log
     *
     * @param msg
     */
    public static void p(String msg) {
        if (logLevel <= PRODUCTION_LEVEL)
            write(null, msg);
    }

    /**
     * Production Log
     *
     * @param tag
     * @param msg
     */
    public static void p(String tag, String msg) {
        if (logLevel <= PRODUCTION_LEVEL)
            write(tag, msg);
    }

    /**
     * Pring Production Log message
     *
     * @param tag Log TAG
     * @param msg Log Message
     */
    private static void write(String tag, String msg) {

        if (!isLogEnabled)
            return;
        // clear buf
        buf.delete(0, buf.length());

        if (tag != null)
            buf.append("[" + tag + "] ");

        if (msg != null)
            buf.append(msg);
        buf.append("\n");

        print(buf.toString());
    }

    /**
     * Print Exception irrespective of Log Level.
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    /**
     * Print Exception irrespective of Log Level.
     *
     * @param msg
     * @param ex
     */
    public static void e(String tag, String msg, Exception ex) {

        // clear buf
        buf.delete(0, buf.length());

        if (tag != null)
            buf.append("[" + tag + "] ");

        if (msg != null)
            buf.append(msg);

        if (buf.length() > 0) {
            buf.append("\n");
            print(buf.toString());
        }

        // Print Exception
        if (ex != null) {
            try {
                if (LOG_TO_FILE_ENABLED) {
                    if (out == null) {
                        out = new PrintStream(logFile);
                    }
                    ex.printStackTrace(out);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void print(String s) {
        // TODO: Add appropriate stream based on your platform
        try {
            if (LOG_TO_FILE_ENABLED) {
                if (out == null) {
                    out = new PrintStream(logFile);
                }
                out.print(s);
                out.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //System.out.print(s);
    }

    public static void exitLogger() {
        if (out != null) {
            out.close();
        }
    }
}
