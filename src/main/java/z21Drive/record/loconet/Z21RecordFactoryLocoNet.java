package z21Drive.record.loconet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import z21Drive.record.Z21DataRecord;
import z21Drive.record.Z21Record;

import java.util.Arrays;
import java.util.List;

import static z21Drive.Z21Constants.*;

/**
 * Base class for Z21 LocoNet records.
 */
public abstract class Z21RecordFactoryLocoNet {

    protected static final Logger LOGGER_RECORD = LoggerFactory.getLogger("Z21 Record");

    private static final List header1List = Arrays.asList(
            Z21_HEADER_1_LOCO_NET_RX,
            Z21_HEADER_1_LOCO_NET_TX,
            Z21_HEADER_1_LOCO_NET_DISPATCH_ADDR
            );

    public static boolean isLocoNetResponse(Z21DataRecord record) {
        return (header1List.contains(record.header1) && record.header2 == Z21_HEADER_2_LOCO_NET);
    }

    public static Z21Record responseFromPacket(Z21DataRecord record) {
        LOGGER_RECORD.warn("Received unknown LOCO-NET message");
        return null;
    }
}
