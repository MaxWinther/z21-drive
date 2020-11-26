package z21Drive.actions;

/**
 * Used to log off from z21 when shutting down the app. Called by Z21 class' finalize method, but you can do it yourself
 * for extra safety. This client gets automatically removed from z21's list of clients after 30 seconds of inactivity,
 * so there is no real problem if this action is not sent.
 */
public class Z21ActionLanLogoff extends Z21Action {
    // Made a static one here for better performance.
    private static byte[] REP = new byte[] { 0x04, 0x00, 0x30, 0x00 };

    public Z21ActionLanLogoff() {
        addBytes(REP);
    }

    /**
     * Unnecessary here.
     * 
     * @param objs
     *            Make it null
     */
    @Override
    public void addDataToByteRepresentation(int... objs) {
    }
}
