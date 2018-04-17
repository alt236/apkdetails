package uk.co.alt236.apkdetails.output.sections;

import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.common.ZipContents;
import uk.co.alt236.apkdetails.repo.dex.DexRepository;

public class DexInfoOutput implements Output {
    private final DexRepository repo;

    public DexInfoOutput(final ZipContents zipContents) {
        this(new DexRepository(zipContents));
    }

    public DexInfoOutput(final DexRepository repo) {
        this.repo = repo;
    }

    @Override
    public void output(OutputCollector printer) {
        final long dexClasses = repo.getTotalClassCount();
        final long anonClasses = repo.getAnonymousClassCount();
        final long lambdaClasses = repo.getLambdaClassCount();

        printer.add("Dex Contents");
        printer.startKeyValueSection();
        printer.addKv("Files", repo.getDexFiles().size());
        printer.addKv("Classes", dexClasses);
        printer.addKv("Anon Classes", anonClasses + "/" + dexClasses);
        printer.addKv("Lambdas Classes", lambdaClasses + "/" + anonClasses);
        printer.addKv("Methods", repo.getTotalMethodCount());
        printer.addKv("Proto", repo.getTotalProtoCount());
        printer.addKv("Fields", repo.getTotalFieldCount());
        printer.addKv("Strings", repo.getTotalStringCount());
        printer.endKeyValueSection();
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.DEX_INFO;
    }
}
