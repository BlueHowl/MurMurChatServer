package org.thread;

import org.server.Server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRunnable implements Runnable {
    private Socket monClient;
    private BufferedReader in;
    private PrintWriter out;

    public ClientRunnable (Socket client, Server server) {

    }
    @Override
    public void run() {

    }
}
