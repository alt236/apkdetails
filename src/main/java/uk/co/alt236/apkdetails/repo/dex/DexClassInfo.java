package uk.co.alt236.apkdetails.repo.dex;

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
}
