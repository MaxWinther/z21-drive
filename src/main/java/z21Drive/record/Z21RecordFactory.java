package z21Drive.record;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import z21Drive.record.loconet.Z21RecordFactoryLocoNet;
import z21Drive.record.xbus.Z21RecordFactoryXBus;

import java.net.DatagramPacket;

public final class Z21RecordFactory {

    protected static final Logger LOGGER_RECORD = LoggerFactory.getLogger("Z21 Record");

    /**
     * Here the magic of turning bytes into objects happens.
     *
     * @param packet
     *            UDP packet received from Z21
     * @return Z21 response object which represents the byte array.
     */
    public static Z21Record fromPacket(DatagramPacket packet) {
        Z21DataRecord record = new Z21DataRecord(packet.getData());

        if (Z21RecordFactoryXBus.isXBusResponse(record)){
            return Z21RecordFactoryXBus.fromPacket(record);

        } else if (Z21RecordFactoryLocoNet.isLocoNetResponse(record)) {
            return Z21RecordFactoryLocoNet.responseFromPacket(record);

        } else if ((record.header1 & 0xFF) == 0x88 && record.header2 == 0x00) {
            return new Z21RecordRailcomDatachanged(record);

        } else {
            LOGGER_RECORD.warn("Received unknown message. record={}", record);
            return null;
        }
    }
}
