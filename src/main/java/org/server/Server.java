package org.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created by lsw on 02-02-16.
 */
public class Server {
    public static final int DEFAULT_PORT = 23502;

    public static final String DEFAULT_SERVER_NAME = "server1.godswila.guru";
    private boolean stop = false;
    private boolean isStarted = false;
    private boolean isConnected = false;

    public Server(int port) {

        ServerSocket ecoute = null;
        Socket client = null;

        try {
            ecoute = new ServerSocket(port);
            isStarted = true;
            System.out.println("Démarrage du serveur sur l'adresse " + ecoute.getInetAddress() + " et le port " + port);

            while(!stop) {
                client = ecoute.accept();
                isConnected = true;
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
                    ecoute.close();
            } catch(IOException ex) { }
        }
    }

    /**
     * Envoi le message hello {addresse serveur} {22 caractères aléatoires}
     * @param toClient (PrintWriter)
     */
    private void sendHello(PrintWriter toClient) {
        String hello = "HELLO " + DEFAULT_SERVER_NAME + " " + UUID.randomUUID().toString().replace("-", "").substring(0, 22) + "\r\n";
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
