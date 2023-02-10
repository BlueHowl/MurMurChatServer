package org.task;

import org.model.Task;

public class TaskFactory {
    private Task task;
    public TaskFactory(TaskList taskList) {

    }

    //TODO Gérer la création des différentes tasks
    public void createTask() {
        task = new Task();

    }

    public Task getTask() {
        return task;
    }
}
