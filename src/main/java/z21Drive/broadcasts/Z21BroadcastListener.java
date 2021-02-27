package z21Drive.broadcasts;

import z21Drive.record.Z21Record;
import z21Drive.record.Z21RecordType;

public interface Z21BroadcastListener {
    void onBroadCast(Z21RecordType type, Z21Record broadcast);

    Z21RecordType[] getListenerTypes();
}
