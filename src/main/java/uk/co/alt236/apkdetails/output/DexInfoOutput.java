package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.DexRepository;
import uk.co.alt236.apkdetails.repo.common.ZipContents;

public class DexInfoOutput implements Output {
    private final ZipContents zipContents;

    public DexInfoOutput(final ZipContents zipContents) {
        this.zipContents = zipContents;
    }

    @Override
    public void output(OutputCollector printer) {
        final DexRepository dexContents = new DexRepository(zipContents);

        printer.add("Dex Contents");
        printer.startKeyValueSection();
        printer.addKv("Dex Files", dexContents.getDexFiles().size());
        printer.addKv("Dex Classes", dexContents.getTotalClasses());
        printer.addKv("Dex Methods", dexContents.getTotalMethods());
        printer.addKv("Dex Strings", dexContents.getTotalStrings());
        printer.endKeyValueSection();
    }
}
