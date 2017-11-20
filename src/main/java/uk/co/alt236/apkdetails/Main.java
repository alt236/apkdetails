package uk.co.alt236.apkdetails;

import uk.co.alt236.apkdetails.decoder.ManifestParser;
import uk.co.alt236.apkdetails.model.Manifest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        final String source = "/home/alex/tmp/test_apk/";
        final List<String> files = new ApkFileFilter().getFiles(source);


        System.out.println("APK Files: " + files.size());

        for (final String apkFile : files) {
            final ManifestParser parser = new ManifestParser(apkFile);

            try {
                System.out.println("------------------------");
                System.out.println(" APK: " + apkFile);
                System.out.println("------------------------");
                final Manifest manifest = parser.parse();
                final KVPrinter kvPrinter = new KVPrinter();

                kvPrinter.add("Application ID", manifest.getApplicationId());
                kvPrinter.add("Version Code", manifest.getVersionCode());
                kvPrinter.add("Version Name", manifest.getVersionName());
                kvPrinter.add("Min SDK", manifest.getMinSdkVersion());
                kvPrinter.add("Target SDK", manifest.getTargetSdkVersion());
                kvPrinter.add("Debuggable", manifest.isDebuggable());
                kvPrinter.print();
                System.out.println();
                System.out.println(manifest.getXml());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static class KVPrinter {
        private final List<String> keys;
        private final List<String> values;

        public KVPrinter() {
            keys = new ArrayList<>();
            values = new ArrayList<>();
        }

        public void add(String key, String value) {
            keys.add(key);
            values.add(value);
        }

        public void print() {
            final Optional<String> maxString = keys.stream().max(Comparator.comparingInt(String::length));
            final int maxStringSize = maxString.get().length();

            for (int i = 0; i < keys.size(); i++) {
                final String key = keys.get(i);
                final String value = values.get(i);

                System.out.println(padRight(key, maxStringSize) + ": " + value);
            }
        }

        private static String padRight(String s, int n) {
            return String.format("%1$-" + n + "s", s);
        }
    }
}
