package uk.co.alt236.apkdetails.util.date;

import java.util.Date;

public final class IsoISO8601DateParser {

    private static final String ISO_ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static String toIsoDateString(final Date date) {
        return new UtcDateFormatter(ISO_ISO_8601_DATE_FORMAT).format(date);
    }
}
