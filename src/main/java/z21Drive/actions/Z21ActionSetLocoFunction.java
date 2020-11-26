package z21Drive.actions;

import z21Drive.LocoAddressOutOfRangeException;

/**
 * Sent to z21 to change a loco function. Supports functions from F0 to F12. With some more work it could be made to
 * allow use of more functions. Supports loco addresses from 1 to 128.
 */
public class Z21ActionSetLocoFunction extends Z21Action {

    public Z21ActionSetLocoFunction(int locoAddress, int functionNo, boolean on) throws LocoAddressOutOfRangeException {
        addByte(0x40);
        addByte(0x00);
        if (locoAddress < 1)
            throw new LocoAddressOutOfRangeException(locoAddress);
        addDataToByteRepresentation(locoAddress, functionNo, on ? 0x80 : 0x00);
        addLenByte();
    }

    @Override
    public void addDataToByteRepresentation(int... objs) {
        addByte(0xE4);
        addByte(0xF8);
        byte Adr_MSB = (byte) ((objs[0]) >> 8);
        byte Adr_LSB = (byte) ((objs[0]) & 0b11111111);
        if (Adr_MSB != 0) {
            Adr_MSB |= 0b11000000;
        }
        addByte(Adr_MSB);
        addByte(Adr_LSB);

        // Generate data byte
        int dataByte = (objs[1] & 63) | objs[2];

        addByte(dataByte);

        appendChecksum();
        // // Add the XOR byte
        // byte xor;
        // xor =
        // (byte) (byteRepresentation.get(2) ^ byteRepresentation.get(3) ^ byteRepresentation.get(4)
        // ^ byteRepresentation.get(5) ^ byteRepresentation.get(6));
        // byteRepresentation.add(xor);
    }
}
