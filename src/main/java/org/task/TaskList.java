package org.task;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class TaskList {
    private final Queue<Task> tasks;

    public TaskList() {
        tasks = new LinkedBlockingDeque<>();
    }

    public void createSendMessageTask() {
        //TODO ajouter la tache dans la liste
    }

    public void createMsgsMessageTask() {
        //TODO ajouter la tache dans la liste
    }

    public Task getTask() {
        //TODO retourner la tache suivante
        return tasks.poll();
    }
}
