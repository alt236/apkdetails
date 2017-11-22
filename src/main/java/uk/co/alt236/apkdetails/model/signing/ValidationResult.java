package uk.co.alt236.apkdetails.model.signing;

import uk.co.alt236.apkdetails.model.common.Entry;

import java.util.Collections;
import java.util.List;

public class ValidationResult {
    private final SignatureStatus signatureStatusStatus;
    private final List<Entry> failedEntries;


    ValidationResult(final SignatureStatus signatureStatusStatus,
                     final List<Entry> result) {
        this.signatureStatusStatus = signatureStatusStatus;
        this.failedEntries = result;
    }

    ValidationResult(final SignatureStatus signatureStatusStatus) {
        this(signatureStatusStatus, Collections.emptyList());
    }

    public SignatureStatus getSignatureStatus() {
        return signatureStatusStatus;
    }

    public List<Entry> getFailedEntries() {
        return failedEntries;
    }
}
