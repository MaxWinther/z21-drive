package z21Drive.actions;

/**
 * Used to switch track power on.
 */
public class Z21ActionLanXTrackPowerOn extends Z21Action {
    // Made a static one here for better performance.
    private static final byte[] REP = new byte[] { 0x07, 0x00, 0x40, 0x00, 0x21, (byte) 0x81, (byte) 0xa0 };

    public Z21ActionLanXTrackPowerOn() {
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
