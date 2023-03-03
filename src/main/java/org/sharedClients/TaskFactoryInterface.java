package org.sharedClients;

/**
 * Interface TaskFactory
 * Permet de créer une tache
 */
public interface TaskFactoryInterface {

    /**
     * Cette méthode permet de créer une tache via un thread client
     * @param command (String)
     * @param username (String)
     */
     void createTask(String command, String username);

    /**
     * Cette méthode permet de créer une tache via un thread relai
     * @param command (String)
     */
    void createTask(String command);

}
