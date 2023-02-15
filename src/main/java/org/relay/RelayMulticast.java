package org.relay;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RelayMulticast {

    public RelayMulticast(String multicastIP, int multicastPort, int relayPort, String domain) throws Exception {
        InetAddress ip = new InetSocketAddress(multicastIP, multicastPort).getAddress();

        MulticastRunnable multicastRunnable = new MulticastRunnable(new MulticastSocket(), ip, relayPort, domain);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(multicastRunnable, 0, 15, TimeUnit.SECONDS);
    }

}


class MulticastRunnable implements Runnable {

    public static final String ECHO = "ECHO %d %s\r\n";

    private final InetAddress multicastIP;
    private final int port;
    private final MulticastSocket socketEmission;
    private final String domain;

    MulticastRunnable(MulticastSocket socketEmission, InetAddress multicastIP, int port, String domain) throws Exception {
        this.multicastIP = multicastIP;
        this.port = port;
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
            content = String.format(ECHO, port, domain).getBytes();
            msg = new DatagramPacket(content, content.length, multicastIP, port);
            socketEmission.send(msg);

            System.out.println("Multicast sent " + String.format(ECHO, port, domain)); //todo debug
        } catch (Exception ignored) {}; //todo change ?

    }

}