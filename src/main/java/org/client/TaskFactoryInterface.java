package org.client;

/**
 * Interface TaskList
 * Permet de récupérer les taches à exécuter
 */
public interface TaskFactoryInterface {

    /**
     * Cette méthode permet de récupérer la tache suivante à exécuter
     * @param command (String)
     * @param clientRunnable (ClientRunnable)
     */
     void createTask(String command, ClientRunnable clientRunnable);

}
