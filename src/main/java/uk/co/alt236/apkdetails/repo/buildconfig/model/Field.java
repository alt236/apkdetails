package uk.co.alt236.apkdetails.repo.buildconfig.model;

public class Field {
    private final String name;
    private final String value;

    public Field(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
