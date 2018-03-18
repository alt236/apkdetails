package uk.co.alt236.apkdetails.repo.buildconfig;

import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.DexBackedClassDef;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBackedField;
import uk.co.alt236.apkdetails.repo.buildconfig.model.BuildConfigFile;
import uk.co.alt236.apkdetails.repo.buildconfig.model.Field;
import uk.co.alt236.apkdetails.repo.common.Entry;
import uk.co.alt236.apkdetails.repo.common.ZipContents;
import uk.co.alt236.apkdetails.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BuildConfigRepository {

    private final ZipContents zipContents;
    private final List<BuildConfigFile> buildConfigFiles;
    private final DexValueParser dexValueParser;
    private final BuildConfigDetector buildConfigDetector;

    public BuildConfigRepository(final ZipContents zipContents) {
        this.zipContents = zipContents;
        this.buildConfigFiles = new ArrayList<>();
        this.dexValueParser = new DexValueParser();
        this.buildConfigDetector = new BuildConfigDetector();
    }

    public List<BuildConfigFile> getBuildConfigFiles() {
        loadFiles();
        return Collections.unmodifiableList(buildConfigFiles);
    }

    private void loadFiles() {
        if (!buildConfigFiles.isEmpty()) {
            return;
        }

        final List<Entry> entries = getDexFileEntries();
        for (final Entry entry : entries) {
            final InputStream is = zipContents.getInputStream(entry);
            try {
                final DexBackedDexFile dexFile = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), is);
                dexFile.getClasses()
                        .stream()
                        .filter(buildConfigDetector::isBuildConfigFile)
                        .map(this::createBuildConfigFile)
                        .forEachOrdered(buildConfigFile -> buildConfigFiles.add(buildConfigFile));

            } catch (final IOException e) {
                e.printStackTrace();
            }
            StreamUtils.close(is);
        }
    }

    private BuildConfigFile createBuildConfigFile(DexBackedClassDef classDef) {
        final String type = classDef.getType();
        final List<Field> fields = new ArrayList<>();

        for (final DexBackedField dexBackedField : classDef.getFields()) {
            fields.add(new Field(dexBackedField.getName(), dexValueParser.getValueAsString(dexBackedField.initialValue)));
        }


        return new BuildConfigFile(type, fields);
    }

    private List<Entry> getDexFileEntries() {
        return zipContents
                .getEntries(entry -> !entry.isDirectory() && entry.getName().toLowerCase(Locale.US).endsWith(".dex"));
    }
}
