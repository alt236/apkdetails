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
        final long lambdaClasses = dexContents.getLambdaClassCount();

        printer.add("Dex Contents");
        printer.startKeyValueSection();
        printer.addKv("Files", dexContents.getDexFiles().size());
        printer.addKv("Classes", dexClasses);
        printer.addKv("Anon Classes", anonClasses + "/" + dexClasses);
        printer.addKv("Lambdas Classes", lambdaClasses + "/" + anonClasses);
        printer.addKv("Methods", dexContents.getTotalMethodCount());
        printer.addKv("Proto", dexContents.getTotalProtoCount());
        printer.addKv("Fields", dexContents.getTotalFieldCount());
        printer.addKv("Strings", dexContents.getTotalStringCount());
        printer.endKeyValueSection();
    }
}
