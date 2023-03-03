package org.server;

import org.client.ClientRunnable;
import org.model.ServerSettings;
import org.model.Task;
import org.sharedClients.TaskFactoryInterface;
import org.utils.Regexes;

import java.util.Map;

public class TaskFactory implements TaskFactoryInterface {
    private int idCountTasks = 0;
    private final TaskList taskList;

    private final ServerSettings serverSettings;

    private final Regexes regexes;

    public TaskFactory(TaskList taskList, ServerSettings serverSettings) {
        this.taskList = taskList;
        this.serverSettings = serverSettings;
        this.regexes = Regexes.getInstance();
    }

    /**
     * Crée une tache sur base d'une commande et du thread client concerné
     * @param command (String)
     * @param user (ClientRunnable)
     */
    @Override
    public synchronized void createTask(String command, ClientRunnable user) {
        Map<String, Object> commandInfos = regexes.decomposeCommand(command);
        taskList.addTask(new Task(++idCountTasks, commandInfos, user, "pending"));
    }

    /**
     * Appel depuis relai !! (Pour commande SEND uniquement)
     * Crée une tache sur base d'une commande
     * @param command (String)
     */
    @Override
    public void createTask(String command) {
        taskList.addTask(new Task(++idCountTasks, regexes.decomposeCommand(command), null, "pending"));
    }

}
