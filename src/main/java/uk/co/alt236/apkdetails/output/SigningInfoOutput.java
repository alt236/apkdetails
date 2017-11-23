package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.model.signing.SignatureInfo;
import uk.co.alt236.apkdetails.model.signing.SignatureStatus;
import uk.co.alt236.apkdetails.model.signing.SigningCertificate;
import uk.co.alt236.apkdetails.model.signing.ValidationResult;
import uk.co.alt236.apkdetails.print.Coloriser;
import uk.co.alt236.apkdetails.print.section.SectionedKvPrinter;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class SigningInfoOutput implements Output {

    @Override
    public void output(SectionedKvPrinter printer, File file) {
        final SignatureInfo signatureInfo = new SignatureInfo(file);

        final List<SigningCertificate> certificates = signatureInfo.getCertificates();
        final String validationStatus = getValidationStatus(signatureInfo.validateSignature());

        printer.add("Signature Info");
        printer.startKeyValueSection();

        printer.addKv("Signature status", validationStatus);
        printer.addKv("Certificates", certificates.size());
        int count = 0;
        for (final SigningCertificate certificate : certificates) {
            count++;

            final String prefix = "Certificate " + count + " ";
            printer.addKv(prefix + "Subject", certificate.getSubjectDN().toString());
            printer.addKv(prefix + "Issuer", certificate.getIssuerDN().toString());
            printer.addKv(prefix + "Validity", certificate.getNotBefore() + " to " + certificate.getNotAfter());
            printer.addKv(prefix + "Algorithm", certificate.getSigAlgName());
            printer.addKv(prefix + "Serial", certificate.getSerialNumber().toString());
            printer.addKv(prefix + "MD5 Thumb", certificate.getMd5Thumbprint());
            printer.addKv(prefix + "SHA1 Thumb", certificate.getSha1Thumbprint());
        }

        printer.endKeyValueSection();
    }

    private String getValidationStatus(ValidationResult validationResult) {
        final String retVal;

        final String status = validationResult.getSignatureStatus().toString().toLowerCase(Locale.US);
        if (validationResult.getSignatureStatus() == SignatureStatus.INVALID) {
            retVal = Coloriser.error(status + ". Failed entries: " + validationResult.getFailedEntries().size());
        } else {
            retVal = status;
        }

        return retVal;
    }
}
