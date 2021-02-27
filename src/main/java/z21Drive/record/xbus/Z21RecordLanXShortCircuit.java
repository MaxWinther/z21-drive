package z21Drive.record.xbus;

import z21Drive.record.Z21DataRecord;
import z21Drive.record.Z21Record;

/**
 * Sent from z21 when a short circuit occurs somewhere on the layout. When this happens, z21 turns track power off and
 * status LED starts blinking red.
 */
public class Z21RecordLanXShortCircuit extends Z21RecordXBus {
    public Z21RecordLanXShortCircuit(Z21DataRecord z21DataRecord) {
        super(z21DataRecord);
    }

    @Override
    public z21Drive.record.Z21RecordType getRecordType() {
        return z21Drive.record.Z21RecordType.LAN_X_SHORT_CIRCUIT;
    }

    @Override
    public boolean isBroadCast() { return true; }

    @Override
    public boolean isResponse() { return false; }
}
