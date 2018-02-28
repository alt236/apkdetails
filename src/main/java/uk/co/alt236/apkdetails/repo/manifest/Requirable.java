package uk.co.alt236.apkdetails.repo.manifest;

public class Requirable {
    private final String name;
    private final boolean required;

    public Requirable(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }
}
