package uk.co.alt236.apkdetails.repo.buildconfig.model;

import java.util.Collections;
import java.util.List;

public class BuildConfigFile {
    private final String type;
    private final List<Field> fields;

    public BuildConfigFile(String type,
                           List<Field> fields) {
        this.type = type;
        this.fields = Collections.unmodifiableList(fields);
    }

    public String getType() {
        return type;
    }

    public List<Field> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return "BuildConfigFile{" +
                "type='" + type + '\'' +
                ", fields=" + fields +
                '}';
    }
}
