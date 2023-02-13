package org.thread;

import java.io.PrintWriter;

/**
 * Interface TaskList
 * Permet de récupérer les taches à exécuter
 */
public interface TaskHandlerInterface {
    /**
     * Cette méthode permet de récupérer la tache suivante à exécuter
     * @return Task retourne null si plus de tache à exécuter
     */
     void processTask(String line, PrintWriter destination);
     void createHello(PrintWriter destination);
}
