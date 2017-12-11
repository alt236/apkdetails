package uk.co.alt236.apkdetails.repo.dex.model;

import org.jf.dexlib2.dexbacked.DexBackedClassDef;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DexClass {

    private final boolean innerClass;
    private final boolean lambdaClass;
    private final String name;
    private final String type;
    private final String superType;
    private final PackageName packageName;

    private DexClass(final DexBackedClassDef classDef) {
        final DexClassInfo dexClassInfo = new DexClassInfo();
        this.name = dexClassInfo.getClassSimpleName(classDef);
        this.type = classDef.getType();
        this.superType = classDef.getSuperclass();
        this.innerClass = dexClassInfo.isInnerClass(classDef);
        this.lambdaClass = dexClassInfo.isLambda(classDef);
        this.packageName = new PackageName(dexClassInfo.getPackageName(classDef));
    }

    static Set<DexClass> getClasses(final Set<? extends DexBackedClassDef> classes) {
        final Set<DexClass> retVal = new HashSet<>(classes.size());

        for (final DexBackedClassDef classDef : classes) {
            retVal.add(new DexClass(classDef));
        }

        return Collections.unmodifiableSet(retVal);
    }

    public String getType() {
        return type;
    }

    public String getSuperType() {
        return superType;
    }

    public boolean isInnerClass() {
        return innerClass;
    }

    public boolean isLambda() {
        return lambdaClass;
    }

    public String getSimpleName() {
        return name;
    }

    public PackageName getPackageName() {
        return packageName;
    }
}
