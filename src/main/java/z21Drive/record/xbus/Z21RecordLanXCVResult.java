package z21Drive.record.xbus;

import z21Drive.record.Z21DataRecord;
import z21Drive.record.Z21RecordType;

import static z21Drive.record.Z21DataRecord.Z21_RECORD_DB1_OFFSET;
import static z21Drive.record.Z21DataRecord.Z21_RECORD_DB2_OFFSET;

/**
 * Result for the Action_Lan_X_CV_Read
 */
public class Z21RecordLanXCVResult extends Z21RecordXBus {
    private int cvAdr;

    private int value;

    public Z21RecordLanXCVResult(Z21DataRecord record) {
        super(record);
        if (record != null)
            populateFields();
    }

    @Override
    public Z21RecordType getRecordType() {
        return Z21RecordType.LAN_X_CV_RESULT;
    }

    @Override
    public boolean isBroadCast() { return false; }

    @Override
    public boolean isResponse() { return true; }

    private void populateFields() {
        byte cvadr_MSB = z21DataRecord.raw[Z21_RECORD_DB1_OFFSET];
        byte cvadr_LSB = z21DataRecord.raw[Z21_RECORD_DB2_OFFSET];

        cvAdr = ((cvadr_MSB & 0x3F) << 8 | cvadr_LSB) + 1; // 0 = CV1, ...
        value = z21DataRecord.raw[8] & 255;
    }

    public int getCVadr() {
        return cvAdr;
    }

    public int getValue() {
        return value;
    }
}
