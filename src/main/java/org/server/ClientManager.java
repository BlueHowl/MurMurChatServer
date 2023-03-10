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
            System.out.println("Démarrage du serveur client sur l'adresse " + server.getInetAddress() + " et le port " + port);

            while (true) {
                final SSLSocket client = (SSLSocket) server.accept(); //todo implement auto-closeable ? Comment gèrer la déconnexion timeout
                ClientRunnable runnable = new ClientRunnable(client, taskHandlerInterface);
                clientsList.add(runnable);
                (new Thread(runnable)).start();

                System.out.println("Nombre de clients : " + clientsList.size());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /*/** todo remove
     * Récupère les clients correspondants à la liste d'utilisateur donnée
     * et supprime de la liste de followers les utilisateurs ajoutés au resultat
     * @param domain (String) domaine
     * @param followers (List<String>) liste d'utilisateurs
     * @return (List<ClientRunnable>) liste de clients
     *//*
    public List<ClientRunnable> getMatchingClients(String domain, List<String> followers) {
        List<ClientRunnable> clients = new ArrayList<>();

        for (ClientRunnable c : clientsList) {
            String username = String.format("%s@%s", c.getUsername(), domain);
            if(followers.contains(username)) {
                clients.add(c);
                followers.remove(username);
            }
        }

        return clients;
    }*/

    /**
     * Récupère les clients correspondants à la liste d'utilisateur donnée
     * et supprime de la liste de followers les utilisateurs ajoutés au resultat
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
        try { //todo stop le thread ? comment faire quand le client est fermé brutalement (timeout)
            client.close();
            clientsList.remove(client);
        } catch (IOException e) {
            throw new CloseClientException("Erreur lors de la fermeture du client", e);
        }
    }
}