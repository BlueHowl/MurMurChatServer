package org.server;

import org.client.ClientRunnable;
import org.client.TaskFactoryInterface;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientManager implements Runnable {
    private List<ClientRunnable> clientsList;

    private TaskFactoryInterface taskHandlerInterface;

    private int port;

    private boolean isStarted = false;
    private boolean isConnected = false;

    public ClientManager(TaskFactoryInterface taskHandlerInterface, int port) {
        clientsList = Collections.synchronizedList(new ArrayList<>());
        this.taskHandlerInterface = taskHandlerInterface;
        this.port = port;
    }

    @Override
    public void run() {
        SSLServerSocketFactory sslssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        try (SSLServerSocket server = (SSLServerSocket) sslssf.createServerSocket(port)) {
            System.out.println("Démarrage du serveur sur l'adresse " + server.getInetAddress() + " et le port " + port);

            while (true) {
                final SSLSocket client = (SSLSocket) server.accept();
                ClientRunnable runnable = new ClientRunnable(client, taskHandlerInterface);
                clientsList.add(runnable);
                (new Thread(runnable)).start();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}