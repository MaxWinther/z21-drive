package z21Drive.responses;

import z21Drive.record.Z21Record;
import z21Drive.record.Z21RecordType;

public interface Z21ResponseListener {
    void responseReceived(Z21RecordType type, Z21Record response);
    Z21RecordType[] getListenerTypes();
}
