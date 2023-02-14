package org.server;

import org.model.Task;
import org.client.ClientRunnable;
import org.client.TaskFactoryInterface;
import org.utils.Regexes;

import java.util.Map;

public class TaskFactory implements TaskFactoryInterface {
    private int idCountTasks = 0;
    private final TaskList taskList;

    public TaskFactory(TaskList taskList) {
        this.taskList = taskList;
    }

    /**
     * Crée une tache sur base d'une commande et du thread client concerné
     * @param command (String)
     * @param client (ClientRunnable)
     */
    public synchronized void createTask(String command, ClientRunnable client) {
        Map<String, String> commandInfos = Regexes.decomposeCommand(command);
        taskList.addTask(new Task(++idCountTasks, commandInfos, client, "pending"));
    }

}
