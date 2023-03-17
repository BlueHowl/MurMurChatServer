package org.server;

import org.model.Task;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Cette classe permet de créer une liste de taches
 * elle permet de créer des taches
 * elle permet de récupérer la tache suivante à exécuter à l'aide de la méthode getTask()
 */
public class TaskList {
    private final Queue<Task> tasks;

    /**
     * Constructeur de la classe TaskList qui pour le moment ne prend rien en paramètres
     */
    public TaskList() {
        tasks = new LinkedBlockingDeque<>();
    }

    /**
     * Cette méthode permet de créer une tache
     * et de l'ajouter à la liste de taches
     * @param task (Task) tache
     */
    public void addTask(Task task) {
         tasks.add(task);
    }

    /**
     * Cette méthode permet de récupérer la tache suivante à exécuter
     * à l'aide du poll() qui va récupérer la tache et la retirer de la queue
     * @return Task retourne null si la queue est vide
     */
    public Task getTask() {
        return tasks.poll();
    }
}
