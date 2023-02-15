package org.server;

import org.client.ClientRunnable;
import org.model.Server;
import org.model.Tag;
import org.model.Task;
import org.model.User;
import org.model.exceptions.InvalidTagException;
import org.model.exceptions.InvalidUserException;
import org.repository.DataInterface;
import org.repository.exceptions.NotSavedException;
import org.utils.Queries;
import org.utils.RandomStringUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

public class Executor implements Runnable{

    private static final int RANDOM22SIZE = 22;

    private final TaskList taskList;

    private final Server server;

    private final DataInterface dataInterface;
    private String key;

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
                hello(client);
                break;
            case "REGISTER":
                register(commandMap, client);
                break;
            case "CONNECT":
                connect(commandMap, client);
                break;
            case "CONFIRM":
                confirm(commandMap, client);
                break;
            case "MSG":
                break;
            case "FOLLOW":
                follow(commandMap, client);
                break;
            case "DISCONNECT":

                break;
        }
    }

    private void hello(ClientRunnable client) {
        key = RandomStringUtil.generateString(RANDOM22SIZE);
        client.sendMessage(String.format(Queries.HELLO, server.getCurrentDomain(), key));
    }

    private void register(Map<String, String> commandMap, ClientRunnable client) {
        try {
            User user = new User(commandMap.get("username"), Integer.parseInt(commandMap.get("bcryptround")),
                    commandMap.get("bcryptsalt"), commandMap.get("bcrypthash"), new ArrayList<>(),
                    new ArrayList<>(), 0);
            server.addUser(user);
            dataInterface.saveServerSettings(server);
            client.sendMessage(String.format(Queries.OK, "Le compte est enregistré"));
            System.out.println("Compte enregistré");
            client.setUser(user);
        } catch (InvalidUserException | NotSavedException ex) {
            client.sendMessage(String.format(Queries.ERR, ex.getMessage()));
            System.out.println("Erreur envoyé, erreur d'enregistrement");
        }
    }

    private void connect(Map<String, String> commandMap, ClientRunnable client) {
        try {
            User user = server.findUser(commandMap.get("username"));
            client.sendMessage(String.format(Queries.PARAM, user.getBcryptRotations(), user.getBcryptSalt()));
            client.setUser(user);
        } catch (InvalidUserException ex) {
            client.sendMessage(String.format(Queries.ERR, ex.getMessage()));
        }
        System.out.println("Sending PARAM");
    }

    private void confirm(Map<String, String> commandMap, ClientRunnable client) {
        User user = client.getUser();
        String sha3hex = commandMap.get("sha3hex");
        String comparable;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            byte[] hash = digest.digest((key + "$2b$" + user.getBcryptRotations() + "$" + user.getBcryptSalt() + user.getBcryptHash()).getBytes(StandardCharsets.UTF_8));
            comparable = bytesToHex(hash);
            if (sha3hex.equals(comparable)) {
                client.sendMessage(String.format(Queries.OK, "Welcome!"));
                System.out.println("Sending +OK");
            } else{
                client.sendMessage(String.format(Queries.ERR, "Wrong password"));
                System.out.println("Sending -ERR: Wrong password!");
            }
        } catch (NoSuchAlgorithmException ex) {
            client.sendMessage(String.format(Queries.ERR, ex.getMessage()));
        }
    }

    private void msg(Map<String, String> commandMap, ClientRunnable client) {

    }

    private void follow(Map<String, String> commandMap, ClientRunnable client) {
        String domain = commandMap.get("domain");
        if (commandMap.get("name") != null) {
            try {
                User followedUser = server.findUser(commandMap.get("name"));
                server.addFollowerToUser(followedUser, client.getUser().getUsername());
            } catch (InvalidUserException ex) {
                System.out.println("Le compte n'existe pas");
            }
        } else {
            String followedTagString = commandMap.get("tag");
            if (server.tagExists(followedTagString)) {

            } else {
                try {
                    Tag newTag = new Tag(followedTagString, new ArrayList<>());
                    newTag.addFollower(client.getUser().getUsername()+"@"+domain);
                    client.getUser().addUserTag(newTag.getTag()+"@"+domain);
                } catch (InvalidTagException ex) {

                }

            }
        }
        System.out.println("Follow reçu");
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte h : hash) {
            String hex = Integer.toHexString(0xff & h);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
