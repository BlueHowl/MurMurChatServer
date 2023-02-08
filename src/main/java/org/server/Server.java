package org.server;

import org.thread.ClientRunnable;
import org.utils.RandomStringUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by lsw on 02-02-16.
 */
public class Server {
    public static final int DEFAULT_PORT = 23502;
    private final String DEFAULT_SERVER_NAME = "server1.godswila.guru";
    private List<ClientRunnable> clientsList;
    private boolean isStarted = false;
    private boolean isConnected = false;

    public Server(int port) {

        clientsList = Collections.synchronizedList(new ArrayList<>());
        ServerSocket server = null;
        Socket client = null;

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

                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream(), Charset.forName("UTF-8")));
                PrintWriter toClient = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), Charset.forName("UTF-8")), true);
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
        String hello = "HELLO " + DEFAULT_SERVER_NAME + " " + RandomStringUtil.generateString(22) + "\r\n";
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

    public static void main(String[] args) {
        if(args.length == 0)
            new Server(DEFAULT_PORT);
        else
            new Server(Integer.parseInt(args[0]));

    }
}
