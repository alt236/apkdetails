package uk.co.alt236.apkdetails.model;

import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import uk.co.alt236.apkdetails.model.common.Entry;
import uk.co.alt236.apkdetails.model.common.ZipContents;
import uk.co.alt236.apkdetails.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DexContents {
    private final ZipContents zipContents;
    private final List<DexBackedDexFile> dexFiles;

    public DexContents(final ZipContents zipContents) {
        this.zipContents = zipContents;
        this.dexFiles = new ArrayList<>();
    }

    public List<DexBackedDexFile> getDexFiles() {
        loadDexFiles();
        return Collections.unmodifiableList(dexFiles);
    }

    public long getTotalMethods() {
        loadDexFiles();
        return dexFiles.stream().mapToLong(DexBackedDexFile::getMethodCount).sum();
    }

    public long getTotalClasses() {
        loadDexFiles();
        return dexFiles.stream().mapToLong(DexBackedDexFile::getClassCount).sum();
    }

    public long getTotalStrings() {
        loadDexFiles();
        return dexFiles.stream().mapToLong(DexBackedDexFile::getStringCount).sum();
    }

    private void loadDexFiles() {
        if (!dexFiles.isEmpty()) {
            return;
        }

        final List<Entry> entries = getDexFileEntries();
        for (final Entry entry : entries) {
            final InputStream is = zipContents.getInputStream(entry);
            try {
                final DexBackedDexFile dexFile = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), is);
                dexFiles.add(dexFile);
            } catch (final IOException e) {
                e.printStackTrace();
            }
            StreamUtils.close(is);
        }
    }


    private List<Entry> getDexFileEntries() {
        return zipContents
                .getEntries(entry -> !entry.isDirectory() && entry.getName().toLowerCase(Locale.US).endsWith(".dex"));
    }
}
