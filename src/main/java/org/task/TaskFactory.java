package org.task;

import org.model.Task;
import org.thread.TaskFactoryInterface;

public class TaskFactory implements TaskFactoryInterface {
    private int idCount;

    private TaskList taskList;
    private Task task;

    public TaskFactory(TaskList taskList) {
        this.taskList = taskList;
    }

    //TODO Gérer la création des différentes tasks

    /**
     * Crée une nouvelle tache et la met dans la queue
     * @param typeSended (String)
     * @param source (String)
     * @param destination (String)
     */
    public void createTask(String typeSended, String source, String destination) {
        switch(typeSended) {

        }
        taskList.addTask(new Task(++idCount, typeSended, source, destination, "pending"));
    }

}
