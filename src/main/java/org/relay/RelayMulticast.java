package org.relay;

import org.model.ServerSettings;

import java.net.*;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RelayMulticast {

    private ScheduledExecutorService executor;
    private MulticastRunnable multicastRunnable;

    private boolean isActive = false;

    /**
     * Crée un
     * @param multicastPort
     * @param relayPort
     * @param domain
     * @param net
     * @throws Exception
     */
    public RelayMulticast(int multicastPort, int relayPort, String domain, NetworkInterface net, String address) throws Exception {
        MulticastSocket socketEmission = new MulticastSocket();
        socketEmission.setNetworkInterface(net);
        multicastRunnable = new MulticastRunnable(socketEmission, multicastPort, relayPort, domain, InetAddress.getByName(address));
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(multicastRunnable, 0, 15, TimeUnit.SECONDS);
    }

}


class MulticastRunnable implements Runnable {

    public static final String ECHO = "ECHO %d %s\r\n";

    private InetAddress multicastIP;
    private final int multicastPort;

    private final int relayPort;
    private final MulticastSocket socketEmission;
    private final String domain;

    MulticastRunnable(MulticastSocket socketEmission, int multicastPort, int relayPort, String domain, InetAddress address) throws Exception {
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

            //ByteArrayOutputStream output = new ByteArrayOutputStream();
            //(new DataOutputStream(output)).writeUTF(command);
            //content = output.toByteArray();
            content = String.format(ECHO, relayPort, domain).getBytes();
            msg = new DatagramPacket(content, content.length, multicastIP, multicastPort);
            socketEmission.send(msg);

            System.out.printf("Multicast[%s:%d] sent %s\n", multicastIP.toString(), multicastPort, String.format(ECHO, relayPort, domain));
        } catch (Exception ignored) {}; //todo change ?

    }

}