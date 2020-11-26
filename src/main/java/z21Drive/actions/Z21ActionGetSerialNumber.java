package z21Drive.actions;

/**
 * Mostly used to poll the keepAlive timer, but feel free to use it if you really want to know what is the serial number
 * of your z21...
 */
public class Z21ActionGetSerialNumber extends Z21Action {

    public Z21ActionGetSerialNumber() {
        addByte(0x10);
        addByte(0x00);
        addLenByte();
    }

    // Not necessary here
    @Override
    public void addDataToByteRepresentation(int... objs) {
    }
}
