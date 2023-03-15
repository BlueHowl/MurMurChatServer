package org.relay;

import org.sharedClients.TaskFactoryInterface;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RelayRunnable implements Runnable, Closeable {
    private final Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private final TaskFactoryInterface taskFactoryInterface;

    private final RelayManager relayManager;

    public RelayRunnable(Socket client, TaskFactoryInterface taskHandlerInterface, RelayManager relayManager) {
        this.client = client;
        this.taskFactoryInterface = taskHandlerInterface;
        this.relayManager = relayManager;

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
                taskFactoryInterface.createTask(line);
                line = in.readLine();
            }
        } catch (Exception e) {
            try {
                close();  //todo gèrer cas line == null
            } catch (IOException ignored) {}
        }
    }

    @Override
    public void close() throws IOException {
        out.close();
        in.close();
        client.close();
    }

    /**
     * Envoi la commande au relai
     * @param command (String)
     */
    public void send(String command) {
        System.out.println(String.format("Envoi relai : %s", command));
        out.println(command);
    }
}
