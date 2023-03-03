package org.sharedClients;

import org.client.ClientRunnable;

/**
 * Interface TaskFactory
 * Permet de créer une tache
 */
public interface TaskFactoryInterface {

    /**
     * Cette méthode permet de créer une tache via un thread client
     * @param command (String)
     * @param user (ClientRunnable)
     */
     void createTask(String command, ClientRunnable user);

    /**
     * Cette méthode permet de créer une tache via un thread relai
     * @param command (String)
     */
    void createTask(String command);

}
