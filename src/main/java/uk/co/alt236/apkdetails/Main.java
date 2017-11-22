package uk.co.alt236.apkdetails;

import uk.co.alt236.apkdetails.decoder.ManifestParser;
import uk.co.alt236.apkdetails.model.AndroidManifest;
import uk.co.alt236.apkdetails.model.ApkContents;
import uk.co.alt236.apkdetails.model.FileInfo;
import uk.co.alt236.apkdetails.model.signing.SignatureInfo;
import uk.co.alt236.apkdetails.model.signing.SignatureStatus;
import uk.co.alt236.apkdetails.model.signing.SigningCertificate;
import uk.co.alt236.apkdetails.model.signing.ValidationResult;
import uk.co.alt236.apkdetails.print.SectionedPrinter;

import java.util.List;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        final String source = "/home/alex/tmp/test_apk/";
        final List<String> files = new ApkFileFilter().getFiles(source);

        System.out.println("APK Files: " + files.size());

        for (final String apkFile : files) {
            final SectionedPrinter printer = new SectionedPrinter();

            printer.addSectionLine();
            appendFileInfo(printer, apkFile);
            printer.addNewLine();
            appendManifestInfo(printer, apkFile);
            printer.addNewLine();
            appendApkInfo(printer, apkFile);
            printer.addNewLine();
            appendSigningInfo(printer, apkFile);
            printer.print();
        }
    }

    private static void appendSigningInfo(SectionedPrinter kvPrinter, String apkFile) {
        final SignatureInfo signatureInfo = new SignatureInfo(apkFile);
        final List<SigningCertificate> certificates = signatureInfo.getCertificates();
        kvPrinter.add("Signature Info");
        kvPrinter.startKeyValueSection();

        final ValidationResult validationResult = signatureInfo.validateSignature();
        final String validationStatus;
        if (validationResult.getSignatureStatus() == SignatureStatus.INVALID) {
            validationStatus = "Invalid. Failed entries: " + validationResult.getFailedEntries().size();
        } else {
            validationStatus = validationResult.getSignatureStatus().toString().toLowerCase(Locale.US);
        }

        kvPrinter.addKv("SignatureStatus", validationStatus);

        kvPrinter.addKv("Certificates", certificates.size());
        int count = 0;
        for (final SigningCertificate certificate : certificates) {
            count++;

            final String prefix = "SigningCertificate " + count + " ";
            kvPrinter.addKv(prefix + "Subject", certificate.getSubjectDN().toString());
            kvPrinter.addKv(prefix + "Issuer", certificate.getIssuerDN().toString());
            kvPrinter.addKv(prefix + "Validity", certificate.getNotBefore() + " to " + certificate.getNotAfter());
            kvPrinter.addKv(prefix + "Algorithm", certificate.getSigAlgName());
            kvPrinter.addKv(prefix + "Serial", certificate.getSerialNumber().toString());
            kvPrinter.addKv(prefix + "MD5 Thumb", certificate.getMd5Thumbprint());
            kvPrinter.addKv(prefix + "SHA1 Thumb", certificate.getSha1Thumbprint());
        }

        kvPrinter.endKeyValueSection();
    }

    private static void appendFileInfo(final SectionedPrinter kvPrinter, final String file) {
        final FileInfo apkContents = new FileInfo(file);

        kvPrinter.add("File Info");
        kvPrinter.startKeyValueSection();
        kvPrinter.addKv("APK", apkContents.getPath());
        kvPrinter.addKv("MD5", apkContents.getMd5());
        kvPrinter.addKv("SHA1", apkContents.getSha1());
        kvPrinter.endKeyValueSection();
    }

    private static void appendApkInfo(final SectionedPrinter kvPrinter, final String file) {
        final ApkContents fileInfo = new ApkContents(file);
        final List<String> jniArchitectures = fileInfo.getJniArchitectures();


        kvPrinter.add("APK Contents");
        kvPrinter.startKeyValueSection();
        kvPrinter.addKv("Assets", fileInfo.getNumberOfAssets());
        kvPrinter.addKv("Unique Raw", fileInfo.getNumberOfRawRes());
        kvPrinter.addKv("Unique Layouts", fileInfo.getNumberOfLayoutRes());
        kvPrinter.addKv("Unique Drawables", fileInfo.getNumberOfDrawableRes());
        kvPrinter.addKv("JNI Architectures", jniArchitectures.isEmpty()
                ? "none"
                : toString(jniArchitectures));
        kvPrinter.endKeyValueSection();
    }

    private static void appendManifestInfo(final SectionedPrinter kvPrinter, final String file) {
        kvPrinter.add("AndroidManifest Info");
        kvPrinter.startKeyValueSection();
        try {
            final ManifestParser parser = new ManifestParser(file);
            final AndroidManifest manifest = parser.parse();
            kvPrinter.addKv("Application Id", manifest.getApplicationId());
            kvPrinter.addKv("Version Name", manifest.getVersionName());
            kvPrinter.addKv("Version Code", manifest.getVersionCode());
            kvPrinter.addKv("Minimum SDK", manifest.getMinSdkVersion());
            kvPrinter.addKv("Compile SDK", manifest.getTargetSdkVersion());
            kvPrinter.addKv("Build SDK", manifest.getPlatformBuildSdkVersion());
            kvPrinter.addKv("Debuggable", manifest.isDebuggable());

            //System.out.println(manifest.getXml());
        } catch (Exception e) {
            kvPrinter.addKv("Parsing Error", e.getMessage());
        }

        kvPrinter.endKeyValueSection();
    }

    private static String toString(final List<?> list) {
        final StringBuilder sb = new StringBuilder();
        for (final Object object : list) {
            if (sb.length() > 0) {
                sb.append(", ");
            }

            sb.append(object);
        }
        return sb.toString();
    }
}
