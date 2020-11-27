package z21Drive;

public class LocoAddressOutOfRangeException extends Exception {
    private static final long serialVersionUID = 1L;

    private final int address;

    public LocoAddressOutOfRangeException(int invalidAddress) {
        address = invalidAddress;
    }

    public int getInvalidAddress() {
        return address;
    }
}
