package uk.co.alt236.apkdetails.repo.dex.model;

import org.jf.dexlib2.iface.ClassDef;

import java.util.regex.Pattern;

class DexClassInfo {
    // We want to match the following "xxxx$[number];"
    private static final String INNER_CLASS_REGEX = ".*\\$[0-9]+;$";
    private static final String LAMBDA_CLASS_REGEX = ".*\\$\\$Lambda\\$[0-9]+;$";
    private final Pattern innerClassPattern;
    private final Pattern lambdaClassPattern;

    public DexClassInfo() {
        innerClassPattern = Pattern.compile(INNER_CLASS_REGEX);
        lambdaClassPattern = Pattern.compile(LAMBDA_CLASS_REGEX);
    }

    public boolean isInnerClass(final ClassDef clazz) {
        return innerClassPattern.matcher(clazz.getType()).matches();
    }

    public boolean isLambda(final ClassDef clazz) {
        return lambdaClassPattern.matcher(clazz.getType()).matches();
    }


    public String getClassSimpleName(ClassDef classDef) {
        final String type = classDef.getType();
        final int lastSlash = type.lastIndexOf('/');

        final String name;
        if (lastSlash == -1) {
            name = type;
        } else {
            name = type.substring(lastSlash + 1, type.length() - 1);
        }

        return name.endsWith(";") ? name.substring(0, name.length() - 2) : name;
    }

    public String getPackageName(ClassDef classDef) {
        final String type = classDef.getType();
        final int lastSlash = type.lastIndexOf('/');

        final String path;
        if (lastSlash == -1) {
            path = type;
        } else {
            path = type.substring(0, lastSlash);
        }

        final String packagePath = path.replaceAll("/", ".");
        return packagePath.startsWith("L") ? packagePath.substring(1) : path;
    }
}
