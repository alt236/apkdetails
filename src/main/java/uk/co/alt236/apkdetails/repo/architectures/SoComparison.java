package uk.co.alt236.apkdetails.repo.architectures;

import java.util.Set;

public class SoComparison {
    private final String soFileName;
    private final Set<String> architecturesPresent;

    public SoComparison(String soFileName, Set<String> architecturesPresent) {
        this.soFileName = soFileName;
        this.architecturesPresent = architecturesPresent;
    }

    public String getSoLibrary() {
        return soFileName;
    }

    public Set<String> getArchitectures() {
        return architecturesPresent;
    }

    @Override
    public String toString() {
        return "SoComparison{" +
                "soFileName='" + soFileName + '\'' +
                ", architecturesPresent=" + architecturesPresent +
                '}';
    }
}
