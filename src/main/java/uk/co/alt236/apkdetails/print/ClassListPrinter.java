package uk.co.alt236.apkdetails.print;

import uk.co.alt236.apkdetails.print.file.FileWriter;
import uk.co.alt236.apkdetails.repo.dex.model.DexClass;

import java.util.List;

public class ClassListPrinter {


    public void print(final FileWriter fileWriter,
                      final List<DexClass> dexClasses) {

        for (final DexClass dexClass : dexClasses) {
            fileWriter.outln(formatLine(dexClass));
        }

        fileWriter.close();
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
