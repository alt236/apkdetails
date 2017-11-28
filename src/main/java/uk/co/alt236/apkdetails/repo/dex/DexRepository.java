package uk.co.alt236.apkdetails.repo.dex;

import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.DexBackedClassDef;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import uk.co.alt236.apkdetails.repo.common.Entry;
import uk.co.alt236.apkdetails.repo.common.ZipContents;
import uk.co.alt236.apkdetails.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DexRepository {
    private final ZipContents zipContents;
    private final List<DexBackedDexFile> dexFiles;
    private final DexClassInfo dexClassInfo;

    public DexRepository(final ZipContents zipContents) {
        this.dexClassInfo = new DexClassInfo();
        this.zipContents = zipContents;
        this.dexFiles = new ArrayList<>();
    }

    public List<DexBackedDexFile> getDexFiles() {
        loadDexFiles();
        return Collections.unmodifiableList(dexFiles);
    }

    public long getTotalMethodCount() {
        loadDexFiles();
        return dexFiles.stream().mapToLong(DexBackedDexFile::getMethodCount).sum();
    }

    public long getTotalClassCount() {
        loadDexFiles();
        return dexFiles.stream().mapToLong(DexBackedDexFile::getClassCount).sum();
    }

    public long getTotalFieldCount() {
        loadDexFiles();
        return dexFiles.stream().mapToLong(DexBackedDexFile::getFieldCount).sum();
    }

    public long getAnonymousClassCount() {
        loadDexFiles();
        long count = 0;
        for (final DexBackedDexFile dexFile : dexFiles) {
            for (final DexBackedClassDef clazz : dexFile.getClasses()) {
                if (dexClassInfo.isInnerClass(clazz)) {
                    count++;
                }
            }
        }

        return count;
    }

    public long getTotalStringCount() {
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
