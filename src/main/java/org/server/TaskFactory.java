package org.server;

import org.model.Task;
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
     * @param username (String)
     */
    public synchronized void createTask(String command, String username) {
        Map<String, Object> commandInfos = regexes.decomposeCommand(command);
        taskList.addTask(new Task(++idCountTasks, commandInfos, username, "pending"));
    }

    public synchronized void createTask(String command) {
        String username = "anonymous";
        Map<String, Object> commandInfos = regexes.decomposeCommand(command);
        taskList.addTask(new Task(++idCountTasks, commandInfos, username, "pending"));
    }

}
