package org.sharedClients;

/**
 * Interface TaskList
 * Permet de récupérer les taches à exécuter
 */
public interface TaskFactoryInterface {

    /**
     * Cette méthode permet de récupérer la tache suivante à exécuter
     * @param command (String)
     * @param runnable (SharedRunnableInterface)
     */
     void createTask(String command, SharedRunnableInterface runnable);

}
