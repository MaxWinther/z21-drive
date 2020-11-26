package z21Drive.actions;

/**
 * Used to switch track power off.
 */
public class Z21ActionLanXTrackPowerOff extends Z21Action {
    // Made a static one here for better performance.
    private static final byte[] REP = new byte[] { 0x07, 0x00, 0x40, 0x00, 0x21, (byte) 0x80, (byte) 0xA1 };

    public Z21ActionLanXTrackPowerOff() {
        addBytes(REP);
    }

    /**
     * Not necessary here.
     * 
     * @param objs
     *            Null or whatever.
     */
    @Override
    public void addDataToByteRepresentation(int... objs) {
    }
}
