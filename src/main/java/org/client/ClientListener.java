package org.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientListener implements Runnable {
    private List<ClientRunnable> clientsList;

    private Socket client;

    private TaskFactoryInterface taskHandlerInterface;

    private int port;

    private boolean isStarted = false;
    private boolean isConnected = false;

    public ClientListener(TaskFactoryInterface taskHandlerInterface, int port) {
        clientsList = Collections.synchronizedList(new ArrayList<>());
        this.taskHandlerInterface = taskHandlerInterface;
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Démarrage du serveur sur l'adresse " + server.getInetAddress() + " et le port " + port);

            while (true) {
                client = server.accept();
                ClientRunnable runnable = new ClientRunnable(client, taskHandlerInterface);
                clientsList.add(runnable);
                (new Thread(runnable)).start();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}