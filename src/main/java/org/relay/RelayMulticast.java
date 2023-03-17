package org.relay;


import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RelayMulticast {
    /**
     * Crée un émetteur multicast
     * @param multicastPort (int)
     * @param relayPort (int)
     * @param domain (String)
     * @param net (NetworkInterface)
     * @throws Exception e
     */
    public RelayMulticast(int multicastPort, int relayPort, String domain, NetworkInterface net, String address) throws Exception {
        MulticastSocket socketEmission = new MulticastSocket();
        socketEmission.setNetworkInterface(net);
        MulticastRunnable multicastRunnable = new MulticastRunnable(socketEmission, multicastPort, relayPort, domain, InetAddress.getByName(address));
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(multicastRunnable, 0, 15, TimeUnit.SECONDS);
    }

}


class MulticastRunnable implements Runnable {

    public static final String ECHO = "ECHO %d %s\r\n";

    private final InetAddress multicastIP;
    private final int multicastPort;

    private final int relayPort;
    private final MulticastSocket socketEmission;
    private final String domain;

    MulticastRunnable(MulticastSocket socketEmission, int multicastPort, int relayPort, String domain, InetAddress address) {
        this.multicastPort = multicastPort;
        this.relayPort = relayPort;
        this.domain = domain;
        this.socketEmission = socketEmission;
        this.multicastIP = address;
    }

    public void run() {
        try {

            byte[] content;
            DatagramPacket msg;

            content = String.format(ECHO, relayPort, domain).getBytes();
            msg = new DatagramPacket(content, content.length, multicastIP, multicastPort);
            socketEmission.send(msg);

            System.out.printf("Multicast[%s:%d] sent %s\n", multicastIP.toString(), multicastPort, String.format(ECHO, relayPort, domain));
        } catch (Exception e) {
            System.out.println("Failed to send multicast message");
        }

    }

}