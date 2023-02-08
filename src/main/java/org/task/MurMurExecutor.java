package org.task;

public class MurMurExecutor implements Runnable{
    private final TaskList taskList;

    public MurMurExecutor(TaskList taskList) {
        this.taskList = taskList;
    }

    @Override
    public void run() {
        //TODO executer les taches si les taches ne sont pas null
        while (true){
            Task task = taskList.getTask();
            if (task != null){
                task.execute();
            }
        }
    }

}
