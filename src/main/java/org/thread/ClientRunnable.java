package org.thread;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientRunnable implements Runnable {

    private final Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private final TaskHandlerInterface taskHandlerInterface;

    public ClientRunnable (Socket client, TaskHandlerInterface taskHandlerInterface) {
        this.client = client;
        this.taskHandlerInterface = taskHandlerInterface;

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
            taskHandlerInterface.createHello(out);
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

                taskHandlerInterface.processTask(line, out);
            } while (line != null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
