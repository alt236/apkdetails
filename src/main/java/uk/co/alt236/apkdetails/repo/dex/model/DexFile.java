package uk.co.alt236.apkdetails.repo.dex.model;

import org.jf.dexlib2.dexbacked.DexBackedDexFile;

import java.util.Set;

public class DexFile {

    private final long methodCount;
    private final long classCount;
    private final long fieldCount;
    private final long stringCount;
    private final long protoCount;
    private final Set<DexClass> classes;
    private final long fileSize;
    private final String name;

    public DexFile(DexBackedDexFile dexBackedFile,
                   String name,
                   long fileSize) {
        this.methodCount = dexBackedFile.getMethodCount();
        this.classCount = dexBackedFile.getClassCount();
        this.fieldCount = dexBackedFile.getFieldCount();
        this.stringCount = dexBackedFile.getStringCount();
        this.protoCount = dexBackedFile.getProtoCount();
        this.classes = DexClass.getClasses(name, dexBackedFile.getClasses());
        this.fileSize = fileSize;
        this.name = name;
    }

    public long getMethodCount() {
        return methodCount;
    }

    public long getClassCount() {
        return classCount;
    }

    public long getFieldCount() {
        return fieldCount;
    }

    public long getStringCount() {
        return stringCount;
    }

    public Set<DexClass> getClasses() {
        return classes;
    }

    public String getName() {
        return name;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getProtoCount() {
        return protoCount;
    }
}
