package uk.co.alt236.apkdetails.repo.signing;

import uk.co.alt236.apkdetails.util.Hasher;

import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class SigningCertificate {
    private final X509Certificate cert;
    private final Hasher hasher;

    SigningCertificate(X509Certificate cert) {
        this.cert = cert;
        this.hasher = new Hasher();
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
            return hasher.md5Hex(cert.getEncoded());
        } catch (CertificateEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public String getSha1Thumbprint() {
        try {
            return hasher.sha1Hex(cert.getEncoded());
        } catch (CertificateEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public String getSha256Thumbprint() {
        try {
            return hasher.sha256Hex(cert.getEncoded());
        } catch (CertificateEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
