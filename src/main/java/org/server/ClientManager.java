package org.server;

import org.client.ClientRunnable;
import org.sharedClients.TaskFactoryInterface;
import org.server.exception.CloseClientException;

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


    public ClientManager(TaskFactoryInterface taskHandlerInterface, int port) {
        clientsList = Collections.synchronizedList(new ArrayList<>());
        this.taskHandlerInterface = taskHandlerInterface;
        this.port = port;
    }

    @Override
    public void run() {
        SSLServerSocketFactory sslssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        try (SSLServerSocket server = (SSLServerSocket) sslssf.createServerSocket(port)) {
            System.out.println("Démarrage de l'écoute client sur l'adresse " + server.getInetAddress() + " et le port " + port);

            while (true) {
                final SSLSocket client = (SSLSocket) server.accept();
                ClientRunnable runnable = new ClientRunnable(client, taskHandlerInterface);
                clientsList.add(runnable);
                (new Thread(runnable)).start();

                System.out.println("Nombre de clients : " + clientsList.size());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Récupère le client correspondant au nom d'utilisateur
     * @param domain (String) domaine
     * @param name (String) utilisateur
     * @return ClientRunnable client (retourne null si pas trouvé)
     */
    public ClientRunnable getMatchingClient(String domain, String name) {

        for (ClientRunnable c : clientsList) {
            String username = String.format("%s@%s", c.getUsername(), domain);
            if(name.equals(username)) {
                return c;
            }
        }

        return null;
    }

    /**
     * Supprime un client de la liste
     * @param client (ClientRunnable)
     */
    public void removeClient(ClientRunnable client) throws CloseClientException {
        try {
            client.close();
            clientsList.remove(client);
        } catch (IOException e) {
            throw new CloseClientException("Erreur lors de la fermeture du client", e);
        }
    }
}