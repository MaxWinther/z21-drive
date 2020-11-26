package z21Drive.actions;

import z21Drive.LocoAddressOutOfRangeException;

/**
 * Sent to z21 to make a loco move. Supports loco addresses up to 63.
 */
public class Z21ActionSetLocoDrive extends Z21Action {
    /**
     * Default constructor for this.
     * 
     * @param locoAddress
     *            Address of the loco that this action targets.
     * @param speed
     *            New speed value.
     * @param speedStepsID
     *            How many speed steps loco has (IDs: 0 = 14 speed steps, 2 = 28 speed steps, 3 = 128 speed steps).
     * @param direction
     *            Which direction the loco should go (true = forward, false = backward).
     * @throws LocoAddressOutOfRangeException
     *             Thrown when loco address is too big or negative.
     */
    public Z21ActionSetLocoDrive(int locoAddress, int speed, int speedStepsID, boolean direction)
        throws LocoAddressOutOfRangeException {
        addByte(0x40);
        addByte(0x00);
        if (locoAddress < 1)
            throw new LocoAddressOutOfRangeException(locoAddress);
        addDataToByteRepresentation(locoAddress, speed, speedStepsID, direction ? 0x80 : 0x00);

        addLenByte();

    }

    @Override
    public void addDataToByteRepresentation(int... objs) {
        addByte(0xE4);
        switch (objs[2]) {
            case 0:
                addByte(16);
                break;
            case 2:
                addByte(18);
                break;
            case 3:
                addByte(19);
                break;
            default:
                System.err.println("Constructing new SetLocoDrive action: Unknown speed step ID");
                throw new RuntimeException("Wrong speed step ID");
        }

        byte Adr_MSB = (byte) ((objs[0]) >> 8);
        byte Adr_LSB = (byte) ((objs[0]) & 0b11111111);
        if (Adr_MSB != 0) {
            Adr_MSB |= 0b11000000;
        }

        addByte(Adr_MSB);
        addByte(Adr_LSB);

        int speedAndDirection = objs[1];
        speedAndDirection |= objs[3];

        addByte(speedAndDirection);

        appendChecksum();
        // byteRepresentation
        // .add((byte) (byteRepresentation.get(2) ^ byteRepresentation.get(3) ^ byteRepresentation.get(4)
        // ^ byteRepresentation.get(5) ^ byteRepresentation.get(6)));
    }

    // @SuppressWarnings("Duplicates")
    // private boolean[] fromByte(byte x) {
    // boolean bs[] = new boolean[8];
    // bs[0] = ((x & 0x01) != 0);
    // bs[1] = ((x & 0x02) != 0);
    // bs[2] = ((x & 0x04) != 0);
    // bs[3] = ((x & 0x08) != 0);
    // bs[4] = ((x & 0x16) != 0);
    // bs[5] = ((x & 0x32) != 0);
    // bs[6] = ((x & 0x64) != 0);
    // bs[7] = ((x & 0x128) != 0);
    // return bs;
    // }
}
