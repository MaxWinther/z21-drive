package z21Drive.actions;

import z21Drive.LocoAddressOutOfRangeException;

/**
 * Request a CV Value
 * 
 * @author littleyoda
 */
public class Z21ActionLanXCVRead extends Z21Action {

    /**
     * @param cv
     *            The CV to read.
     * @throws LocoAddressOutOfRangeException
     *             Thrown if loco address is out of supported range.
     */
    public Z21ActionLanXCVRead(int cv) throws LocoAddressOutOfRangeException {
        addByte(0x40);
        addByte(0x00);
        addDataToByteRepresentation(cv);
        addLenByte();
    }

    @Override
    public void addDataToByteRepresentation(int... objs) {
        // Add all the data
        addByte(0x23);
        addByte(0x11);
        int cv = objs[0] - 1; // 0 => CV1, ...
        addByte(cv >> 8);
        addByte(cv & 0xFF);

        appendChecksum();
        // byteRepresentation
        // .add((byte) (byteRepresentation.get(2) ^ byteRepresentation.get(3) ^ byteRepresentation.get(4)
        // ^ byteRepresentation.get(5)));
    }
}