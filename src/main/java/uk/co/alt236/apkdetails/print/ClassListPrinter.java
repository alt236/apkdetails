package uk.co.alt236.apkdetails.print;

import uk.co.alt236.apkdetails.print.writer.Writer;
import uk.co.alt236.apkdetails.repo.dex.model.DexClass;

import java.util.List;

public class ClassListPrinter {


    public void print(final Writer writer,
                      final List<DexClass> dexClasses) {

        for (final DexClass dexClass : dexClasses) {
            writer.outln(formatLine(dexClass));
        }

        writer.close();
    }

    private String formatLine(final DexClass dexClass) {
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
