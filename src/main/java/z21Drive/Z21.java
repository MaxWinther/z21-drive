package z21Drive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import z21Drive.actions.Z21Action;
import z21Drive.actions.Z21ActionGetSerialNumber;
import z21Drive.actions.Z21ActionLanLogoff;
import z21Drive.broadcasts.Z21BroadcastListener;
import z21Drive.record.Z21Record;
import z21Drive.record.Z21RecordFactory;
import z21Drive.record.Z21RecordType;
import z21Drive.responses.Z21ResponseListener;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

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

    //Sommerhus
    //private static final String host = "192.168.8.197";

    //Direct connected
    //private static final String host = "192.168.111.111";


    //Home
    private static final String host = "10.76.215.157";
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

        } catch (SocketException | UnknownHostException e) {
            LOGGER.warn("Failed to open socket to Z21...", e);
        }

        listenerThread.setDaemon(true);
        listenerThread.start();

        addBroadcastListener(new Z21BroadcastListener() {
            @Override
            public void onBroadCast(Z21RecordType type, Z21Record broadcast) {
                if (type == Z21RecordType.LAN_X_UNKNOWN_COMMAND)
                    LOGGER_MONITOR.warn("Z21 reported receiving an unknown command.");
                else
                    LOGGER_MONITOR
                        .error(
                            "Broadcast delivery messed up. Please report immediately to GitHub issues what have you done.");
            }

            @Override
            public Z21RecordType[] getListenerTypes() {
                return new Z21RecordType[] { Z21RecordType.LAN_X_UNKNOWN_COMMAND };
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
            LOGGER_SENDER.info("Send action on " + host + ":" + serverPort);
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
                z21Drive.record.Z21Record z21Record = Z21RecordFactory.fromPacket(packet);

                // Determine if it's a response or a broadcast
                if (z21Record != null) {
                    LOGGER_RECEIVER.info("Received '{}'", z21Record);
                    if (z21Record.isResponse()) {
                        for (Z21ResponseListener listener : responseListeners) {
                            for (Z21RecordType type : listener.getListenerTypes()) {
                                if (type == z21Record.getRecordType()) {
                                    listener.responseReceived(type, z21Record);
                                }
                            }
                        }
                    } else if (z21Record.isBroadCast()) {
                        for (Z21BroadcastListener listener : broadcastListeners) {
                            for (Z21RecordType type : listener.getListenerTypes()) {
                                if (type == z21Record.getRecordType()) {
                                    listener.onBroadCast(type, z21Record);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
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
    }
}
