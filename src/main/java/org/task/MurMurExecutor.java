package org.task;

import org.model.Task;

/**
 * Cette classe permet de créer un thread qui va exécuter les taches
 */
public class MurMurExecutor implements Runnable{
    private final TaskList taskList;

    /**
     * Constructeur de la classe MurMurExecutor qui prend en paramètre une liste de taches
     * @param taskList
     */
    public MurMurExecutor(TaskList taskList) {
        this.taskList = taskList;
    }

    @Override
    public void run() {
        //TODO executer les taches si les taches ne sont pas null
        while (true){
            Task task = taskList.getTask();
            if (task != null){
                task.execute();
            }
        }
    }

}
