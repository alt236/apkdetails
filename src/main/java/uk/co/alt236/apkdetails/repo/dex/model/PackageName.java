package uk.co.alt236.apkdetails.repo.dex.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PackageName {

    final String packageName;
    final String[] packageNameParts;
    private String leaf;

    public PackageName(final String packageName) {
        this.packageName = packageName;
        this.packageNameParts = packageName.split("\\.");
    }

    public PackageName getParent() {
        if (packageNameParts.length > 0) {
            final String[] newArray = Arrays.copyOfRange(packageNameParts, 0, packageNameParts.length - 1);
            return toPackageName(newArray);
        } else {
            return new PackageName("");
        }
    }

    public List<PackageName> getParents() {
        final List<PackageName> retVal = new ArrayList<>();

        for (int i = 0; i < packageNameParts.length; i++) {
            final String[] newArray = Arrays.copyOfRange(packageNameParts, 0, i);
            final PackageName newPackageName = toPackageName(newArray);
            retVal.add(newPackageName);
        }
        return retVal;
    }

    public PackageName getLeaf() {
        if (packageNameParts.length > 0) {
            return new PackageName(packageNameParts[packageNameParts.length - 1]);
        } else {
            return new PackageName("");
        }
    }

    public PackageName getRoot() {
        if (packageNameParts.length > 0) {
            return new PackageName(packageNameParts[0]);
        } else {
            return new PackageName("");
        }
    }

    @Override
    public String toString() {
        return packageName;
    }

    private PackageName toPackageName(final String... parts) {
        final StringBuilder sb = new StringBuilder();
        for (final String part : parts) {
            if (sb.length() > 0) {
                sb.append('.');
            }
            sb.append(part);
        }
        return new PackageName(sb.toString());
    }
}
