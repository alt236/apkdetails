package uk.co.alt236.apkdetails.print;

import uk.co.alt236.apkdetails.print.writer.Writer;
import uk.co.alt236.apkdetails.repo.dex.model.DexClass;

import java.util.List;

public class ClassListPrinter {
    private final boolean verbose;

    public ClassListPrinter(final boolean verbose) {
        this.verbose = verbose;
    }

    public void print(final Writer writer,
                      final List<DexClass> dexClasses) {

        for (final DexClass dexClass : dexClasses) {
            if (verbose) {
                writer.outln(dexClass.getDexFileName() + ", " + formatClass(dexClass));
            } else {
                writer.outln(formatClass(dexClass));
            }

        }

        writer.close();
    }

    private String formatClass(final DexClass dexClass) {
        String retVal = dexClass.getType();

        if (retVal.startsWith("L")) {
            retVal = retVal.substring(1);
        }

        if (retVal.endsWith(";")) {
            retVal = retVal.substring(0, retVal.length() - 1);
        }

        return retVal + ".class";

    }
}
