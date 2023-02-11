package org.thread;

import org.server.Server;
import org.utils.Queries;
import org.utils.RandomStringUtil;
import org.utils.Regexes;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientRunnable implements Runnable {
    private static final int SIZE = 22;
    private final Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private final Server server;

    public ClientRunnable (Socket client, Server server) {
        this.client = client;
        this.server = server;

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
            String key = RandomStringUtil.generateString(SIZE);
            sendMessage(String.format(Queries.HELLO, "localhost", key));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            String line;
            do {
                line = in.readLine();

                Regexes.decomposeRegister(line);
                Regexes.decomposeConnect(line);
            } while (line != null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void sendMessage(String message) {
        out.println(message);
    }
}
