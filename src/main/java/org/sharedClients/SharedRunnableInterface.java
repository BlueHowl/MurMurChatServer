package org.sharedClients;

public interface SharedRunnableInterface {
    /**
     * Permet de renvoyer une commande au client
     * @param command (String) Commande
     */
    public void send(String command);

}
