package z21Drive.record.xbus;

import z21Drive.record.Z21DataRecord;
import z21Drive.record.Z21RecordType;

public class Z21RecordLanXStatusChanged extends Z21RecordXBus {

    @Override
    public Z21RecordType getRecordType() {
        return Z21RecordType.LAN_X_STATUS_CHANGED;
    }

    public Z21RecordLanXStatusChanged(Z21DataRecord record) {
        super(record);
    }

    @Override
    public boolean isBroadCast() { return false; }

    @Override
    public boolean isResponse() { return true; }
}
