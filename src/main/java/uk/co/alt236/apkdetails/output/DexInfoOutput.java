package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.common.ZipContents;
import uk.co.alt236.apkdetails.repo.dex.DexRepository;

public class DexInfoOutput implements Output {
    private final ZipContents zipContents;

    public DexInfoOutput(final ZipContents zipContents) {
        this.zipContents = zipContents;
    }

    @Override
    public void output(OutputCollector printer) {
        final DexRepository dexContents = new DexRepository(zipContents);
        final long dexClasses = dexContents.getTotalClassCount();
        final long anonClasses = dexContents.getAnonymousClassCount();
        printer.add("Dex Contents");
        printer.startKeyValueSection();
        printer.addKv("Dex Files", dexContents.getDexFiles().size());
        printer.addKv("Dex Classes", dexClasses);
        printer.addKv("Dex Anon Classes", anonClasses + "/" + dexClasses);
        printer.addKv("Dex Methods", dexContents.getTotalMethods());
        printer.addKv("Dex Strings", dexContents.getTotalStrings());
        printer.endKeyValueSection();
    }
}
