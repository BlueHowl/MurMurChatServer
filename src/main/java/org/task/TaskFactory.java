package org.task;

import org.model.Task;

public class TaskFactory {
    private int idCount;
    private Task task;
    public TaskFactory(TaskList taskList) {
        idCount = 1;
    }

    //TODO Gérer la création des différentes tasks
    public void createTask(String typeSended, String source, String destination) {
        switch(typeSended) {

        }
        task = new Task(idCount, typeSended, source, destination, "pending");
        idCount++;
    }

    /**
     * Une fonction qui retourne la tâche crée
     * @return la tâche créee
     */
    public Task getTask() {
        return task;
    }
}
