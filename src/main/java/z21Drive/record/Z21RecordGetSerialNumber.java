package z21Drive.record;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Represents the response with serial number of z21.
 */
public class Z21RecordGetSerialNumber extends Z21Record {
    public int serialNumber;

    public Z21RecordGetSerialNumber(Z21DataRecord record) {
        super(record);

        if (record != null) {
            byte[] serialBytes = new byte[4];
            serialBytes[0] = record.raw[4];
            serialBytes[1] = record.raw[5];
            serialBytes[2] = record.raw[6];
            serialBytes[3] = record.raw[7];

            ByteBuffer buffer = ByteBuffer.wrap(serialBytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            serialNumber = buffer.getInt();
        }
    }

    @Override
    public Z21RecordType getRecordType() {
        return Z21RecordType.LAN_GET_SERIAL_NUMBER_RESPONSE;
    }

    @Override
    public boolean isBroadCast() { return false; }

    @Override
    public boolean isResponse() { return true; }
}
