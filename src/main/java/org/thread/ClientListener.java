package org.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientListener implements Runnable {
    private List<ClientRunnable> clientsList;

    private Socket client;

    private TaskFactoryInterface taskFactoryInterface;

    private int port;

    private boolean isStarted = false;
    private boolean isConnected = false;

    public ClientListener(TaskFactoryInterface taskFactoryInterface, int port) {
        clientsList = Collections.synchronizedList(new ArrayList<>());
        this.taskFactoryInterface = taskFactoryInterface;
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Démarrage du serveur sur l'adresse " + server.getInetAddress() + " et le port " + port);

            while (true) {
                client = server.accept();
                ClientRunnable runnable = new ClientRunnable(client, taskFactoryInterface);
                clientsList.add(runnable);
                (new Thread(runnable)).start();
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if(line != null && line.length() > 2 && line.startsWith("\uFEFF"))
            return line.substring("\uFEFF".length());
        return line;
    }

}