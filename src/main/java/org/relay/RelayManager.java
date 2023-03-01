package org.relay;

import org.sharedClients.TaskFactoryInterface;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RelayManager implements Runnable {
    private RelayRunnable relay = null;
    private final TaskFactoryInterface taskHandlerInterface;
    private final RelayMulticast relayMulticast;
    private final int port;
    private final AESGCM aesgcm;

    public RelayManager(TaskFactoryInterface taskHandlerInterface, RelayMulticast relayMulticast, int port, AESGCM aesgcm) {
        this.taskHandlerInterface = taskHandlerInterface;
        this.relayMulticast = relayMulticast;
        this.port = port;
        this.aesgcm = aesgcm;
    }

    @Override
    public void run() {
        ServerSocketFactory ssf = ServerSocketFactory.getDefault();
        try (ServerSocket server = ssf.createServerSocket(port)) {
            System.out.println("Démarrage du serveur relai sur l'adresse " + server.getInetAddress() + " et le port " + port);

            while (true) {
                if(relay == null) { //tant qu'il n'y a pas un relai
                    final Socket client = server.accept();
                    RelayRunnable runnable = new RelayRunnable(client, taskHandlerInterface, aesgcm);
                    relay = runnable; //ajoute le nouveau client relai à la liste
                    relayMulticast.toggleMulticast(); //désactive le multicast
                    (new Thread(runnable)).start();
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
