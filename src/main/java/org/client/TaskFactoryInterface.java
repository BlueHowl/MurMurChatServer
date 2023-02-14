package org.client;

import java.io.PrintWriter;

/**
 * Interface TaskList
 * Permet de récupérer les taches à exécuter
 */
public interface TaskFactoryInterface {

    /**
     * Cette méthode permet de récupérer la tache suivante à exécuter
     * @param command (String)
     * @param client (ClientRunnable)
     */
     void createTask(String command, ClientRunnable client);

}
