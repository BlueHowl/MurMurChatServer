package org.thread;

import org.model.Server;
import org.model.Task;
import org.task.TaskList;

/**
 * Cette classe permet de créer un thread qui va exécuter les taches
 */
public class MurMurExecutor implements Runnable{
    private final TaskListInterface taskListInterface;

    private final Server server;

    /**
     * Constructeur de la classe MurMurExecutor qui prend en paramètre une liste de taches
     * @param taskListInterface (TaskListInterface)
     */
    public MurMurExecutor(TaskListInterface taskListInterface, Server server) {
        this.taskListInterface = taskListInterface;
        this.server = server;
    }

    @Override
    public void run() {
        //TODO executer les taches si les taches ne sont pas null
        while (true){
            Task task = taskListInterface.getTask();
            if (task != null){
                task.execute();
            }
        }
    }

}
