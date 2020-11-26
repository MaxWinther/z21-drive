package z21Drive.actions;

import z21Drive.LocoAddressOutOfRangeException;

public class Z21ActionLanXCVPomReadByte extends Z21Action {

    /**
     * Reads a CV via RailCom on the MainTrack
     * 
     * @param cv
     *            The CV to read.
     * @param locoAddress
     *            the Adress of the Loco
     * @throws LocoAddressOutOfRangeException
     *             Thrown if loco address is out of supported range.
     */
    public Z21ActionLanXCVPomReadByte(int locoAddress, int cv) throws LocoAddressOutOfRangeException {
        addByte(0x40);
        addByte(0x00);
        if (locoAddress < 1)
            throw new LocoAddressOutOfRangeException(locoAddress);
        addDataToByteRepresentation(locoAddress, cv);
        addLenByte();
    }

    @Override
    public void addDataToByteRepresentation(int... objs) {
        // Add all the data
        addByte(0xE6); // X-Header
        addByte(0x30); // DB 0

        // Adding Loco-Addr
        byte Adr_MSB = (byte) ((objs[0]) >> 8);
        byte Adr_LSB = (byte) ((objs[0]) & 0b11111111);
        if (Adr_MSB != 0) {
            Adr_MSB |= 0b11000000;
        }
        addByte(Adr_MSB); // DB 1
        addByte(Adr_LSB); // DB 2

        // Adding CV
        int cv = objs[1] - 1; // 0 => CV1, ...
        addByte(0xE4 | cv >> 8); // DB3
        addByte(cv & 0xFF); // DB4
        addByte(0); // DB5

        appendChecksum();
        // byteRepresentation
        // .add((byte) (byteRepresentation.get(2) & 0xff ^ byteRepresentation.get(3) & 0xff
        // ^ byteRepresentation.get(4) & 0xff ^ byteRepresentation.get(5) & 0xff ^ byteRepresentation.get(6) & 0xff
        // ^ byteRepresentation.get(7) & 0xff ^ byteRepresentation.get(8) & 0xff));
    }
}