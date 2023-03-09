package org.server;

import org.client.ClientRunnable;
import org.model.Task;
import org.sharedClients.TaskFactoryInterface;
import org.utils.AESGCM;
import org.utils.Regexes;

import java.util.Map;

public class TaskFactory implements TaskFactoryInterface {
    private int idCountTasks = 0;
    private final TaskList taskList;

    private final AESGCM aesgcm;

    private final Regexes regexes;

    public TaskFactory(TaskList taskList, AESGCM aesgcm) {
        this.taskList = taskList;
        this.aesgcm = aesgcm;
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
        Map<String, String> messageInfos = regexes.decomposeRelayMsg(command);
        String uncryptedText = aesgcm.decrypt(messageInfos.get("message"), messageInfos.get("ivnonce"));
        taskList.addTask(new Task(++idCountTasks, regexes.decomposeCommand(uncryptedText), null, "pending"));
    }

}
