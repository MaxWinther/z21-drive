package z21Drive;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import z21Drive.actions.Z21Action;
import z21Drive.actions.Z21ActionGetSerialNumber;
import z21Drive.actions.Z21ActionLanLogoff;
import z21Drive.broadcasts.BroadcastTypes;
import z21Drive.broadcasts.Z21Broadcast;
import z21Drive.broadcasts.Z21BroadcastLanXLocoInfo;
import z21Drive.broadcasts.Z21BroadcastLanXProgrammingMode;
import z21Drive.broadcasts.Z21BroadcastLanXShortCircuit;
import z21Drive.broadcasts.Z21BroadcastLanXTrackPowerOff;
import z21Drive.broadcasts.Z21BroadcastLanXTrackPowerOn;
import z21Drive.broadcasts.Z21BroadcastLanXUnknownCommand;
import z21Drive.broadcasts.Z21BroadcastListener;
import z21Drive.responses.ResponseTypes;
import z21Drive.responses.Z21Response;
import z21Drive.responses.Z21ResponseGetSerialNumber;
import z21Drive.responses.Z21ResponseLanXCVNACK;
import z21Drive.responses.Z21ResponseLanXCVResult;
import z21Drive.responses.Z21ResponseLanXGetFirmwareVersion;
import z21Drive.responses.Z21ResponseListener;
import z21Drive.responses.Z21ResponseRailcomDatachanged;

/**
 * Main class in this library which represents Z21 and handles all communication with it.
 * 
 * @author grizeldi
 */
