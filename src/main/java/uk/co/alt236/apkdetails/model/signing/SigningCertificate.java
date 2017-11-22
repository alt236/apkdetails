package uk.co.alt236.apkdetails.model.signing;

import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class SigningCertificate {
    private final X509Certificate cert;

    SigningCertificate(X509Certificate cert) {
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
