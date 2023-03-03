package org.client;

import org.sharedClients.TaskFactoryInterface;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ClientRunnable implements Runnable, Closeable {

    private final SSLSocket client;
    private BufferedReader in;
    private PrintWriter out;
    private final TaskFactoryInterface taskFactoryInterface;
    private String username = null;

    public ClientRunnable (SSLSocket client, TaskFactoryInterface taskHandlerInterface) {
        this.client = client;
        this.taskFactoryInterface = taskHandlerInterface;

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
            taskHandlerInterface.createTask("HELLO ", username);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    @Override
    public void run() {
        try {
            String line = in.readLine();
            while (line != null) { //todo gèrer déconnexion quand line == null
                taskFactoryInterface.createTask(line, username);
                line = in.readLine();
            }
        } catch (IOException e) {
            System.out.println("Lost client connection"); //todo debug
            taskFactoryInterface.createTask("DISCONNECT", username); //todo meilleur façon ?
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
