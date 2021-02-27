package z21Drive.record.loconet;

import z21Drive.record.Z21DataRecord;
import z21Drive.record.Z21RecordType;

public class Z21RecordLocoNetLanDetector extends Z21RecordLocoNet {

    public Z21RecordLocoNetLanDetector(Z21DataRecord record) {
        super(record);
    }

    @Override
    public Z21RecordType getRecordType() {
        return Z21RecordType.LAN_LOCONET_DETECTOR;
    }

    @Override
    public boolean isBroadCast() { return false; }

    @Override
    public boolean isResponse() { return true; }
}
