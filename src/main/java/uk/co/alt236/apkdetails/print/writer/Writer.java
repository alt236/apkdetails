package uk.co.alt236.apkdetails.print.writer;

public interface Writer {
    void outln(String string);

    void errln(String string);

    void close();
}
