package z21Drive.record.xbus;

import z21Drive.record.Z21DataRecord;

/**
 * Sent from z21 when some client switches track power off or the button on z21 is pressed.
 */
public class Z21RecordLanXTrackPowerOff extends Z21RecordXBus {

    public Z21RecordLanXTrackPowerOff(Z21DataRecord z21DataRecord) {
        super(z21DataRecord);
    }

    @Override
    public z21Drive.record.Z21RecordType getRecordType() {
        return z21Drive.record.Z21RecordType.LAN_X_TRACK_POWER_OFF;
    }

    @Override
    public boolean isBroadCast() { return true; }

    @Override
    public boolean isResponse() { return false; }
}
