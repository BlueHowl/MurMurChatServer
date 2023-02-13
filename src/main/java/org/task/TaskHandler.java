package org.task;

import org.model.Server;
import org.model.Task;
import org.model.User;
import org.model.exceptions.InvalidUserException;
import org.repository.DataInterface;
import org.repository.exceptions.NotSavedException;
import org.thread.TaskHandlerInterface;
import org.utils.Queries;
import org.utils.RandomStringUtil;
import org.utils.Regexes;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

public class TaskHandler implements TaskHandlerInterface, Runnable {
    private int idCountTasks = 0;
    private static final int RANDOM22SIZE = 22;
    private TaskList taskList;
    private Task task;
    private Server server;

    private DataInterface dataInterface;

    public TaskHandler(TaskList taskList, Server server, DataInterface dataInterface) {
        this.taskList = taskList;
        this.server = server;
        this.dataInterface = dataInterface;
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
                    //TODO si on check l'utilisateur avant de créer l'objet meilleur pratique
                    try {
                        User user = new User(commandLine.get("username"), Integer.parseInt(commandLine.get("bcryptround")),
                                commandLine.get("bcryptsalt"), commandLine.get("bcrypthash"), new ArrayList<>(),
                                new ArrayList<>(), 0);
                        server.addUser(user);
                        dataInterface.saveServerSettings(server);
                        return createTask("+OK", "", destination, "pending"); //TODO rajouter String command à Task
                    } catch (InvalidUserException | NotSavedException ex) {
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
                String key = RandomStringUtil.generateString(RANDOM22SIZE);
                task.getDestination().println(String.format(Queries.HELLO, server.getCurrentDomain(), key));
                break;
            case "+OK":
                task.getDestination().println(String.format(Queries.OK, "Message ok register test"));
                break;
        }
    }
}
