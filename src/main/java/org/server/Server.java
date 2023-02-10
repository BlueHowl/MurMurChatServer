package org.server;

import org.task.TaskFactory;
import org.task.TaskList;
import org.thread.ClientRunnable;
import org.utils.Queries;
import org.utils.RandomStringUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lsw on 02-02-16.
 */
public class Server {
    private List<ClientRunnable> clientsList;
    private boolean isStarted = false;
    private boolean isConnected = false;
    private TaskFactory taskFactory;
    private TaskList taskList;

    public Server(int port) {

        clientsList = Collections.synchronizedList(new ArrayList<>());
        ServerSocket server = null;
        Socket client = null;
        TaskFactory taskFactory;
        taskList = new TaskList();

        try {

            server = new ServerSocket(port);
            isStarted = true;
            System.out.println("Démarrage du serveur sur l'adresse " + server.getInetAddress() + " et le port " + port);

            while(true) {
                client = server.accept();
                isConnected = true;
                ClientRunnable runnable = new ClientRunnable(client, this);
                clientsList.add(runnable);
                (new Thread(runnable)).start();

                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter toClient = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
                String ligne = readLine(fromClient);
                System.out.println("Ligne reçue: " + (ligne==null?"":ligne));

                sendHello(toClient);

                client.close();
                isConnected = false;
            }

        } catch(IOException exception) {
            exception.printStackTrace();

        } finally {
            try {
                if (isConnected)
                    client.close();
                if (isStarted)
                    server.close();
            } catch(IOException ex) { }
        }
    }

    /**
     * Envoi le message hello {addresse serveur} {22 caractères aléatoires}
     * @param toClient (PrintWriter)
     */
    private void sendHello(PrintWriter toClient) {
        String hello = String.format(Queries.HELLO, "", RandomStringUtil.generateString(22));
        System.out.println(hello); //DEBUG
        toClient.print(hello);

        toClient.flush();
    }

    private String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if(line != null && line.length() > 2 && line.startsWith("\uFEFF"))
            return line.substring("\uFEFF".length());
        return line;
    }
}
