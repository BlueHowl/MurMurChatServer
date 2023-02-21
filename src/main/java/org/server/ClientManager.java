package org.server;

import org.client.ClientRunnable;
import org.client.TaskFactoryInterface;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientManager implements Runnable {
    private final List<ClientRunnable> clientsList;

    private final TaskFactoryInterface taskHandlerInterface;

    private final int port;

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

    /**
     * Récupère les clients correspondants à la liste d'utilisateur donnée
     * @return (List<ClientRunnable>) liste de clients
     */
    public List<ClientRunnable> getMatchingClients(String domain, List<String> followers) {//TODO synchronized ?
        List<ClientRunnable> clients = new ArrayList<>();

        for (ClientRunnable c : clientsList) {
            if(followers.contains(String.format("%s@%s", c.getUsername(), domain))) {
                clients.add(c);
            }
        }

        return clients;
    }
}