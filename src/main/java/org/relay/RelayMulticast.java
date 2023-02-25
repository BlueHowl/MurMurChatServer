package org.relay;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RelayMulticast {

    private ScheduledExecutorService executor;
    private MulticastRunnable multicastRunnable;

    private boolean isActive = false;

    /**
     * Crée un
     * @param multicastIP
     * @param multicastPort
     * @param relayPort
     * @param domain
     * @throws Exception
     */
    public RelayMulticast(String multicastIP, int multicastPort, int relayPort, String domain) throws Exception {
        multicastRunnable = new MulticastRunnable(new MulticastSocket(), InetAddress.getByName(multicastIP), multicastPort, relayPort, domain);

        executor = Executors.newScheduledThreadPool(1);
        toggleMulticast();
    }

    public void toggleMulticast() {
        if(isActive) {
            executor.shutdown();
        } else {
            executor.scheduleAtFixedRate(multicastRunnable, 0, 15, TimeUnit.SECONDS);
        }

        isActive = !isActive;
    }

}


class MulticastRunnable implements Runnable {

    public static final String ECHO = "ECHO %d %s\r\n";

    private final InetAddress multicastIP;
    private final int multicastPort;

    private final int relayPort;
    private final MulticastSocket socketEmission;
    private final String domain;

    MulticastRunnable(MulticastSocket socketEmission, InetAddress multicastIP, int multicastPort, int relayPort, String domain) throws Exception {
        this.multicastIP = multicastIP;
        this.multicastPort = multicastPort;
        this.relayPort = relayPort;
        this.domain = domain;
        this.socketEmission = socketEmission;
    }

    public void run() {
        try {

            byte[] content;
            DatagramPacket msg;

            //ByteArrayOutputStream output = new ByteArrayOutputStream();
            //(new DataOutputStream(output)).writeUTF(command);
            //content = output.toByteArray();
            content = String.format(ECHO, relayPort, domain).getBytes();
            msg = new DatagramPacket(content, content.length, multicastIP, multicastPort);
            socketEmission.send(msg);

            System.out.printf("Multicast[%s:%d] sent %s\n", multicastIP.toString(), multicastPort, String.format(ECHO, relayPort, domain)); //todo debug
        } catch (Exception ignored) {}; //todo change ?

    }

}