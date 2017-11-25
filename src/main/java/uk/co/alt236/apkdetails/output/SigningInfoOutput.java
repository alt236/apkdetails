package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.model.signing.SignatureInfo;
import uk.co.alt236.apkdetails.model.signing.SignatureStatus;
import uk.co.alt236.apkdetails.model.signing.SigningCertificate;
import uk.co.alt236.apkdetails.model.signing.ValidationResult;
import uk.co.alt236.apkdetails.print.Coloriser;
import uk.co.alt236.apkdetails.print.section.SectionedKvPrinter;
import uk.co.alt236.apkdetails.util.date.IsoISO8601DateParser;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SigningInfoOutput implements Output {
    private final File file;

    public SigningInfoOutput(File file) {
        this.file = file;
    }

    @Override
    public void output(SectionedKvPrinter printer) {
        final SignatureInfo signatureInfo = new SignatureInfo(file);

        final List<SigningCertificate> certificates = signatureInfo.getCertificates();
        final ValidationResult validationResult = signatureInfo.validateSignature();
        final String validationStatus = getValidationStatus(validationResult);

        printer.add("Signature Info");
        printer.startKeyValueSection();

        printer.addKv("Signature status", validationStatus);
        if (!validationResult.getFailedEntries().isEmpty()) {
            printer.addKv("Failed entries", getFailedEntries(validationResult));
        }
        printer.addKv("Certificates", certificates.size());
        int count = 0;
        for (final SigningCertificate certificate : certificates) {
            count++;

            final String prefix = "Certificate " + count + " ";
            printer.addKv(prefix + "Subject", certificate.getSubjectDN().toString());
            printer.addKv(prefix + "Issuer", certificate.getIssuerDN().toString());
            printer.addKv(prefix + "Valid from", IsoISO8601DateParser.toIsoDateString(certificate.getNotBefore()));
            printer.addKv(prefix + "Valid to", IsoISO8601DateParser.toIsoDateString(certificate.getNotAfter()));
            printer.addKv(prefix + "Algorithm", certificate.getSigAlgName());
            printer.addKv(prefix + "Serial", certificate.getSerialNumber().toString());
            printer.addKv(prefix + "MD5 Thumb", certificate.getMd5Thumbprint());
            printer.addKv(prefix + "SHA1 Thumb", certificate.getSha1Thumbprint());
        }

        printer.endKeyValueSection();
    }

    private List<String> getFailedEntries(ValidationResult validationResult) {
        return validationResult
                .getFailedEntries()
                .stream()
                .map(entry -> Coloriser.error(entry.getName()))
                .sorted()
                .collect(Collectors.toList());
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
