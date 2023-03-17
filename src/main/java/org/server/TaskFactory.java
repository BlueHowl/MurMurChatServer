package org.server;

import org.client.ClientRunnable;
import org.model.Task;
import org.sharedClients.TaskFactoryInterface;
import org.utils.AESGCM;
import org.utils.Regexes;

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
        taskList.addTask(new Task(++idCountTasks, regexes.decomposeCommand(command), user, "pending"));
    }

    /**
     * Appel depuis relai !! (Pour commande SEND uniquement)
     * Crée une tache sur base d'une commande
     * @param command (String)
     */
    @Override
    public void createTask(String command) {
        String uncryptedText = aesgcm.decrypt(command);
        taskList.addTask(new Task(++idCountTasks, regexes.decomposeCommand(uncryptedText.trim()), null, "pending"));
    }

}
