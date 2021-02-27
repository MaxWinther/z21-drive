package z21Drive.record.xbus;

import z21Drive.record.Z21DataRecord;
import z21Drive.record.Z21Record;

/**
 * Received when z21 enters programming mode.
 */
public class Z21RecordLanXProgrammingMode extends Z21RecordXBus {
    public Z21RecordLanXProgrammingMode(Z21DataRecord z21DataRecord) {
        super(z21DataRecord);
    }

    @Override
    public z21Drive.record.Z21RecordType getRecordType() {
        return z21Drive.record.Z21RecordType.LAN_X_PROGRAMMING_MODE;
    }

    @Override
    public boolean isBroadCast() { return true; }

    @Override
    public boolean isResponse() { return false; }
}
