package z21Drive.utils;

public class ByteUtils {

    /**
     * Converts byte to bits
     * @param x byte to convert
     * @return array of bits
     */
    public static boolean [] fromByte(byte x){
        String binaryString = String.format("%8s", Integer.toString(x, 2)).replace(' ', '0');
        if (binaryString.charAt(1) == '1') {
            binaryString = binaryString.replace("-", "");
        }
        char [] binary = binaryString.toCharArray();

        boolean bs[] = new boolean[8];

        for (int i = 0; i < binary.length; i++){
            if (binary[i] == '0')
                bs[i] = false;
            else if (binary[i] == '1')
                bs[i] = true;
        }
        return bs;
    }

    public static final String byteToString(byte b) {
        return "0x" + String.format("%02X ", b);
    }

    public static final String byteArrayToString(byte[] array) {
        StringBuffer sb = new StringBuffer();
        if (array != null && array != null) {
            for (byte b : array) {
                sb.append(byteToString(b));
            }
        }
        return sb.toString();
    }
}
