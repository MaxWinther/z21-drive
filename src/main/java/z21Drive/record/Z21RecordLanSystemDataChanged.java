package z21Drive.record;

import static z21Drive.record.Z21DataRecord.*;
import static z21Drive.utils.ByteUtils.byteArrayToString;
import static z21Drive.utils.ByteUtils.bytesToInt;

public class Z21RecordLanSystemDataChanged extends Z21Record {

    public final static int Z21_HEADER_1_LAN_SYSTEMSTATE_DATACHANGED = 0x84;
    public final static int Z21_HEADER_2_LAN_SYSTEMSTATE_DATACHANGED = 0x00;

    public static boolean isLanSystemDataChanged(Z21DataRecord z21DataRecord) {
        return (z21DataRecord.header1 & 0xFF) == Z21_HEADER_1_LAN_SYSTEMSTATE_DATACHANGED &&
                z21DataRecord.header2 == Z21_HEADER_2_LAN_SYSTEMSTATE_DATACHANGED;
    }

    public Z21RecordLanSystemDataChanged(Z21DataRecord z21DataRecord) {
        super(z21DataRecord);

        if (z21DataRecord != null) {
            filteredMainCurrent = bytesToInt(z21DataRecord.raw[Z21_RECORD_DATA_OFFSET + 4], z21DataRecord.raw[Z21_RECORD_DATA_OFFSET + 5]);
            temperature = bytesToInt(z21DataRecord.raw[Z21_RECORD_DATA_OFFSET + 6], z21DataRecord.raw[Z21_RECORD_DATA_OFFSET + 7]);
            supplyVoltage = bytesToInt(z21DataRecord.raw[Z21_RECORD_DATA_OFFSET + 8], z21DataRecord.raw[Z21_RECORD_DATA_OFFSET + 9]);
            vccVoltage = bytesToInt(z21DataRecord.raw[Z21_RECORD_DATA_OFFSET + 10], z21DataRecord.raw[Z21_RECORD_DATA_OFFSET + 11]);
        }
    }

    @Override
    public z21Drive.record.Z21RecordType getRecordType() {
        return Z21RecordType.LAN_SYSTEMSTATE_DATACHANGED;
    }

    @Override
    public boolean isBroadCast() { return true; }

    @Override
    public boolean isResponse() { return false; }

    @Override
    public String toString() {
        return super.toString() + "Main current=" + filteredMainCurrent + "mA supply-voltage=" + supplyVoltage + " vcc-voltage=" + vccVoltage +" temperature=" + temperature;
    }

    public int filteredMainCurrent;
    public int temperature;
    public int supplyVoltage;
    public int vccVoltage;
}
