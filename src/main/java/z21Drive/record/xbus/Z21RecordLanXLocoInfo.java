package z21Drive.record.xbus;

import z21Drive.record.Z21DataRecord;

import static z21Drive.utils.ByteUtils.fromByte;

/**
 * Probably the most important broadcast, because it represents the current state of a loco. Supports up to 28
 * functions.
 */
public class Z21RecordLanXLocoInfo extends Z21RecordXBus {

    public Z21RecordLanXLocoInfo(Z21DataRecord z21DataRecord) {
        super(z21DataRecord);
        if (z21DataRecord.raw != null)
            populateFields();
    }

    @Override
    public z21Drive.record.Z21RecordType getRecordType() {
        return z21Drive.record.Z21RecordType.LAN_X_LOCO_INFO;
    }

    @Override
    public boolean isBroadCast() { return true; }

    @Override
    public boolean isResponse() { return false; }

    @Override
    public String toString() {
        return super.toString() + " Addr=" + locoAddress + " InUse=" + locoInUse;
    }

    private int locoAddress;
    private boolean locoInUse;
    private int speedSteps;

    /**
     * Represents direction in which loco is driving. true = forward, false = backward
     */
    private boolean direction;

    private int speed;

    private boolean f0On, f1On, f2On, f3On, f4On, f5On, f6On, f7On, f8On, f9On, f10On, f11On, f12On, f13On, f14On,
        f15On, f16On, f17On, f18On, f19On, f20On, f21On, f22On, f23On, f24On, f25On, f26On, f27On, f28On;


    private void populateFields() {
        byte adr_MSB = z21DataRecord.raw[5];
        byte adr_LSB = z21DataRecord.raw[6];
        locoAddress = (adr_MSB & 0x3F) << 8 | adr_LSB;

        locoInUse = (z21DataRecord.raw[7] & 0x80) == 0x80;
        int speedStepsBits = z21DataRecord.raw[7] & 0x7F;
        switch (speedStepsBits) {
            case 0:
                speedSteps = 14;
                break;
            case 2:
                speedSteps = 28;
                break;
            default:
                speedSteps = 128;
                break;
        }

        direction = (z21DataRecord.raw[8] & 0x80) == 0x80;

        boolean[] db3bits = fromByte(z21DataRecord.raw[8]);
        boolean[] speedArray = db3bits.clone();

        if (direction) {
            speedArray = fromByte((byte) (z21DataRecord.raw[8] + 128));
        }

        speedArray[0] = false;
        speed =
            ((speedArray[0] ? 1 << 7 : 0) + (speedArray[1] ? 1 << 6 : 0) + (speedArray[2] ? 1 << 5 : 0)
                + (speedArray[3] ? 1 << 4 : 0) + (speedArray[4] ? 1 << 3 : 0) + (speedArray[5] ? 1 << 2 : 0)
                + (speedArray[6] ? 1 << 1 : 0) + (speedArray[7] ? 1 : 0));

        // Set all functions.
        // Not really a good design choice having so many variables...
        //// FIXME: 19.2.2016 one day when I have too much time change this into an array
        boolean[] db4bits = fromByte(z21DataRecord.raw[9]);
        f0On = db4bits[3];
        f1On = db4bits[7];
        f2On = db4bits[6];
        f3On = db4bits[5];
        f4On = db4bits[4];

        boolean[] db5bits = fromByte(z21DataRecord.raw[10]);
        f5On = db5bits[0];
        f6On = db5bits[1];
        f7On = db5bits[2];
        f8On = db5bits[3];
        f9On = db5bits[4];
        f10On = db5bits[5];
        f11On = db5bits[6];
        f12On = db5bits[7];

        boolean[] db6bits = fromByte(z21DataRecord.raw[11]);
        f13On = db6bits[0];
        f14On = db6bits[1];
        f15On = db6bits[2];
        f16On = db6bits[3];
        f17On = db6bits[4];
        f18On = db6bits[5];
        f19On = db6bits[6];
        f20On = db6bits[7];

        boolean[] db7bits = fromByte(z21DataRecord.raw[12]);
        f21On = db7bits[0];
        f22On = db7bits[1];
        f23On = db7bits[2];
        f24On = db7bits[3];
        f25On = db7bits[4];
        f26On = db7bits[5];
        f27On = db7bits[6];
        f28On = db7bits[7];
    }

    // Getters for all locomotive properties
    public boolean isLocoInUse() {
        return locoInUse;
    }

    public int getLocoAddress() {
        return locoAddress;
    }

    public int getSpeedSteps() {
        return speedSteps;
    }

    /**
     * Represents direction in which loco is driving. true = forward, false = backward
     * 
     * @return boolean following those rules
     */
    public boolean getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    // Getters for all them functions
    public boolean isF0On() {
        return f0On;
    }

    public boolean isF1On() {
        return f1On;
    }

    public boolean isF2On() {
        return f2On;
    }

    public boolean isF3On() {
        return f3On;
    }

    public boolean isF4On() {
        return f4On;
    }

    public boolean isF5On() {
        return f5On;
    }

    public boolean isF6On() {
        return f6On;
    }

    public boolean isF7On() {
        return f7On;
    }

    public boolean isF8On() {
        return f8On;
    }

    public boolean isF9On() {
        return f9On;
    }

    public boolean isF10On() {
        return f10On;
    }

    public boolean isF11On() {
        return f11On;
    }

    public boolean isF12On() {
        return f12On;
    }

    public boolean isF13On() {
        return f13On;
    }

    public boolean isF14On() {
        return f14On;
    }

    public boolean isF15On() {
        return f15On;
    }

    public boolean isF16On() {
        return f16On;
    }

    public boolean isF17On() {
        return f17On;
    }

    public boolean isF18On() {
        return f18On;
    }

    public boolean isF19On() {
        return f19On;
    }

    public boolean isF20On() {
        return f20On;
    }

    public boolean isF21On() {
        return f21On;
    }

    public boolean isF22On() {
        return f22On;
    }

    public boolean isF23On() {
        return f23On;
    }

    public boolean isF24On() {
        return f24On;
    }

    public boolean isF25On() {
        return f25On;
    }

    public boolean isF26On() {
        return f26On;
    }

    public boolean isF27On() {
        return f27On;
    }

    public boolean isF28On() {
        return f28On;
    }

    /**
     * Array of functions F0 to F12. I was too lazy to add all other functions.
     * 
     * @return Array of function values.
     */
    public boolean[] getFunctionsAsArray() {
        boolean[] array = new boolean[13];
        array[0] = f0On;
        array[1] = f1On;
        array[2] = f2On;
        array[3] = f3On;
        array[4] = f4On;
        array[5] = f5On;
        array[6] = f6On;
        array[7] = f7On;
        array[8] = f8On;
        array[9] = f9On;
        array[10] = f10On;
        array[11] = f11On;
        array[12] = f12On;
        return array;
    }
}
