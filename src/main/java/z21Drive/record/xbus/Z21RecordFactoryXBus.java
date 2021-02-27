package z21Drive.record.xbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import z21Drive.record.*;

import static z21Drive.record.xbus.Z21RecordXBus.*;

/**
 * Factory class for Z21 X-BUS record.
 */
public final class Z21RecordFactoryXBus {

    private static final Logger LOGGER_RECEIVER = LoggerFactory.getLogger("Z21 Receiver");

    public static boolean isXBusResponse(Z21DataRecord record) {
        return (record.header1 == Z21_HEADER_1_X_BUS && record.header2 == Z21_HEADER_2_X_BUS);
    }

    public static Z21RecordXBus fromPacket(Z21DataRecord record) {

        if (record.xHeader == 0xF1) {
            return new Z21RecordLanXGetFirmwareVersion(record);

        } else if (record.xHeader == X_HEADER_LAN_X_CV_RESULT && record.db0 == DBO_LAN_X_CV_RESULT) {
            return new Z21RecordLanXCVResult(record);

        } else if (record.xHeader == X_HEADER_LAN_BC_CV_XX && record.db0 == DBO_LAN_X_CV_NACK) {
            return new Z21RecordLanXCVNACK(record);

        } else if (record.xHeader == X_HEADER_LAN_X_STATUS_CHANGED && record.db0 == DBO_LAN_X_STATUS_CHANGED) {
            return new Z21RecordLanXStatusChanged(record);

        } else if (record.xHeader == 239) {
            return new Z21RecordLanXLocoInfo(record);

        } else if (record.xHeader == X_HEADER_LAN_BC_CV_XX && record.db0  == 0x82) {
            return new Z21RecordLanXUnknownCommand(record);

        } else if (record.xHeader == X_HEADER_LAN_BC_CV_XX && record.db0 == 0x00) {
            return new Z21RecordLanXTrackPowerOff(record);

        } else if (record.xHeader == X_HEADER_LAN_BC_CV_XX && record.db0  == 0x01) {
            return new Z21RecordLanXTrackPowerOn(record);

        } else if (record.xHeader == X_HEADER_LAN_BC_CV_XX && record.db0 == 0x02) {
            return new Z21RecordLanXProgrammingMode(record);

        } else if (record.xHeader == X_HEADER_LAN_BC_CV_XX && record.db0  == 0x08) {
            return new Z21RecordLanXShortCircuit(record);

        } else {
            LOGGER_RECEIVER.warn("Received unknown X_BUS message xHeader=0x" + String.format("%02X ", record.xHeader));
            return null;
        }
    }
}
