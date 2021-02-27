package z21Drive.record.xbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import z21Drive.record.Z21DataRecord;
import z21Drive.record.Z21Record;

/**
 * Base class for Z21 X-BUS record.
 */
public abstract class Z21RecordXBus extends Z21Record {

    private static final Logger LOGGER_RECEIVER = LoggerFactory.getLogger("Z21 Receiver");

    public final static int Z21_HEADER_1_X_BUS = 0x40;
    public final static int Z21_HEADER_2_X_BUS = 0x00;

    public static int X_HEADER_LAN_X_STATUS_CHANGED = 0x62;

    //DBO Constants for X_HEADER_LAN_X_STATUS_CHANGED
    public static int DBO_LAN_X_STATUS_CHANGED = 0x22;

    public static int X_HEADER_LAN_X_CV_RESULT = 0x64;
    public static int X_HEADER_LAN_BC_CV_XX = 0x61;

    //DBO Constants for X_HEADER_LAN_X_CV_RESULT
    public static int DBO_LAN_X_CV_RESULT = 0x14;

    //DBO Constants for X_HEADER_LAN_X_BC_XX
    public static int DBO_LAN_X_BC_TRACK_POWER_OFF = 0x00;
    public static int DBO_LAN_X_BC_TRACK_POWER_ON = 0x01;
    public static int DBO_LAN_X_BC_PROGRAMMING_MODE = 0x02;
    public static int DBO_LAN_X_BC_TRACK_SHORT_CIRCUIT = 0x03;
    public static int DBO_LAN_X_CV_NACK = 0x13;

    public static int X_HEADER_LAN_X_LOCO_INFO = 0xEF;

    public Z21RecordXBus(Z21DataRecord z21DataRecord) {
        super(z21DataRecord);
    }
}
