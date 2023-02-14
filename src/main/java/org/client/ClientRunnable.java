package org.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientRunnable implements Runnable {

    private final Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private final TaskFactoryInterface taskHandlerInterface;

    public ClientRunnable (Socket client, TaskFactoryInterface taskHandlerInterface) {
        this.client = client;
        this.taskHandlerInterface = taskHandlerInterface;

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
            taskHandlerInterface.createTask("HELLO ",this);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            String line = in.readLine();
            while (line != null) {
                taskHandlerInterface.createTask(line, this);
                line = in.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Permet de renvoyer une commande au client
     * @param command (String) Commande
     */
    public void sendMessage(String command) {
        out.println(command);
    }
}
