package uk.co.alt236.apkdetails.resources;

import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Strings extends ResourceBundle {

    private final PropertyResourceBundle bundle;

    public Strings() {
        bundle = (PropertyResourceBundle) ResourceBundle.getBundle("strings");
    }

    @Override
    protected Object handleGetObject(String s) {
        return bundle.handleGetObject(s);
    }

    @Override
    public Enumeration<String> getKeys() {
        return bundle.getKeys();
    }

    public String getString(String s, final Object... values) {
        final String string = getString(s);

        return String.format(getLocale(), string, values);
    }
}
