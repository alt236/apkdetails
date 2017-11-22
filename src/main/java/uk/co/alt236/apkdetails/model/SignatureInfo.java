package uk.co.alt236.apkdetails.model;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SignatureInfo {
    private final String path;

    public SignatureInfo(String path) {
        this.path = path;
    }

    private List<Cert> loadCertificates() {
        final List<Cert> retVal = new ArrayList<>();

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
                for (final Certificate certificate : cp.getCertificates()) {
                    retVal.add(new Cert((X509Certificate) certificate));
                }
                is.close();
            }
        } catch (IOException | CertificateException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public List<Cert> getCertificates() {
        return loadCertificates();
    }

    public static class Cert {
        private final X509Certificate cert;

        private Cert(X509Certificate cert) {
            this.cert = cert;
        }

        public Principal getSubjectDN() {
            return cert.getSubjectDN();
        }

        public Principal getIssuerDN() {
            return cert.getIssuerDN();
        }

        public String getSigAlgName() {
            return cert.getSigAlgName();
        }

        public BigInteger getSerialNumber() {
            return cert.getSerialNumber();
        }

        public Date getNotBefore() {
            return cert.getNotBefore();
        }

        public Date getNotAfter() {
            return cert.getNotAfter();
        }

        public String getMd5Thumbprint() {
            try {
                return DigestUtils.md5Hex(cert.getEncoded());
            } catch (CertificateEncodingException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        public String getSha1Thumbprint() {
            try {
                return DigestUtils.sha1Hex(cert.getEncoded());
            } catch (CertificateEncodingException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }
}
