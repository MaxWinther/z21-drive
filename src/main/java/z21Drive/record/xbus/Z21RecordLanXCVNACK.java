package z21Drive.record.xbus;

import z21Drive.record.Z21DataRecord;
import z21Drive.record.Z21RecordType;

/**
 * No response from the loco-decoder. Programming/reading was unsuccessful.
 */
public class Z21RecordLanXCVNACK extends Z21RecordXBus {

    @Override
    public Z21RecordType getRecordType() {
        return Z21RecordType.LAN_X_CV_NACK;
    }

    @Override
    public boolean isBroadCast() { return false; }

    @Override
    public boolean isResponse() { return true; }

    public Z21RecordLanXCVNACK(Z21DataRecord record) {
        super(record);
    }
}
