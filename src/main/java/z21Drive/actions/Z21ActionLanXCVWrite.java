package z21Drive.actions;

import z21Drive.LocoAddressOutOfRangeException;

/**
 * Write a CV Value
 * 
 * @author sven
 *
 */
public class Z21ActionLanXCVWrite extends Z21Action {

    /**
     * @param cv
     *            The CV to read.
     * @param value
     *            the value
     * @throws LocoAddressOutOfRangeException
     *             Thrown if loco address is out of supported range.
     */
    public Z21ActionLanXCVWrite(int cv, int value) throws LocoAddressOutOfRangeException {
        addByte(0x40);
        addByte(0x00);
        addDataToByteRepresentation(cv, value);
        addLenByte();
    }

    @Override
    public void addDataToByteRepresentation(int... objs) {
        // Add all the data
        addByte(0x24); // X-Header
        addByte(0x12); // DB 0
        int cv = objs[0] - 1; // 0 => CV1, ...
        int value = objs[1];
        addByte(cv >> 8);
        addByte(cv & 0xFF);
        addByte(value & 0xFF);

        appendChecksum();
        // byteRepresentation
        // .add((byte) (byteRepresentation.get(2) ^ byteRepresentation.get(3) ^ byteRepresentation.get(4)
        // ^ byteRepresentation.get(5) ^ byteRepresentation.get(6)));
    }
}