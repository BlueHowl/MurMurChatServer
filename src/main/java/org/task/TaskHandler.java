package org.task;

import org.model.Server;
import org.model.Task;
import org.model.User;
import org.model.exceptions.InvalidUserException;
import org.thread.TaskHandlerInterface;
import org.utils.Queries;
import org.utils.RandomStringUtil;
import org.utils.Regexes;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

public class TaskHandler implements TaskHandlerInterface, Runnable {
    private int idCountTasks = 0;
    private static final int SIZE = 22;
    private TaskList taskList;
    private Task task;
    private Server server;

    public TaskHandler(TaskList taskList, Server server) {
        this.taskList = taskList;
        this.server = server;
    }

    //TODO Gérer la création des différentes tasks

    /**
     */
    public synchronized void processTask(String line, PrintWriter destination) {
        Map<String, String> commandInfos = Regexes.decomposeCommand(line);
        taskList.addTask(selectTask(commandInfos, destination));
    }

    /**
     * Détermine quelle tâche il faut crée en fonction de la commande
     * @param commandLine Map<String, String> la commande
     */
    private Task selectTask(Map<String, String> commandLine, PrintWriter destination) {
        String type = commandLine.get("type");
            switch(type) {
                case "REGISTER":
                    //TODO savoir pour le bcrypt salt
                    try {
                        User user = new User(commandLine.get("username"), Integer.parseInt(commandLine.get("bcryptround")),
                                commandLine.get("bcryptsalt"), commandLine.get("bcrypthash"), new ArrayList<String>(),
                                new ArrayList<>(), 0);
                        server.addUser(user);
                        return createTask("+OK", "", destination, "pending");
                    } catch (InvalidUserException ex) {
                        return createTask("-ERR", "", destination, "pending");
                    }
                case "CONNECT":
                    break;
            }
        return null;
    }

    public void createHello(PrintWriter destination) {
        taskList.addTask(createTask("HELLO", "", destination, "pending"));
    }

    private Task createTask(String type, String source, PrintWriter destination, String status) {
        idCountTasks++;
        return new Task(idCountTasks, type, source, destination, status);
    }

    @Override
    public void run() {
        while(true) {
            Task task = taskList.getTask();
            if (task != null) {
                execute(task);
            }
        }
    }

    private void execute(Task task) {
        switch(task.getType()) {
            case "HELLO":
                String key = RandomStringUtil.generateString(SIZE);
                task.getDestination().println(String.format(Queries.HELLO, server.getCurrentDomain(), key));
                break;
            case "OK":

                break;
        }
    }
}
