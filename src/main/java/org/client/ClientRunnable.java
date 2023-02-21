package org.client;

import org.model.User;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ClientRunnable implements Runnable {

    private final SSLSocket client;
    private BufferedReader in;
    private PrintWriter out;
    private final TaskFactoryInterface taskHandlerInterface;
    private User user = null;

    public ClientRunnable (SSLSocket client, TaskFactoryInterface taskHandlerInterface) {
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
    public void send(String command) {
        out.println(command);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public List<String> getFollowers() {
        return user.getFollowers();
    }
}
