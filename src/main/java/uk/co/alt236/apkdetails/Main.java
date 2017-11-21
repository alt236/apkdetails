package uk.co.alt236.apkdetails;

import uk.co.alt236.apkdetails.decoder.ManifestParser;
import uk.co.alt236.apkdetails.model.Manifest;

import java.util.List;

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

                kvPrinter.add("Application Id", manifest.getApplicationId());
                kvPrinter.add("Version Name", manifest.getVersionName());
                kvPrinter.add("Version Code", manifest.getVersionCode());
                kvPrinter.add("Min SDK", manifest.getMinSdkVersion());
                kvPrinter.add("Target SDK", manifest.getTargetSdkVersion());
                kvPrinter.add("Debuggable", manifest.isDebuggable());
                kvPrinter.print();
                System.out.println();
                //System.out.println(manifest.getXml());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
