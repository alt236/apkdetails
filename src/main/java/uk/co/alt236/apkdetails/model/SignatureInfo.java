package uk.co.alt236.apkdetails.model;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SignatureInfo {
    private final String path;

    public SignatureInfo(String path) {
        this.path = path;
    }

    private List<? extends Certificate> loadCertificates() {
        final List<? extends Certificate> retVal = new ArrayList<>();

        try (final ZipFile zipFile = new ZipFile(path)) {
            final Enumeration<ZipEntry> iterator = (Enumeration<ZipEntry>) zipFile.entries();
            final List<ZipEntry> certEntries = new ArrayList<>();

            while (iterator.hasMoreElements()) {
                final ZipEntry ze = iterator.nextElement();
                final String name = ze.getName();
                if (name.startsWith("META-INF/") && (name.endsWith(".RSA") || name.endsWith(".DSA"))) {
                    certEntries.add(ze);
                }
            }

            for (final ZipEntry zipEntry : certEntries) {
                final InputStream is = zipFile.getInputStream(zipEntry);
                final CertificateFactory factory = CertificateFactory.getInstance("X.509");
                final CertPath cp = factory.generateCertPath(is, "PKCS7");
                retVal.addAll(new ArrayList(cp.getCertificates()));
                is.close();
            }
        } catch (IOException | CertificateException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public List<? extends Certificate> getCertificates() {
        return loadCertificates();
    }
}
