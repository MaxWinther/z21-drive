package z21Drive.actions;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import z21Drive.LocoAddressOutOfRangeException;

class Z21ActionLanXCVWriteTest {

    @Test
    void testCvWrite() throws LocoAddressOutOfRangeException {

        final Z21ActionLanXCVWrite cvWrite = new Z21ActionLanXCVWrite(1, 10);
        byte[] data = cvWrite.getByteRepresentation().array();
        int len = cvWrite.getByteRepresentation().position();

        Assertions.assertThat(len).isEqualTo(0x0A);
        Assertions.assertThat(data[6] & 0xFF).isEqualTo(0x00);
        Assertions.assertThat(data[7] & 0xFF).isEqualTo(0x00); // CV value -1 ... CV1 is 0

        Assertions.assertThat(data[8] & 0xFF).isEqualTo(10); // value
    }

    @Test
    void testCvWriteCV4() throws LocoAddressOutOfRangeException {

        final Z21ActionLanXCVWrite cvWrite = new Z21ActionLanXCVWrite(4, 20);
        byte[] data = cvWrite.getByteRepresentation().array();
        int len = cvWrite.getByteRepresentation().position();

        Assertions.assertThat(len).isEqualTo(0x0A);
        Assertions.assertThat(data[6] & 0xFF).isEqualTo(0x00);
        Assertions.assertThat(data[7] & 0xFF).isEqualTo(0x03); // CV value -1 ... CV1 is 0

        Assertions.assertThat(data[8] & 0xFF).isEqualTo(20); // value
    }

}
