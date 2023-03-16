package org.server;

import org.client.ClientRunnable;
import org.model.ServerSettings;
import org.model.Tag;
import org.model.Task;
import org.model.User;
import org.model.exceptions.InvalidUserException;
import org.relay.RelayManager;
import org.repository.DataInterface;
import org.repository.exceptions.NotSavedException;
import org.server.exception.CloseClientException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Executor implements Runnable {

    private static final String CARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*,-./:;<=>?@[]^_`";
    private static final int RANDOM22SIZE = 22;
    private static final String HELLO = "HELLO %s %s\r\n";
    private static final String PARAM = "PARAM %d %s\r\n";
    private static final String MSGS = "MSGS %s %s\r\n";
    private static final String OK = "+OK[ %s]\r\n";
    private static final String ERR = "-ERR[ %s]\r\n";

    private static final String FOLLOW = "FOLLOW %s\r\n";

    private static final String SEND = "SEND %d@%s %s %s %s\r\n";

    private final TaskList taskList;

    private final ClientManager clientManager;

    private final RelayManager relayManager;

    private final ServerSettings serverSettings;

    private final DataInterface dataInterface;


    public Executor(TaskList taskList, ClientManager clientManager, RelayManager relayManager, ServerSettings server, DataInterface dataInterface) {
        this.taskList = taskList;
        this.clientManager = clientManager;
        this.relayManager = relayManager;
        this.serverSettings = server;
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
        Map<String, Object> commandMap = task.getCommandMap();
        ClientRunnable client = task.getClient();

        switch((String)commandMap.get("type")) {
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
                msg(commandMap, client, task.getId());
                break;
            case "FOLLOW":
                follow(commandMap, client, task.getId());
                break;
            case "DISCONNECT":
                disconnect(client);
                break;

            case "SEND":
                send(commandMap);
                break;
        }
    }

    private void hello(ClientRunnable client) {
        String key = generateRandomString(RANDOM22SIZE);
        client.setConnectionKey(key);
        client.send(String.format(HELLO, serverSettings.getCurrentDomain(), key));
    }

    private void register(Map<String, Object> commandMap, ClientRunnable client) {
        try {
            User user = new User((String)commandMap.get("username"), Integer.parseInt((String)commandMap.get("bcryptround")),
                    (String)commandMap.get("bcryptsalt"), (String)commandMap.get("bcrypthash"), new HashSet<>(),
                    new HashSet<>(), 0);
            serverSettings.addUser(user);
            dataInterface.saveServerSettings(serverSettings); //sauvegarde json
            client.send(String.format(OK, "Le compte est enregistré"));
            System.out.println("Compte enregistré");
            client.setUser(user);
        } catch (InvalidUserException | NotSavedException ex) {
            client.send(String.format(ERR, ex.getMessage()));
            System.out.println("Erreur envoyé, erreur d'enregistrement");
        }
    }

    private void connect(Map<String, Object> commandMap, ClientRunnable client) {
        try {
            User user = serverSettings.findUser((String)commandMap.get("username"));
            client.send(String.format(PARAM, user.getBcryptRotations(), user.getBcryptSalt()));
            client.setUser(user);
        } catch (InvalidUserException ex) {
            client.send(String.format(ERR, ex.getMessage()));
        }
        System.out.println("Sending PARAM");
    }

    private void confirm(Map<String, Object> commandMap, ClientRunnable client) {
        try {
            User user = client.getUser();
            String sha3hex = (String)commandMap.get("sha3hex");

            MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            byte[] hash = digest.digest((client.getConnectionKey() + "$2b$" + user.getBcryptRotations() + "$" + user.getBcryptSalt() + user.getBcryptHash()).getBytes(StandardCharsets.UTF_8));
            String comparable = bytesToHex(hash);
            if (sha3hex.equals(comparable)) {
                client.send(String.format(OK, "Welcome!"));
                System.out.println("Sending +OK");

                Map<String, List<String>> messages = serverSettings.getOfflineMessages();
                String username = user.getUsername();
                if (messages != null && messages.containsKey(username) && messages.get(username) != null) {
                    for (String message : messages.get(username)) {
                        client.send(message);
                    }

                    messages.get(username).clear();
                    serverSettings.saveMessages(messages);
                }

            } else{
                client.send(String.format(ERR, "Wrong password"));
                System.out.println("Sending -ERR: Wrong password!");
            }
        } catch (NoSuchAlgorithmException ex) {
            client.send(String.format(ERR, ex.getMessage()));
        }
    }

    /**
     * Envoyer un message
     * @param commandMap (Map<String, Object>) contient le message et les hashtags
     * @param client (ClientRunnable) client qui envoie le message
     */
    private void msg(Map<String, Object> commandMap, ClientRunnable client, int taskId) {
        String nameDomain = String.format("%s@%s", client.getUsername(), serverSettings.getCurrentDomain());

        String msgs = String.format(MSGS, nameDomain, commandMap.get("message"));


        //envoi aux followers du client
        if (client.getFollowers() != null)
        for (String follower : client.getFollowers()) {
            sendMessageToFollower(follower, msgs, client.getUsername(), taskId);
        }

        //envoi aux followers des tags todo debug
        for (String hashtag : (String[])commandMap.get("hashtags")) {
            System.out.println("tag detecté");
            if(serverSettings.tagExists(hashtag)) {
                System.out.println("tag sur le serveur courant");
                List<String> followers = serverSettings.getTagFollowers(hashtag);
                if (followers != null)
                for (String follower : followers) {
                    sendMessageToFollower(follower, msgs, client.getUsername(), taskId);
                }
            } else {

                System.out.println("tag sur un serveur distant");
                //si le serveur ne contient pas le tag alors chercher le tag complet dans l'utilisateur et envoyer SEND
                String completeTag = serverSettings.getCompleteTag(hashtag, client.getUsername());
                if(completeTag != null) {
                    System.out.printf("envoi msg tag relai : %s", String.format(SEND, taskId, serverSettings.getCurrentDomain(), client.getUsername()+"@"+serverSettings.getCurrentDomain(), completeTag, msgs));
                    relayManager.sendToRelay(String.format(SEND, taskId, serverSettings.getCurrentDomain(), client.getUsername()+"@"+serverSettings.getCurrentDomain(), completeTag, msgs));
                }
            }

        }

    }

    /**
     * Envoi un msgs au follower
     * @param follower (String) follower
     * @param msgs (String) message
     * @param sender (String) nom de l'expéditeur
     * @param taskId (int) id de la tâche
     */
    private void sendMessageToFollower(String follower, String msgs, String sender, int taskId) {
        if(follower.contains(serverSettings.getCurrentDomain())) {
            ClientRunnable dest = clientManager.getMatchingClient(serverSettings.getCurrentDomain(), follower);
            if(dest != null) {
                dest.send(msgs);
            } else {
                //si destinataire null alors on stocke le message
                serverSettings.addOfflineMessage(follower, msgs);
            }
        } else {
            //si domaine reçu ne correspond pas à celui du serveur alors envoi send
            relayManager.sendToRelay(String.format(SEND, taskId, serverSettings.getCurrentDomain(), sender+"@"+serverSettings.getCurrentDomain(), follower, msgs)); // todo gèrer id
        }
    }

    private void follow(Map<String, Object> commandMap, ClientRunnable client, int taskId) {
        try {
            String domain = (String)commandMap.get("domain");

            //si on est dans le nom de domaine courant
            if(domain.contains(serverSettings.getCurrentDomain())) {
                if (commandMap.get("name") != null) {
                    //follow utilisateur
                    try {
                        User followedUser = serverSettings.findUser((String) commandMap.get("name"));
                        serverSettings.addFollowerToUser(followedUser, String.format("%s@%s", client.getUsername(), domain));
                    } catch (InvalidUserException e) {
                        System.out.println(e.getMessage());
                        System.out.println("L'user est invalide");
                    }
                } else {
                    //follow tag
                    String followedTagString = (String) commandMap.get("tag");
                    Tag tag = serverSettings.findTag(followedTagString);
                    if (tag.getFollowers().isEmpty()) {
                        tag.addFollower(client.getUsername() + "@" + domain);
                        serverSettings.addTag(tag);
                        serverSettings.addUserTagToUser(client.getUser(), tag.getName() + "@" + domain);
                    } else {
                        serverSettings.addFollowerToTag(tag, client.getUsername() + "@" + domain);
                        serverSettings.addUserTagToUser(client.getUser(), followedTagString + "@" + domain);
                    }
                }
                dataInterface.saveServerSettings(serverSettings);
            } else {
                //si le domaine reçu ne correspond pas à celui du serveur alors send
                String follow = String.format("%s@dummy", (String) ((String)commandMap.get("tag") != null ? commandMap.get("tag") : commandMap.get("name")));
                relayManager.sendToRelay(String.format(SEND, taskId, serverSettings.getCurrentDomain(), client.getUsername()+"@"+serverSettings.getCurrentDomain(), String.format("dummy@%s", domain), String.format(FOLLOW, follow))); // todo gèrer id

                if((String)commandMap.get("tag") != null) {
                    //todo enregistre le follow à l'utilisateur sans garantie de l'enregistrement distant
                    serverSettings.addUserTagToUser(client.getUser(), (String)commandMap.get("tag") + "@" + domain);
                }
            }

            System.out.println("Follow reçu");
        } catch (NotSavedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gère le message SEND reçu
     * @param commandMap (Map<String, Object>)
     */
    private void send(Map<String, Object> commandMap) {
        if(commandMap.get("domain") == null) { //champ domain provient de la décomposition de FOLLOW
            //gestion MSGS
            String message = (String) commandMap.get("internalmsg");
            String sender = (String) commandMap.get("sender");

            if((String)commandMap.get("destnamedomain") != null) {
                //envoi le message au follower concerné
                //todo gèrer id
                sendMessageToFollower((String) commandMap.get("destnamedomain"), message, sender, 0);
            } else {
                //envoi le message aux followers du tag
                String tag = (String) commandMap.get("desttag");
                if(serverSettings.tagExists(tag)) {
                    System.out.println("tag sur le serveur courant");
                    List<String> followers = serverSettings.getTagFollowers(tag);
                    for (String follower : followers) {
                        sendMessageToFollower(follower, message, sender, 0); //todo gèrer id
                    }
                }
            }

            System.out.println("Message distant reçu");

        } else {
            //gestion follow
            try {
                if (commandMap.get("name") != null) {
                    //follow utilisateur
                    try {
                        User followedUser = serverSettings.findUser((String) commandMap.get("name"));
                        serverSettings.addFollowerToUser(followedUser, (String) commandMap.get("sender"));
                    } catch (InvalidUserException e) {
                        System.out.println(e.getMessage());
                        System.out.println("L'user est invalide");
                    }
                } else {
                    //follow tag
                    String followedTagString = (String) commandMap.get("tag");
                    Tag tag = serverSettings.findTag(followedTagString);
                    if (tag.getFollowers().isEmpty()) {
                        tag.addFollower((String) commandMap.get("sender"));
                        serverSettings.addTag(tag);
                        //déjà ajouté à l'envoi du send serverSettings.addUserTagToUser(client.getUser(), tag.getName() + "@" + domain);
                    } else {
                        serverSettings.addFollowerToTag(tag, (String) commandMap.get("sender"));
                        //déjà ajouté à l'envoi du send serverSettings.addUserTagToUser(client.getUser(), followedTagString + "@" + domain);
                    }
                }
                dataInterface.saveServerSettings(serverSettings);

                System.out.println("Follow distant reçu");
            } catch (NotSavedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Déconnecte le client
     * @param client (ClientRunnable)
     */
    private void disconnect(ClientRunnable client) {

        try { //todo stop le thread ?
            client.send(String.format(OK, "GoodBye!"));
            clientManager.removeClient(client);
            System.out.printf("Déconnexion client : %s\n", client.getUsername());
        } catch (CloseClientException e) {
            System.out.printf("%s : %s\n", e.getMessage(), client.getUsername());
        }

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

    /**
     * Génère une chaine de caractères aléatoire de la taille donnée et contenant les caractères autorisés
     * @param length (int) Taille de la chaine à retourner
     * @return (String) chaine de caractères aléatoire
     */
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        random.setSeed(new Date().getTime());

        for(int i = 0; i < length; i++) {
            int index = random.nextInt(CARACTERS.length());

            sb.append(CARACTERS.charAt(index));
        }

        return sb.toString();
    }
}
