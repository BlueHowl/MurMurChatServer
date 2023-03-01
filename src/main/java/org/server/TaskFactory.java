package org.server;

import org.model.Task;
import org.sharedClients.SharedRunnableInterface;
import org.sharedClients.TaskFactoryInterface;
import org.utils.Regexes;

import java.util.Map;

public class TaskFactory implements TaskFactoryInterface {
    private int idCountTasks = 0;
    private final TaskList taskList;

    private final Regexes regexes;

    public TaskFactory(TaskList taskList) {
        this.taskList = taskList;
        this.regexes = Regexes.getInstance();
    }

    /**
     * Crée une tache sur base d'une commande et du thread client concerné
     * @param command (String)
     * @param runnable (SharedRunnableInterface)
     */
    public synchronized void createTask(String command, SharedRunnableInterface runnable) {
        Map<String, Object> commandInfos = regexes.decomposeCommand(command);
        taskList.addTask(new Task(++idCountTasks, commandInfos, runnable, "pending"));
    }

}
