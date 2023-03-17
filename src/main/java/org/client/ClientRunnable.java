package org.client;

import org.model.User;
import org.sharedClients.TaskFactoryInterface;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class ClientRunnable implements Runnable, Closeable {

    private final SSLSocket client;
    private BufferedReader in;
    private PrintWriter out;
    private final TaskFactoryInterface taskFactoryInterface;
    private User user = null;

    private String connectionKey;

    public ClientRunnable (SSLSocket client, TaskFactoryInterface taskHandlerInterface) {
        this.client = client;
        this.taskFactoryInterface = taskHandlerInterface;

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
            taskHandlerInterface.createTask("HELLO ", this);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    @Override
    public void run() {
        try {
            String line = in.readLine();
            while (line != null) {
                taskFactoryInterface.createTask(line, this);
                line = in.readLine();
            }
        } catch (IOException e) {
            System.out.println("Lost a client connection");
            taskFactoryInterface.createTask("DISCONNECT", this);
        }
    }

    @Override
    public void close() throws IOException{
        out.close();
        in.close();
        client.close();
    }

    /**
     * Permet de renvoyer une commande au client
     * @param command (String) Commande
     */
    public void send(String command) {
        out.println(command);
    }

    /**
     * Défini l'objet utilisateur correspondant
     * @param user (User)
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Récupère l'objet utilisateur correspondant
     * @return (User)
     */
    public User getUser() {
        return user;
    }

    /**
     * Récupère la clé de connexion random22
     * @return (String)
     */
    public String getConnectionKey() {
        return connectionKey;
    }

    /**
     * Défini la clé de connexion random22
     * @param connectionKey (String)
     */
    public void setConnectionKey(String connectionKey) {
        this.connectionKey = connectionKey;
    }

    /**
     * Récupère le nom d'utilisateur
     * @return (String) username
     */
    public String getUsername() {
        if(user == null)
            return "";

        return user.getUsername();
    }

    /**
     * Récupère les followers
     * @return (Set<String>) followers
     */
    public Set<String> getFollowers() {
        return user.getFollowers();
    }

}
