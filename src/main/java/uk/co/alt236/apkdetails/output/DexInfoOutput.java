package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.model.DexContents;
import uk.co.alt236.apkdetails.model.common.ZipContents;
import uk.co.alt236.apkdetails.print.section.SectionedKvPrinter;

public class DexInfoOutput implements Output {
    private final ZipContents zipContents;

    public DexInfoOutput(final ZipContents zipContents) {
        this.zipContents = zipContents;
    }

    @Override
    public void output(SectionedKvPrinter printer) {
        final DexContents dexContents = new DexContents(zipContents);

        printer.add("Dex Contents");
        printer.startKeyValueSection();
        printer.addKv("Dex Files", dexContents.getDexFiles().size());
        printer.addKv("Dex Classes", dexContents.getTotalClasses());
        printer.addKv("Dex Methods", dexContents.getTotalMethods());
        printer.addKv("Dex Strings", dexContents.getTotalStrings());
        printer.endKeyValueSection();
    }
}
