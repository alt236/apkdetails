package uk.co.alt236.apkdetails.util.date;

import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.TimeZone;

public class UtcDateFormatter extends java.text.SimpleDateFormat {
    private static final String TIME_ZONE_STRING = "UTC";
    private static final TimeZone TIME_ZONE_UTC = TimeZone.getTimeZone(TIME_ZONE_STRING);

    public UtcDateFormatter(final String template) {
        super(template);
        super.setTimeZone(TIME_ZONE_UTC);
    }

    public UtcDateFormatter(final String template, final DateFormatSymbols symbols) {
        super(template, symbols);
        super.setTimeZone(TIME_ZONE_UTC);
    }

    public UtcDateFormatter(final String template, final Locale locale) {
        super(template, locale);
        super.setTimeZone(TIME_ZONE_UTC);
    }

    /*
     * This function will throw an UnsupportedOperationException.
     * You are not be able to change the TimeZone of this object
      *
      * (non-Javadoc)
     * @see java.text.DateFormat#setTimeZone(java.util.TimeZone)
     */
    @Override
    public void setTimeZone(final TimeZone timezone) {
        if (!TIME_ZONE_STRING.equals(timezone.getID())) {
            throw new UnsupportedOperationException("This SimpleDateFormat can only be in " + TIME_ZONE_STRING);
        }
    }
}