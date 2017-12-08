package uk.co.alt236.apkdetails.output.sections;

import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.manifest.AndroidManifestRepository;
import uk.co.alt236.apkdetails.util.Colorizer;

import java.util.List;

public class ManifestInfoOutput implements Output {
    private final boolean verbose;
    private final AndroidManifestRepository repository;
    private final Colorizer colorizer;

    public ManifestInfoOutput(final AndroidManifestRepository repository,
                              final Colorizer colorizer,
                              final boolean verbose) {
        this.repository = repository;
        this.verbose = verbose;
        this.colorizer = colorizer;
    }

    @Override
    public void output(OutputCollector printer) {
        printer.add("Manifest Info");
        printer.startKeyValueSection();
        try {
            printer.addKv("Application Id", repository.getApplicationId());
            printer.addKv("Version Name", repository.getVersionName());
            printer.addKv("Version Code", repository.getVersionCode());
            printer.addKv("Minimum SDK", colorizeError(repository.getMinSdkVersion()));
            printer.addKv("Compile SDK", colorizeError(repository.getTargetSdkVersion()));
            printer.addKv("Build SDK", colorizeError(repository.getPlatformBuildSdkVersion()));
            printer.addKv("Debuggable", repository.isDebuggable());

            printOptionalList(printer, verbose, repository.getActivities(), "Activities");
            printOptionalList(printer, verbose, repository.getServices(), "Services");
            printOptionalList(printer, verbose, repository.getUsedPermissions(), "Used Permissions");
            printOptionalList(printer, verbose, repository.getReceivers(), "Receivers");
            printOptionalList(printer, verbose, repository.getProviders(), "Providers");

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
