package uk.co.alt236.apkdetails;

import uk.co.alt236.apkdetails.decoder.ManifestParser;
import uk.co.alt236.apkdetails.model.ApkFile;
import uk.co.alt236.apkdetails.model.Manifest;
import uk.co.alt236.apkdetails.print.SectionedPrinter;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        final String source = "/home/alex/tmp/test_apk/";
        final List<String> files = new ApkFileFilter().getFiles(source);

        System.out.println("APK Files: " + files.size());

        for (final String apkFile : files) {
            final SectionedPrinter printer = new SectionedPrinter();

            printer.addSectionLine();
            appendFileInfo(printer, apkFile);
            printer.addNewLine();
            appendManifestInfo(printer, apkFile);
            printer.print();
        }
    }

    private static void appendFileInfo(final SectionedPrinter kvPrinter, final String file) {
        kvPrinter.add("File Info");
        kvPrinter.startKeyValueSection();
        final ApkFile apkFile = new ApkFile(file);

        kvPrinter.startKeyValueSection();
        kvPrinter.addKv("APK", apkFile.getPath());
        kvPrinter.addKv("MD5", apkFile.getMd5());
        kvPrinter.addKv("SHA1", apkFile.getSha1());
        kvPrinter.endKeyValueSection();
    }

    private static void appendManifestInfo(final SectionedPrinter kvPrinter, final String apkFile) {
        kvPrinter.add("Manifest Info");
        kvPrinter.startKeyValueSection();
        try {
            final ManifestParser parser = new ManifestParser(apkFile);
            final Manifest manifest = parser.parse();
            kvPrinter.addKv("Application Id", manifest.getApplicationId());
            kvPrinter.addKv("Version Name", manifest.getVersionName());
            kvPrinter.addKv("Version Code", manifest.getVersionCode());
            kvPrinter.addKv("Minimum SDK", manifest.getMinSdkVersion());
            kvPrinter.addKv("Compile SDK", manifest.getTargetSdkVersion());
            kvPrinter.addKv("Build SDK", manifest.getPlatformBuildSdkVersion());
            kvPrinter.addKv("Debuggable", manifest.isDebuggable());

            //System.out.println(manifest.getXml());
        } catch (Exception e) {
            kvPrinter.addKv("Parsing Error", e.getMessage());
        }

        kvPrinter.endKeyValueSection();
    }
}
