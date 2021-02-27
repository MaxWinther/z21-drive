package z21Drive.record;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for Z21 Record.
 */
public abstract class Z21Record {

    protected static final Logger LOGGER_RECORD = LoggerFactory.getLogger("Z21 Record");

    protected Z21DataRecord z21DataRecord;

    public abstract Z21RecordType getRecordType();

    public abstract boolean isBroadCast();
    public abstract boolean isResponse();

    public Z21Record(Z21DataRecord z21DataRecord) {
        this.z21DataRecord = z21DataRecord;
    }

    public byte[] getByteRepresentation() {
        return z21DataRecord.raw;
    }

    @Override
    public String toString() {
        return getRecordType().name() + " broadcast=" + isBroadCast()+ " response=" + isResponse();
    }
}
