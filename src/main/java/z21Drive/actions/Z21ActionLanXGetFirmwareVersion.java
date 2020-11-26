package z21Drive.actions;

public class Z21ActionLanXGetFirmwareVersion extends Z21Action {
    private static final byte[] REP = new byte[] { 0x07, 0x00, 0x40, 0x00, (byte) 0xF1, 0x0A, (byte) 0xFB };

    public Z21ActionLanXGetFirmwareVersion() {
        addBytes(REP);
    }

    /**
     * Not necessary here.
     * 
     * @param objs
     *            Whatever
     */
    @Override
    public void addDataToByteRepresentation(int... objs) {
    }
}
