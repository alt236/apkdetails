package uk.co.alt236.apkdetails.repo.signing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SignatureRepository {
    private final File file;
    private final List<SigningCertificate> certList;

    public SignatureRepository(File file) {
        this.file = file;
        this.certList = new ArrayList<>();
    }

    public List<SigningCertificate> getCertificates() {
        loadCertificates();
        return Collections.unmodifiableList(certList);
    }

    private void loadCertificates() {
        if (!certList.isEmpty()) {
            return;
        }

        final List<SigningCertificate> retVal = new ArrayList<>();

        try (final ZipFile zipFile = new ZipFile(file)) {
            final Enumeration<? extends ZipEntry> iterator = zipFile.entries();
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
                for (final Certificate certificate : cp.getCertificates()) {
                    retVal.add(new SigningCertificate((X509Certificate) certificate));
                }
                is.close();
            }
        } catch (IOException | CertificateException e) {
            e.printStackTrace();
        }

        certList.addAll(retVal);
    }


    public ValidationResult validateSignature() {
        return new SignatureValidator().validateSignature(file);
    }
}
