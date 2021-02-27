package z21Drive.record.loconet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import z21Drive.record.Z21DataRecord;
import z21Drive.record.Z21Record;

/**
 * Base class for Z21 X-BUS record.
 */
public abstract class Z21RecordLocoNet extends Z21Record {

    private static final Logger LOGGER_RECEIVER = LoggerFactory.getLogger("Z21 Receiver");

    public final static int Z21_HEADER_1_LOCO_NET_RX = 0xA0;
    public final static int Z21_HEADER_1_LOCO_NET_TX = 0xA1;
    public final static int Z21_HEADER_1_LOCO_NET_DISPATCH_ADDR = 0xA3;
    public final static int Z21_HEADER_1_LOCO_NET_DETECTOR = 0xA4;

    public final static int Z21_HEADER_2_LOCO_NET = 0x00;

    public Z21RecordLocoNet(Z21DataRecord z21DataRecord) {
        super(z21DataRecord);
    }
}
