package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.decoder.ManifestParser;
import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.AndroidManifestRepository;
import uk.co.alt236.apkdetails.util.Colorizer;

import java.io.File;
import java.util.List;

public class ManifestInfoOutput implements Output {
    private final boolean verbose;
    private final File file;
    private final Colorizer colorizer;

    public ManifestInfoOutput(final File file,
                              final Colorizer colorizer,
                              final boolean verbose) {
        this.file = file;
        this.verbose = verbose;
        this.colorizer = colorizer;
    }

    @Override
    public void output(OutputCollector printer) {
        printer.add("AndroidManifestRepository Info");
        printer.startKeyValueSection();
        try {
            final ManifestParser parser = new ManifestParser(file);
            final AndroidManifestRepository manifest = parser.parse();
            printer.addKv("Application Id", manifest.getApplicationId());
            printer.addKv("Version Name", manifest.getVersionName());
            printer.addKv("Version Code", manifest.getVersionCode());
            printer.addKv("Minimum SDK", colorizeError(manifest.getMinSdkVersion()));
            printer.addKv("Compile SDK", colorizeError(manifest.getTargetSdkVersion()));
            printer.addKv("Build SDK", colorizeError(manifest.getPlatformBuildSdkVersion()));
            printer.addKv("Debuggable", manifest.isDebuggable());

            printOptionalList(printer, verbose, manifest.getActivities(), "Activities");
            printOptionalList(printer, verbose, manifest.getServices(), "Services");
            printOptionalList(printer, verbose, manifest.getUsedPermissions(), "Used Permissions");
            printOptionalList(printer, verbose, manifest.getReceivers(), "Receivers");

            //System.out.println(manifest.getXml());
        } catch (Exception e) {
            printer.addKv("Parsing Error", colorizer.error(e.toString()));
        }

        printer.endKeyValueSection();
    }

    private void printOptionalList(OutputCollector printer, boolean verbose, List<String> items, String name) {
        printer.addKv(name + " #", items.size());
        if (!items.isEmpty() && verbose) {
            printer.addKv(name, items);
        }
    }

    private String colorizeError(final int input) {
        if (input < 0) {
            return colorizer.error(input);
        } else {
            return String.valueOf(input);
        }
    }
}
