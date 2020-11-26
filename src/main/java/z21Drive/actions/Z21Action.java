package z21Drive.actions;

import java.nio.ByteBuffer;

public abstract class Z21Action {
    /**
     * Represents the message in z21 understandable form.
     */
    private ByteBuffer byteRepresentation;

    protected Z21Action() {
        byteRepresentation = ByteBuffer.allocate(32);
        // start position is 2 because 0 and 1 are reserved for length
        byteRepresentation.position(2);
    }

    public ByteBuffer getByteRepresentation() {
        return byteRepresentation;
    }

    /**
     * Here actual conversion to bytes happens
     * 
     * @param values
     *            Whatever objects you might need to determine the bytes.
     */
    protected abstract void addDataToByteRepresentation(int... values);

    /**
     * Adds the required length of message bytes.
     */
    protected void addLenByte() {
        int len = byteRepresentation.position();
        // len += 2;
        byteRepresentation.put(0, (byte) (len & 0xFF));
        byteRepresentation.put(1, (byte) 0);
    }

    protected void addByte(int inValue) {
        byteRepresentation.put((byte) (inValue & 0xFF));
    }

    protected void addByte(byte byteValue) {
        byteRepresentation.put(byteValue);
    }

    protected void addBytes(byte[] byteValues) {
        byteRepresentation.put(byteValues);
    }

    protected void appendChecksum() {
        int beginIndex = 4;

        final byte[] data = byteRepresentation.array();
        int length = byteRepresentation.position();

        int xor = 0;
        for (int index = beginIndex; index < length; index++) {
            xor = xor ^ data[index];
        }

        addByte(xor);
    }
}
