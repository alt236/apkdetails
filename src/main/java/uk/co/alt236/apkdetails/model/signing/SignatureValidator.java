package uk.co.alt236.apkdetails.model.signing;

import uk.co.alt236.apkdetails.model.common.Entry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

class SignatureValidator {

    private static List<Entry> getInvalidEntries(JarFile jar) throws IOException {
        final Enumeration<JarEntry> entries = jar.entries();
        final List<Entry> invalidEntries = new ArrayList<>();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            try (final InputStream is = jar.getInputStream(entry)) {
                final byte[] buffer = new byte[8192];
                //noinspection StatementWithEmptyBody
                while ((is.read(buffer, 0, buffer.length)) != -1) {
                    // We just read. This will throw a SecurityException
                    // if a signature/digest check fails.
                }
            } catch (SecurityException se) {
                invalidEntries.add(new Entry(entry));
            }
        }
        return Collections.unmodifiableList(invalidEntries);
    }

    public ValidationResult validateSignature(final File file) {
        JarFile jar;
        try {
            jar = new JarFile(file);
            final ZipEntry entry = jar.getEntry("META-INF/MANIFEST.MF");
            if (entry == null) {
                return new ValidationResult(SignatureStatus.ABSENT);
            } else {
                final List<Entry> result = getInvalidEntries(jar);
                final SignatureStatus signatureStatusStatus = result.isEmpty()
                        ? SignatureStatus.VALID
                        : SignatureStatus.INVALID;
                return new ValidationResult(signatureStatusStatus, result);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ValidationResult(SignatureStatus.ERROR);
        }
    }
}
