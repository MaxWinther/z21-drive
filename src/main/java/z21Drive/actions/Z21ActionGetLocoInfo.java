package z21Drive.actions;

import z21Drive.LocoAddressOutOfRangeException;

/**
 * Used to retrieve loco status from z21. Supports loco addresses up to 128.
 */
public class Z21ActionGetLocoInfo extends Z21Action {

    /**
     * @param locoAddress
     *            Address of the loco to request info of.
     * @throws LocoAddressOutOfRangeException
     *             Thrown if loco address is out of supported range.
     */
    public Z21ActionGetLocoInfo(int locoAddress) throws LocoAddressOutOfRangeException {
        addByte(0x40);
        addByte(0x00);
        if (locoAddress < 1)
            throw new LocoAddressOutOfRangeException(locoAddress);
        addDataToByteRepresentation(locoAddress);
        addLenByte();
    }

    @Override
    public void addDataToByteRepresentation(int... objs) {
        // Add all the data
        addByte(0xE3);
        addByte(0xF0);
        byte Adr_MSB = (byte) ((objs[0]) >> 8);
        byte Adr_LSB = (byte) ((objs[0]) & 0b11111111);
        if (Adr_MSB != 0) {
            Adr_MSB |= 0b11000000;
        }

        addByte(Adr_MSB);
        addByte(Adr_LSB);

        appendChecksum();

        // addByte((byte) (byteRepresentation.get(2) ^ byteRepresentation.get(3) ^ byteRepresentation.get(4)
        // ^ byteRepresentation.get(5)));
    }
}
