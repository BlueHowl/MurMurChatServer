package org.thread;

import org.server.Server;

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
            String key = randomString(SIZE);
            sendMessage("HELLO localhost " + key + "\r\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            String line = in.readLine();
            while(line != null) {
                System.out.printf("Read line: "+ line); //debug
                line = in.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void sendMessage(String message) {
        out.println(message);
    }
    private String randomString(int size) {
        String possibilites = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!%^.?@#&/*";
        StringBuilder random = new StringBuilder();
        for (int i = 0 ; i < size; i++) {
            int character = (int) (Math.random() * (size));
            random.append(possibilites.charAt(character));
        }
        return random.toString();
    }
}
