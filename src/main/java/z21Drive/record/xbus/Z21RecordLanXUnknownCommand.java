package z21Drive.record.xbus;

import z21Drive.record.Z21DataRecord;
import z21Drive.record.Z21Record;

/**
 * Shipped from z21 if it receives an invalid command.
 * 
 * @see z21Drive.Z21
 */
public class Z21RecordLanXUnknownCommand extends Z21RecordXBus {

    public Z21RecordLanXUnknownCommand(Z21DataRecord z21DataRecord) {
        super(z21DataRecord);
    }

    @Override
    public z21Drive.record.Z21RecordType getRecordType() {
        return z21Drive.record.Z21RecordType.LAN_X_UNKNOWN_COMMAND;
    }

    @Override
    public boolean isBroadCast() { return true; }

    @Override
    public boolean isResponse() { return false; }
}
