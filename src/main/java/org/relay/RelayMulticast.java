package org.relay;

import org.utils.Queries;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RelayMulticast {
    private final InetAddress multicastIP;
    private final int relayPort;
    private final MulticastSocket socketEmission;
    private final String domain;

    public RelayMulticast(String multicastIP, int multicastPort, int relayPort, String domain) throws Exception {
        this.multicastIP = new InetSocketAddress(multicastIP, multicastPort).getAddress();
        this.relayPort = relayPort;
        this.domain = domain;
        socketEmission = new MulticastSocket();

        MulticastRunnable multicastRunnable = new MulticastRunnable(this.multicastIP, relayPort, domain);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(multicastRunnable, 0, 15, TimeUnit.SECONDS);
    }

}


class MulticastRunnable implements Runnable {
    private final InetAddress multicastIP;
    private final int port;
    private final MulticastSocket socketEmission;
    private final String domain;

    MulticastRunnable(InetAddress multicastIP, int port, String domain) throws Exception {
        this.multicastIP = multicastIP;
        this.port = port;
        this.domain = domain;
        socketEmission = new MulticastSocket();
    }

    public void run() {
        try {

            byte[] content;
            DatagramPacket msg;

            //ByteArrayOutputStream output = new ByteArrayOutputStream();
            //(new DataOutputStream(output)).writeUTF(command);
            //content = output.toByteArray();
            content = String.format(Queries.ECHO, port, domain).getBytes();
            msg = new DatagramPacket(content, content.length, multicastIP, port);
            socketEmission.send(msg);

            System.out.println("Multicast sent " + String.format(Queries.ECHO, port, domain)); //todo debug
        } catch (Exception ignored) {}; //todo change ?

    }

}