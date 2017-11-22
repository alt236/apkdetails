package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.decoder.ManifestParser;
import uk.co.alt236.apkdetails.model.AndroidManifest;
import uk.co.alt236.apkdetails.print.SectionedKvPrinter;

import java.io.File;

public class ManifestInfoOutput implements Output {

    @Override
    public void output(SectionedKvPrinter printer, File file) {
        printer.add("AndroidManifest Info");
        printer.startKeyValueSection();
        try {
            final ManifestParser parser = new ManifestParser(file);
            final AndroidManifest manifest = parser.parse();
            printer.addKv("Application Id", manifest.getApplicationId());
            printer.addKv("Version Name", manifest.getVersionName());
            printer.addKv("Version Code", manifest.getVersionCode());
            printer.addKv("Minimum SDK", manifest.getMinSdkVersion());
            printer.addKv("Compile SDK", manifest.getTargetSdkVersion());
            printer.addKv("Build SDK", manifest.getPlatformBuildSdkVersion());
            printer.addKv("Debuggable", manifest.isDebuggable());

            //System.out.println(manifest.getXml());
        } catch (Exception e) {
            printer.addKv("Parsing Error", e.getMessage());
        }

        printer.endKeyValueSection();
    }
}
