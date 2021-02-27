package z21Drive.record;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static z21Drive.utils.ByteUtils.byteArrayToString;
import static z21Drive.utils.ByteUtils.byteToString;

/**
 * A Z21 data record, i.e. a request or response, is structured in the following way:
 *  -------------------------------------------------------
 *  ! DataLen (2 Byte) ! Header (2 Byte) ! Data (n Bytes) !
 *  -------------------------------------------------------
 *  The xHeader is the fist byte in DATA
 */
public class Z21DataRecord {

    protected static final Logger LOGGER_RECORD = LoggerFactory.getLogger("Z21 Record");

    public final static int Z21_RECORD_DATA_LEN_OFFSET = 0;
    public final static int Z21_RECORD_HEADER_1_OFFSET = 2;
    public final static int Z21_RECORD_HEADER_2_OFFSET = 3;
    public final static int Z21_RECORD_X_HEADER_OFFSET = 4;
    public final static int Z21_RECORD_DATA_OFFSET     = 4;
    public final static int Z21_RECORD_DBO_OFFSET      = 5;
    public final static int Z21_RECORD_DB1_OFFSET      = 6;
    public final static int Z21_RECORD_DB2_OFFSET      = 7;

    //TODO Make len chek on record
    public Z21DataRecord(byte[] record) {
        length = record[0];
        header1 = record[Z21_RECORD_HEADER_1_OFFSET];
        header2 = record[Z21_RECORD_HEADER_2_OFFSET];
        xHeader = record[Z21_RECORD_X_HEADER_OFFSET] & 255;
        db0 = record[Z21_RECORD_DBO_OFFSET] & 255;

        // Discard all zeros
        raw = new byte[record[0]];
        System.arraycopy(record, 0, raw, 0, (record.length < record[0] ? record.length : record[0]));

        if (raw.length != 0 && record[record[0] + 1] != 0) {
            // We got two messages in one packet.
            // Don't know yet what to do. TODO
            LOGGER_RECORD
                    .info(
                            "Received two messages in one packet. Multiple messages not supported yet. Please report to github. raw={}",byteArrayToString(raw));
        }
    }

    public int length;
    public byte[] raw;
    public byte header1;
    public byte header2;
    public int xHeader;
    public int db0;

    @Override
    public String toString() {
        return "Z21DataRecord{" +
                "length=" + length +
                ", header1=" + byteToString(header1) +
                ", header2=" + byteToString(header2) +
                ", xHeader=" + xHeader +
                ", db0=" + db0 +
                ", raw=" + byteArrayToString(raw) +
                '}';
    }
}
