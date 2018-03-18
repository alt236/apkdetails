package uk.co.alt236.apkdetails.repo.buildconfig;

import org.jf.dexlib2.ValueType;
import org.jf.dexlib2.dexbacked.value.DexBackedStringEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.value.*;

public class DexValueParser {

    public String getValueAsString(final EncodedValue value) {

        final String retVal;
        switch (value.getValueType()) {
            case ValueType.BYTE:
                retVal = String.valueOf(((ImmutableByteEncodedValue) value).getValue());
                break;
            case ValueType.SHORT:
                retVal = String.valueOf(((ImmutableShortEncodedValue) value).getValue());
                break;
            case ValueType.CHAR:
                retVal = String.valueOf(((ImmutableCharEncodedValue) value).getValue());
                break;
            case ValueType.INT:
                retVal = String.valueOf(((ImmutableIntEncodedValue) value).getValue());
                break;
            case ValueType.LONG:
                retVal = String.valueOf(((ImmutableLongEncodedValue) value).getValue());
                break;
            case ValueType.FLOAT:
                retVal = String.valueOf(((ImmutableFloatEncodedValue) value).getValue());
                break;
            case ValueType.DOUBLE:
                retVal = String.valueOf(((ImmutableDoubleEncodedValue) value).getValue());
                break;
            case ValueType.STRING:
                retVal = ((DexBackedStringEncodedValue) value).getValue();
                break;
            case ValueType.TYPE:
                retVal = getError("TYPE");
                break;
            case ValueType.FIELD:
                retVal = getError("FIELD");
                break;
            case ValueType.METHOD:
                retVal = getError("METHOD");
                break;
            case ValueType.ENUM:
                retVal = getError("ENUM");
                break;
            case ValueType.ARRAY:
                retVal = getError("ARRAY");
                break;
            case ValueType.ANNOTATION:
                retVal = getError("ANNOTATION");
                break;
            case ValueType.NULL:
                retVal = "null";
                break;
            case ValueType.BOOLEAN:
                retVal = String.valueOf(((ImmutableBooleanEncodedValue) value).getValue());
                break;
            default:
                retVal = getError(value.getValueType());
        }

        return retVal;
    }

    private String getError(final int valueType) {
        return getError(String.valueOf(valueType));
    }

    private String getError(final String valueType) {
        return "Error. Don't know how to handle ValueType=" + valueType;
    }
}
