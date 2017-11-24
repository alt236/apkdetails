package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.decoder.ManifestParser;
import uk.co.alt236.apkdetails.model.AndroidManifest;
import uk.co.alt236.apkdetails.print.Coloriser;
import uk.co.alt236.apkdetails.print.section.SectionedKvPrinter;

import java.io.File;
import java.util.List;

public class ManifestInfoOutput implements Output {
    private boolean verbose;

    public ManifestInfoOutput(final boolean verbose) {
        this.verbose = verbose;
    }

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
            printer.addKv("Minimum SDK", coloriseError(manifest.getMinSdkVersion()));
            printer.addKv("Compile SDK", coloriseError(manifest.getTargetSdkVersion()));
            printer.addKv("Build SDK", coloriseError(manifest.getPlatformBuildSdkVersion()));
            printer.addKv("Debuggable", manifest.isDebuggable());

            printOptionalList(printer, verbose, manifest.getActivities(), "Activities");
            printOptionalList(printer, verbose, manifest.getServices(), "Services");
            printOptionalList(printer, verbose, manifest.getPermissions(), "Permissions");

            //System.out.println(manifest.getXml());
        } catch (Exception e) {
            printer.addKv("Parsing Error", Coloriser.error(e.toString()));
        }

        printer.endKeyValueSection();
    }

    private void printOptionalList(SectionedKvPrinter printer, boolean verbose, List<String> items, String name) {
        printer.addKv(name + " #", items.size());
        if (!items.isEmpty() && verbose) {
            printer.addKv(name, items);
        }
    }

    private String coloriseError(final int input) {
        if (input < 0) {
            return Coloriser.error(input);
        } else {
            return String.valueOf(input);
        }
    }
}
