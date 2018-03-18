package uk.co.alt236.apkdetails.repo.buildconfig;

import org.jf.dexlib2.dexbacked.DexBackedClassDef;
import org.jf.dexlib2.dexbacked.DexBackedField;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class BuildConfigDetector {
    private static final String[] FIELD_NAMES =
            {"APPLICATION_ID", "BUILD_TYPE", "DEBUG", "FLAVOR", "VERSION_CODE", "VERSION_NAME"};
    private static final Set<String> FIELD_NAME_SET = new HashSet<>(Arrays.asList(FIELD_NAMES));
    private static final int THRESHOLD = FIELD_NAME_SET.size();

    public boolean isBuildConfigFile(final DexBackedClassDef classDef) {
        return hasBuildConfigClassName(classDef) && (hasBuildConfigClassName(classDef) || hasBuildConfigFields(classDef));
    }

    private boolean hasBuildConfigClassName(DexBackedClassDef classDef) {
        // TODO: this will break with obfuscation
        return classDef.getType().toLowerCase().endsWith("/buildconfig;");
    }

    private boolean hasBuildConfigFields(DexBackedClassDef classDef) {
        int count = 0;

        for (final DexBackedField field : classDef.getStaticFields()) {
            if (FIELD_NAME_SET.contains(field.getName())) {
                count++;
            }

            if (count == THRESHOLD) {
                break;
            }
        }

        return count == THRESHOLD;
    }

}
