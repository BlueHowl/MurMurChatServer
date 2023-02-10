package org.thread;

import org.server.Server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRunnable implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private Server server;

    public ClientRunnable (Socket client, Server server) {
        this.client = client;
        this.server = server;
    }
    @Override
    public void run() {

    }

    public void sendTaskEvent() {

    }
}
