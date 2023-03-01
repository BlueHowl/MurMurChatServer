package org.relay;

import org.sharedClients.TaskFactoryInterface;
import org.sharedClients.SharedRunnableInterface;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RelayRunnable implements Runnable, Closeable, SharedRunnableInterface {
    private final Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private final TaskFactoryInterface taskFactoryInterface;
    private final AESGCM aesgcm;

    public RelayRunnable(Socket client, TaskFactoryInterface taskHandlerInterface, AESGCM aesgcm) {
        this.client = client;
        this.taskFactoryInterface = taskHandlerInterface;
        this.aesgcm = aesgcm;

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String line = in.readLine();
            while (line != null) {
                aesgcm.decrypt(line);
                taskFactoryInterface.createTask(line, this);
                line = in.readLine();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void close() throws IOException {
        out.close();
        in.close();
        client.close();
    }

    @Override
    public void send(String command) {
        out.println(command);
    }
}
