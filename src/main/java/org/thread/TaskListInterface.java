package org.thread;

import org.model.Task;

/**
 * Interface TaskList
 * Permet de récupérer les taches à exécuter
 */
public interface TaskListInterface {

    /**
     * Cette méthode permet de récupérer la tache suivante à exécuter
     * @return Task retourne null si plus de tache à exécuter
     */
    Task getTask();

}
