package z21Drive.record.xbus;

import z21Drive.record.Z21DataRecord;
import z21Drive.record.Z21RecordType;

import static z21Drive.record.Z21DataRecord.Z21_RECORD_DB1_OFFSET;
import static z21Drive.record.Z21DataRecord.Z21_RECORD_DB2_OFFSET;

/**
 * Received as a response to Z21ActionLanXGetFIrmwareVersion Weird display of firmware versions.
 * Check out official documentation for more info.
 * 
 * @see z21Drive.actions.Z21ActionLanXGetFirmwareVersion
 */
public class Z21RecordLanXGetFirmwareVersion extends Z21RecordXBus {
    private int firmwareVersion;

    public Z21RecordLanXGetFirmwareVersion(Z21DataRecord record) {
        super(record);
        if (record != null) {
            firmwareVersion = record.raw[Z21_RECORD_DB1_OFFSET] << 8 | record.raw[Z21_RECORD_DB2_OFFSET];
        }
    }

    @Override
    public Z21RecordType getRecordType() {
        return Z21RecordType.LAN_X_GET_FIRMWARE_VERSION;
    }

    @Override
    public boolean isBroadCast() { return false; }

    @Override
    public boolean isResponse() { return true; }

    public int getFirmwareVersion() {
        return firmwareVersion;
    }

}
