package z21Drive.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import z21Drive.Z21;
import z21Drive.actions.Z21ActionGetSerialNumber;
import z21Drive.record.Z21Record;
import z21Drive.record.Z21RecordType;
import z21Drive.record.Z21RecordGetSerialNumber;
import z21Drive.responses.Z21ResponseListener;

/**
 * Send the get Serial Number Request
 * 
 * @see z21Drive.Z21
 */
public class Serialnumber implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Serialnumber.class);

    public static void main(String[] args) {
        // Start things up
        new Thread(new Serialnumber()).start();
    }

    @Override
    public void run() {
        LOGGER.info("Run ...");
        final Z21 z21 = Z21.instance;

        final Object resultLock = new Object();

        z21.addResponseListener(new Z21ResponseListener() {
            @Override
            public void responseReceived(Z21RecordType type, Z21Record response) {

                LOGGER.info("Received response: {}", response);

                if (type != Z21RecordType.LAN_GET_SERIAL_NUMBER_RESPONSE) {
                    return;
                }
                Z21RecordGetSerialNumber number = (Z21RecordGetSerialNumber) response;
                LOGGER.info("Received Response: {}", number.serialNumber);

                synchronized (resultLock) {
                    resultLock.notifyAll();
                }

            }

            @Override
            public Z21RecordType[] getListenerTypes() {
                return new Z21RecordType[] { Z21RecordType.LAN_GET_SERIAL_NUMBER_RESPONSE };
            };
        });

        LOGGER.info("Send the GetSerialNumber action to the server.");

        z21.sendActionToZ21(new Z21ActionGetSerialNumber());

        synchronized (resultLock) {
            LOGGER.info("Wait for serial number from Z21.");
            try {
                resultLock.wait(50000);
            }
            catch (InterruptedException ex) {
                LOGGER.warn("Wait for serial number failed.", ex);
            }
        }

    }
}