public class Z21 implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Z21.class);

    private static final Logger LOGGER_RECEIVER = LoggerFactory.getLogger("Z21 Receiver");

    private static final Logger LOGGER_SENDER = LoggerFactory.getLogger("Z21 sender");

    private static final Logger LOGGER_MONITOR = LoggerFactory.getLogger("Z21 monitor");

    public static final Z21 instance = new Z21();

    private static final String host = "192.168.1.111";
    // private static final String host = "localhost";

    // private static final int port = 21105;

    private static final int serverPort = 21105;

    private static final int clientPort = 21206;

    private boolean exit = false;

    private List<Z21ResponseListener> responseListeners = new ArrayList<Z21ResponseListener>();

    private List<Z21BroadcastListener> broadcastListeners = new ArrayList<Z21BroadcastListener>();

    private DatagramSocket socket;

    private final Timer keepAliveTimer;

    private Z21() {
        LOGGER.info("Z21 initializing");
        Thread listenerThread = new Thread(this);
        try {
            socket = new DatagramSocket(clientPort, InetAddress.getByName("0.0.0.0"));
        }
        catch (SocketException | UnknownHostException e) {
            LOGGER.warn("Failed to open socket to Z21...", e);
        }
        listenerThread.setDaemon(true);
        listenerThread.start();
        addBroadcastListener(new Z21BroadcastListener() {
            @Override
            public void onBroadCast(BroadcastTypes type, Z21Broadcast broadcast) {
                if (type == BroadcastTypes.LAN_X_UNKNOWN_COMMAND)
                    LOGGER_MONITOR.warn("Z21 reported receiving an unknown command.");
                else
                    LOGGER_MONITOR
                        .error(
                            "Broadcast delivery messed up. Please report immediately to GitHub issues what have you done.");
            }

            @Override
            public BroadcastTypes[] getListenerTypes() {
                return new BroadcastTypes[] { BroadcastTypes.LAN_X_UNKNOWN_COMMAND };
            }
        });
        keepAliveTimer = new Timer(30000, e -> sendActionToZ21(new Z21ActionGetSerialNumber()));
        initKeepAliveTimer();
        // Make sure z21 shuts down communication gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    /**
     *
     * @param delay
     *            Delay for the KeepAliveTimer
     */
    public void setKeepAliveTimer(int delay) {
        keepAliveTimer.setInitialDelay(delay);
        keepAliveTimer.setDelay(delay);
        keepAliveTimer.restart();
    }

    private void initKeepAliveTimer() {
        keepAliveTimer.setRepeats(true);
        keepAliveTimer.start();
    }

    /**
     * Used to send the packet to z21.
     * 
     * @param action
     *            Action to send.
     * @return returns true if action is sent successfully and false if it fails
     */
    public synchronized boolean sendActionToZ21(Z21Action action) {

        DatagramPacket packet = PacketConverter.convert(action);
        try {
            InetAddress address = InetAddress.getByName(host);
            packet.setAddress(address);
            packet.setPort(serverPort);
            socket.send(packet);
        }
        catch (IOException e) {
            LOGGER_SENDER.warn("Failed to send message to z21... ", e);
            return false;
        }
        return true;
    }

    /**
     * Used as a listener for any packets sent by Z21. Also delivers packets to responseListeners.
     * 
     * @see Z21ResponseListener
     * @see Z21BroadcastListener
     */
    @Override
    public void run() {
        LOGGER_RECEIVER.info("Start the datagramm receiver.");

        byte[] receiveBuffer = new byte[512];

        while (!exit) {
            try {
                DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                LOGGER_RECEIVER.info("Wait for packet ...");
                socket.receive(packet);

                LOGGER_RECEIVER.info("Received a packet: {}", packet);

                // Determine if it's a response or a broadcast
                if (PacketConverter.responseFromPacket(packet) != null) {
                    Z21Response response = PacketConverter.responseFromPacket(packet);
                    for (Z21ResponseListener listener : responseListeners) {
                        for (ResponseTypes type : listener.getListenerTypes()) {
                            if (type == response.boundType) {
                                listener.responseReceived(type, response);
                            }
                        }
                    }
                }
                else {
                    Z21Broadcast broadcast = PacketConverter.broadcastFromPacket(packet);
                    if (broadcast != null) {
                        for (Z21BroadcastListener listener : broadcastListeners) {
                            for (BroadcastTypes type : listener.getListenerTypes()) {
                                if (type == broadcast.boundType) {
                                    listener.onBroadCast(type, broadcast);
                                }
                            }
                        }
                    }
                }
            }
            catch (IOException e) {
                if (!exit)
                    LOGGER_RECEIVER.warn("Failed to get a message from z21... ", e);
            }
        }

        LOGGER_RECEIVER.info("The receiver has terminated.");
    }

    public void addResponseListener(Z21ResponseListener listener) {
        responseListeners.add(listener);
    }

    public void removeResponseListener(Z21ResponseListener listener) {
        if (responseListeners.contains(listener))
            responseListeners.remove(listener);
    }

    public void addBroadcastListener(Z21BroadcastListener listener) {
        broadcastListeners.add(listener);
    }

    public void removeBroadcastListener(Z21BroadcastListener listener) {
        if (broadcastListeners.contains(listener))
            broadcastListeners.remove(listener);
    }

    /**
     * Used to gracefully stop all communications.
     */
    public void shutdown() {
        LOGGER.info("Shutting down all communication.");
        sendActionToZ21(new Z21ActionLanLogoff());
        keepAliveTimer.stop();
        exit = true;
        socket.close();
    }

    @Override
    protected void finalize() throws Throwable {
        shutdown();
        super.finalize();
    }

    /**
     * Converting packets to objects and back.
     */
    private static class PacketConverter {
        static DatagramPacket convert(Z21Action action) {

            byte[] packetContent = action.getByteRepresentation().array();
            int len = action.getByteRepresentation().position();
            return new DatagramPacket(packetContent, len + 2);
        }

        /**
         * Here the magic of turning bytes into objects happens.
         * 
         * @param packet
         *            UDP packet received from Z21
         * @return Z21 response object which represents the byte array.
         */
        static Z21Response responseFromPacket(DatagramPacket packet) {
            byte[] array = packet.getData();
            byte header1 = array[2], header2 = array[3];
            int xHeader = array[4] & 255;
            if (header1 == 0x10 && header2 == 0x00)
                return new Z21ResponseGetSerialNumber(array);
            else if (header1 == 0x40 && header2 == 0x00 && xHeader == 0xF1)
                return new Z21ResponseLanXGetFirmwareVersion(array);
            else if (header1 == 0x40 && header2 == 0x00 && xHeader == 0x64 && (array[5] & 255) == 0x14)
                return new Z21ResponseLanXCVResult(array);
            else if (header1 == 0x40 && header2 == 0x00 && xHeader == 0x61 && (array[5] & 255) == 0x13)
                return new Z21ResponseLanXCVNACK(array);
            else if ((header1 & 0xFF) == 0x88 && header2 == 0x00)
                return new Z21ResponseRailcomDatachanged(array);
            return null;
        }

        /**
         * Same as for responses, but for broadcasts. See method responseFromPacket(DatagramPacket packet).
         * 
         * @param packet
         *            UDP packet received from Z21
         * @return Z21 broadcast object which represents the broadcast sent from Z21.
         */
        static Z21Broadcast broadcastFromPacket(DatagramPacket packet) {
            byte[] data = packet.getData();
            // Get headers
            byte header1 = data[2], header2 = data[3];
            int xHeader = data[4] & 255;
            // Discard all zeros
            byte[] newArray = new byte[data[0]];
            System.arraycopy(data, 0, newArray, 0, newArray.length);
            if (data[data[0] + 1] != 0) {
                // We got two messages in one packet.
                // Don't know yet what to do. TODO
                LOGGER_RECEIVER
                    .info(
                        "Received two messages in one packet. Multiple messages not supported yet. Please report to github.");
            }

            if (header1 == 0x40 && header2 == 0x00 && xHeader == 239)
                return new Z21BroadcastLanXLocoInfo(newArray);
            else if (header1 == 0x40 && header2 == 0x00 && xHeader == 0x61 && (data[5] & 255) == 0x82)
                return new Z21BroadcastLanXUnknownCommand(newArray);
            else if (header1 == 0x40 && header2 == 0x00 && xHeader == 0x61 && (data[5] & 255) == 0x00)
                return new Z21BroadcastLanXTrackPowerOff(newArray);
            else if (header1 == 0x40 && header2 == 0x00 && xHeader == 0x61 && (data[5] & 255) == 0x01)
                return new Z21BroadcastLanXTrackPowerOn(newArray);
            else if (header1 == 0x40 && header2 == 0x00 && xHeader == 0x61 && (data[5] & 255) == 0x02)
                return new Z21BroadcastLanXProgrammingMode(newArray);
            else if (header1 == 0x40 && header2 == 0x00 && xHeader == 0x61 && (data[5] & 255) == 0x08)
                return new Z21BroadcastLanXShortCircuit(newArray);
            else {
                LOGGER_RECEIVER.warn("Received unknown message. Array:");
                for (byte b : newArray)
                    System.out.print("0x" + String.format("%02X ", b));
                System.out.println();
            }
            return null;
        }

        // /**
        // * Unboxes Byte array to a primitive byte array.
        // *
        // * @param in
        // * Byte array to primitivize
        // * @return primitivized array
        // */
        // private static byte[] toPrimitive(Byte[] in) {
        // byte[] out = new byte[in.length];
        // int i = 0;
        // for (Byte b : in)
        // out[i++] = b.byteValue();
        // return out;
        // }
    }

}
