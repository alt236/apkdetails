package uk.co.alt236.apkdetails.util;

import java.io.Closeable;
import java.io.IOException;

public final class StreamUtils {
    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
