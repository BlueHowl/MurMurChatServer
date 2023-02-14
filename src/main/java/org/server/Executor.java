package org.server;

import org.client.ClientRunnable;
import org.model.Server;
import org.model.Task;
import org.model.User;
import org.model.exceptions.InvalidUserException;
import org.repository.DataInterface;
import org.repository.exceptions.NotSavedException;
import org.utils.Queries;
import org.utils.RandomStringUtil;

import java.util.ArrayList;
import java.util.Map;

public class Executor implements Runnable{

    private static final int RANDOM22SIZE = 22;

    private final TaskList taskList;

    private final Server server;

    private final DataInterface dataInterface;

    public Executor(TaskList taskList, Server server, DataInterface dataInterface) {
        this.taskList = taskList;
        this.server = server;
        this.dataInterface = dataInterface;
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

    /**
     * Execute la tache
     * @param task (Task)
     */
    private void execute(Task task) {
        Map<String, String> commandMap = task.getCommandMap();
        ClientRunnable client = task.getClient();

        switch(commandMap.get("type")) {
            case "HELLO":
                String key = RandomStringUtil.generateString(RANDOM22SIZE);
                client.sendMessage(String.format(Queries.HELLO, server.getCurrentDomain(), key));
                break;
            case "REGISTER":
                //TODO si on check l'utilisateur avant de créer l'objet meilleur pratique
                try {
                    User user = new User(commandMap.get("username"), Integer.parseInt(commandMap.get("bcryptround")),
                            commandMap.get("bcryptsalt"), commandMap.get("bcrypthash"), new ArrayList<>(),
                            new ArrayList<>(), 0);
                    server.addUser(user);
                    dataInterface.saveServerSettings(server);
                    client.sendMessage(String.format(Queries.OK, "Le compte est enregistré"));
                } catch (InvalidUserException | NotSavedException ex) {
                    client.sendMessage(String.format(Queries.ERR, "Erreur lors de l'enregistrement"));
                }
            case "CONNECT":
                break;
        }
    }
}
