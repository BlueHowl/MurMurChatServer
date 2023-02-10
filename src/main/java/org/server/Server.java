package org.server;

import org.task.TaskFactory;
import org.task.TaskList;
import org.thread.ClientRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
        Socket client = null;
        taskList = new TaskList();
        TaskFactory taskFactory = new TaskFactory(taskList);

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Démarrage du serveur sur l'adresse " + server.getInetAddress() + " et le port " + port);

            while(true) {
                client = server.accept();
                ClientRunnable runnable = new ClientRunnable(client, this);
                clientsList.add(runnable);
                (new Thread(runnable)).start();
            }

        } catch(IOException exception) {
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
